package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.config.AppConfig;
import com.tcss559.alltollpass.entity.User;
import com.tcss559.alltollpass.entity.traveler.TransactionType;
import com.tcss559.alltollpass.entity.traveler.TravelerAccount;
import com.tcss559.alltollpass.entity.traveler.TravelerRfid;
import com.tcss559.alltollpass.entity.traveler.TravelerTransaction;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.ExtrenalServiceException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.external.SMSRequest;
import com.tcss559.alltollpass.model.request.traveler.DebitRequest;
import com.tcss559.alltollpass.model.request.traveler.RfidRequest;
import com.tcss559.alltollpass.model.request.traveler.TravelerBalance;
import com.tcss559.alltollpass.model.response.traveler.*;
import com.tcss559.alltollpass.repository.UserRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerAccountRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerRfidRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sikha
 * This service provides business logic for the end points made for traveler-service (End user)
 */

@Service
public class TravelerService {
    @Autowired
    AppConfig appConfig;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TravelerRfidRepository travelerRfidRepository;

    @Autowired
    TravelerTransactionRepository travelerTransactionRepository;

    @Autowired
    TravelerAccountRepository travelerAccountRepository;

    @Autowired
    ExternalService externalService;


    /**
     * User account balance based methods
     *
     */
    // Credit user account - used for recharge
    public TravelerBalance creditTransaction(TravelerBalance travelerBalance) throws DatabaseException, UserNotFoundException {

        // insert into the entity maintaining history of  users transcation
        TravelerTransaction travelerTransaction =  TravelerTransaction.builder()
                .amount(travelerBalance.getAmount())
                .type(TransactionType.CREDIT)
                .userId(travelerBalance.getUserId())
                .build();

        // update users account - update balance
        return updateTravelerBalance(travelerTransaction, travelerBalance.getUserId());

    }

    // Fetch the current account balance of the user
    public double getBalance(Long userId) throws UserNotFoundException {
        return travelerAccountRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found")).getBalance();
    }

    // Debit user account - for collecting toll
    public TravelerBalance debitTransaction(DebitRequest debitRequest) throws DatabaseException, RfidNotFoundException {

        // insert into the entity maintaining history of  users transcation
        TravelerTransaction travelerTransaction =  TravelerTransaction.builder()
                .amount(debitRequest.getAmount())
                .rfid(debitRequest.getRfid())
                .tollLocation(debitRequest.getTollLocation())
                .type(TransactionType.DEBIT)
                .userId(debitRequest.getUserId())
                .build();

        // update users account - update balance
        return updateTravelerBalance(travelerTransaction, debitRequest.getUserId());
    }

    // update users account - update balance
    private TravelerBalance updateTravelerBalance(TravelerTransaction travelerTransaction, Long userId) throws UserNotFoundException, ExtrenalServiceException {
        User user = userRepository.findByIdAndIsActiveTrue(userId).orElseThrow(() -> new UserNotFoundException("The User/Agency is deactivated"));
        TravelerAccount travelerAccount =  travelerAccountRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not found"));

        try {

            TravelerTransaction savedTransaction = travelerTransactionRepository.save(travelerTransaction);

            // update TravelerAccount
            double updatedBalance = savedTransaction.getType() == TransactionType.CREDIT ?
                    (travelerAccount.getBalance() + savedTransaction.getAmount()) :
                    (travelerAccount.getBalance() - savedTransaction.getAmount());

            travelerAccount.setBalance(updatedBalance);
            TravelerAccount savedAccount = travelerAccountRepository.save(travelerAccount);

            externalService.sendSMS(SMSRequest.builder()
                            .transactionType(savedTransaction.getType())
                            .currentBalance(savedAccount.getBalance())
                            .number(user.getMobile())
                            .amount(savedTransaction.getAmount())
                    .build());

            return TravelerBalance.builder().userId(savedAccount.getId()).amount(savedAccount.getBalance()).build();
        }
        catch (Exception ex){
            throw new DatabaseException(ex);
        }
    }

    /**
     * User RFID based methods
     *
     */
    // add a given RFID to a user
    public TravelerResponse addRfid(RfidRequest rfidRequest) throws DatabaseException, UserNotFoundException {

        User user = userRepository.findById(rfidRequest.getUserId()).orElseThrow(() -> new UserNotFoundException("No user ID found"));

        TravelerRfid rfid = TravelerRfid.builder()
                .userId(rfidRequest.getUserId())
                .rfid(rfidRequest.getRfid())
                .vehicleType(rfidRequest.getVehicleType())
                .build();


        try{
            TravelerRfid savedRfid = travelerRfidRepository.save(rfid);

            // return the list of rfids for disply
            return getAllRfid(user.getId());

        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    // fetch all RFIDs of a user
    public TravelerResponse getAllRfid(Long userId) throws DatabaseException {
        List<TravelerRfid> list = travelerRfidRepository.findByUserIdAndIsActiveTrue(userId);
        return TravelerResponse.builder()
                .rfids(
                    list.stream()
                    .map(it -> TravelerRfidResponse.builder()
                            .rfid(it.getRfid())
                            .createdTimestamp(it.getCreatedTimestamp())
                            .vehicleType(it.getVehicleType())
                            .build()
                    )
                    .collect(Collectors.toList())
                )
                .userId(userId)
                .build();
    }

    // Get the vehicle and userID for a given RFID
    // Assumption is all RFIDs are unique
    public RfidResponse getByRfid(String rfid) throws DatabaseException, RfidNotFoundException{
        TravelerRfid existingRfid = travelerRfidRepository.findByRfid(rfid).orElseThrow(() -> new RfidNotFoundException("RFID not found"));
        return RfidResponse.builder()
                .rfid(existingRfid.getRfid())
                .userId(existingRfid.getUserId())
                .vehicleType(existingRfid.getVehicleType())
                .build();
    }

    // Delete RFID for a given user
    public TravelerResponse deleteRfid(String rfid) throws DatabaseException, UserNotFoundException {
        TravelerRfid existingRfid = travelerRfidRepository.findByRfid(rfid).orElseThrow(() -> new UserNotFoundException("RFID not found"));
        try{

            existingRfid.setActive(false);
            travelerRfidRepository.save(existingRfid);
            return getAllRfid(existingRfid.getUserId());

        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }



    /**
     * User reports based methods
     *
     */

    // Get the report of user transaction history
    public TravelerTransactionResponse getTransactionReports(Long userId) throws DatabaseException {
        try {
            List<TravelerTransaction> transactions = travelerTransactionRepository.findByUserId(userId);
            return TravelerTransactionResponse.builder()
                    .userId(userId)
                    .transactions(transactions.stream()
                            .map(it ->
                                    TransactionDetail.builder()
                                            .rfid(it.getRfid())
                                            .type(it.getType())
                                            .amount(it.getAmount())
                                            .createdTimestamp(it.getTimestamp().toString())
                                            .tollLocation(it.getTollLocation())
                                            .build()
                            )
                            .collect(Collectors.toList())
                    )
                    .build();
        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    // Sned  the report of user transaction history to his emal
    public void sendReportByEmail(Long userId) throws DatabaseException {
        try {
            TravelerTransactionResponse travelerTransactionResponse = getTransactionReports(userId);
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            externalService.sendEmail(travelerTransactionResponse.getTransactions(), user, TransactionDetail.class);
        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    /**
     * Traveller account creation
     *
     */


    // When a user regeisters as traveler, also create a account for him with zero balance
    public TravelerAccount createTravelerAccount(Long userId) throws RuntimeException{
        return travelerAccountRepository.save(TravelerAccount.builder().id(userId).build());
    }






}
