package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.entity.User;
import com.tcss559.alltollpass.entity.traveler.*;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.traveler.RfidRequest;
import com.tcss559.alltollpass.model.request.traveler.TravelerBalance;
import com.tcss559.alltollpass.model.response.traveler.TransactionDetail;
import com.tcss559.alltollpass.model.response.traveler.TravelerResponse;
import com.tcss559.alltollpass.model.response.traveler.TravelerRfidResponse;
import com.tcss559.alltollpass.model.response.traveler.TravelerTransactionResponse;
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
    public TravelerBalance updateTravelerAccount(TravelerBalance travelerBalance) throws DatabaseException, UserNotFoundException {

        // insert into TravelerTransaction
        TravelerTransaction travelerTransaction =  TravelerTransaction.builder()
                .amount(travelerBalance.getAmount())
                .type(TransactionType.CREDIT)
                .userId(travelerBalance.getUserId())
                .build();

        TravelerAccount travelerAccount =  travelerAccountRepository.findById(travelerBalance.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User Not found"));

        try {

            TravelerTransaction savedTransaction = travelerTransactionRepository.save(travelerTransaction);

            // update TravelerAccount
            travelerAccount.setBalance(travelerAccount.getBalance() + savedTransaction.getAmount());
            TravelerAccount savedAccount = travelerAccountRepository.save(travelerAccount);

            return TravelerBalance.builder().userId(savedAccount.getId()).amount(savedAccount.getBalance()).build();
        }
        catch (Exception ex){
            throw new DatabaseException(ex);
        }

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

    public TravelerTransactionResponse getReports(Long userId) {
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

    public void sendReport(Long userId) {
        try {
            TravelerTransactionResponse travelerTransactionResponse = getReports(userId);
            //TODO: create CSV and send email
        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    // TravelerAccount creation

    public TravelerAccount createTravelerAccount(Long userId){
        return travelerAccountRepository.save(TravelerAccount.builder().id(userId).build());
    }

    // add Toll Transactions
//    public TravelerBalance createTransaction(TransactionRequest transactionRequest) throws DatabaseException, RfidNotFoundException {
//        TravelerRfid rfid = travelerRfidRepository.findByRfid(transactionRequest.getRfid()).orElseThrow(() -> new RfidNotFoundException(""));
//        TravelerAccount user = userRepository.findById(rfid.getUserId()).orElseThrow(() -> new DatabaseException(""));
//        TravelerTransaction transaction = TravelerTransaction.builder()
//                .userId(rfid.getUserId())
//                .amount(transactionRequest.getAmount())
//                .type(transactionRequest.getType())
//                .rfid(rfid.getRfid())
//                .build();
//        try{
//
//            TravelerTransaction savedTransactions = travelerTransactionRepository.save(transaction);
//            if(savedTransactions.getType() == TransactionType.DEBIT){
//                user.setBalance(user.getBalance() - savedTransactions.getAmount());
//            }else{
//                user.setBalance(user.getBalance() + savedTransactions.getAmount());
//            }
//
//            userRepository.save(user);
//
//            return savedTransactions;
//        }catch (Exception e){
//            throw new DatabaseException(e);
//        }
//    }


}
