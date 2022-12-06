package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.entity.User;
import com.tcss559.alltollpass.entity.traveler.*;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
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

@Service
public class TravelerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TravelerRfidRepository travelerRfidRepository;

    @Autowired
    TravelerTransactionRepository travelerTransactionRepository;

    @Autowired
    TravelerAccountRepository travelerAccountRepository;





    // Balance
    public TravelerBalance creditTransaction(TravelerBalance travelerBalance) throws DatabaseException, UserNotFoundException {

        // insert into TravelerTransaction
        TravelerTransaction travelerTransaction =  TravelerTransaction.builder()
                .amount(travelerBalance.getAmount())
                .type(TransactionType.CREDIT)
                .userId(travelerBalance.getUserId())
                .build();

        return updateTravelerBalance(travelerTransaction, travelerBalance.getUserId());

    }

    public double getBalance(Long userId) {
        return travelerAccountRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found")).getBalance();
    }

    //RFID Methods
    public TravelerResponse addRfid(RfidRequest rfidRequest) throws DatabaseException, UserNotFoundException {

        User user = userRepository.findById(rfidRequest.getUserId()).orElseThrow(() -> new UserNotFoundException(""));

        TravelerRfid rfid = TravelerRfid.builder()
                .userId(rfidRequest.getUserId())
                .rfid(rfidRequest.getRfid())
                .vehicleType(rfidRequest.getVehicleType())
                .build();


        try{
            TravelerRfid savedRfid = travelerRfidRepository.save(rfid);
            List<TravelerRfid> list = travelerRfidRepository.findByUserId(user.getId());

            return TravelerResponse.builder()
                    .userId(user.getId())
                    .rfids(list.stream()
                            .map(it -> TravelerRfidResponse.builder()
                                    .rfid(it.getRfid())
                                    .createdTimestamp(it.getCreatedTimestamp())
                                    .vehicleType(it.getVehicleType())
                                    .build()
                            )
                            .collect(Collectors.toList())
                    )
                    .build();

        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    public TravelerResponse getAllRfid(Long userId) {
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

    public RfidResponse getByRfid(String rfid) {
        TravelerRfid existingRfid = travelerRfidRepository.findByRfid(rfid).orElseThrow(() -> new RfidNotFoundException("RFID not found"));
        return RfidResponse.builder()
                .rfid(existingRfid.getRfid())
                .userId(existingRfid.getUserId())
                .vehicleType(existingRfid.getVehicleType())
                .build();
    }

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

    public VehicleType getVehicleTypeFromRfid(String rfid) throws UserNotFoundException{
        TravelerRfid existingRfid = travelerRfidRepository.findByRfid(rfid).orElseThrow(() -> new UserNotFoundException("RFID not found"));
        return existingRfid.getVehicleType();
    }

    // Reporrts

    public TravelerTransactionResponse getTransactionReports(Long userId) {
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
                                            .createdTimestamp(it.getTimestamp())
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

    public void sendReportByEmail(Long userId) {
        try {
            TravelerTransactionResponse travelerTransactionResponse = getTransactionReports(userId);
            //TODO: create CSV and send email
        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    // TravelerAccount creation

    public TravelerAccount createTravelerAccount(Long userId){
        return travelerAccountRepository.save(TravelerAccount.builder().id(userId).build());
    }

    public void getUserByRfid(String rfid){
        travelerRfidRepository.findByRfid(rfid).orElseThrow(() -> new UserNotFoundException("RFID not found"));

    }

    // add Toll Transactions
    public TravelerBalance debitTransaction(DebitRequest debitRequest) throws DatabaseException, RfidNotFoundException {

        TravelerTransaction travelerTransaction =  TravelerTransaction.builder()
                .amount(debitRequest.getAmount())
                .rfid(debitRequest.getRfid())
                .tollLocation(debitRequest.getTollLocation())
                .type(TransactionType.DEBIT)
                .userId(debitRequest.getUserId())
                .build();

        return updateTravelerBalance(travelerTransaction, debitRequest.getUserId());
    }

    private TravelerBalance updateTravelerBalance(TravelerTransaction travelerTransaction, Long userId) {
        TravelerAccount travelerAccount =  travelerAccountRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not found"));

        try {

            TravelerTransaction savedTransaction = travelerTransactionRepository.save(travelerTransaction);

            // update TravelerAccount
            double updatedBalance = travelerTransaction.getType() == TransactionType.CREDIT ?
                    (travelerAccount.getBalance() + savedTransaction.getAmount()) :
                    (travelerAccount.getBalance() - savedTransaction.getAmount());

            travelerAccount.setBalance(updatedBalance);
            TravelerAccount savedAccount = travelerAccountRepository.save(travelerAccount);

            //TODO: send sms to user on mobile

            return TravelerBalance.builder().userId(savedAccount.getId()).amount(savedAccount.getBalance()).build();
        }
        catch (Exception ex){
            throw new DatabaseException(ex);
        }
    }


}
