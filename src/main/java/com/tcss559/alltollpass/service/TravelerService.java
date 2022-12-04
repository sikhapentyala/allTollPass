package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.entity.User;
import com.tcss559.alltollpass.entity.traveler.TransactionType;
import com.tcss559.alltollpass.entity.traveler.TravelerAccount;
import com.tcss559.alltollpass.entity.traveler.TravelerRfid;
import com.tcss559.alltollpass.entity.traveler.TravelerTransaction;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.traveler.RfidRequest;
import com.tcss559.alltollpass.model.request.traveler.TransactionRequest;
import com.tcss559.alltollpass.model.request.traveler.TravelerBalance;
import com.tcss559.alltollpass.model.request.traveler.TravelerRequest;
import com.tcss559.alltollpass.model.response.traveler.TransactionDetail;
import com.tcss559.alltollpass.model.response.traveler.TravelerResponse;
import com.tcss559.alltollpass.model.response.traveler.TravelerTransactionResponse;
import com.tcss559.alltollpass.repository.UserRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerAccountRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerRfidRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

        User user =userRepository.findById(rfidRequest.getUserId()).orElseThrow(() -> new UserNotFoundException(""));

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
                    .rfids(list)
                    .build();

        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    public List<TravelerRfid> getAllRfid(Long userId) {
        return travelerRfidRepository.findByUserId(userId);
    }

    public long deleteRfid(String rfid) throws DatabaseException {
        try{
            return travelerRfidRepository.deleteByRfid(rfid);

        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    // Transactions




    public TravelerBalance createTransaction(TransactionRequest ) throws DatabaseException, RfidNotFoundException {
        TravelerRfid rfid = travelerRfidRepository.findByRfid(transactionRequest.getRfid()).orElseThrow(() -> new RfidNotFoundException(""));
        TravelerAccount user = userRepository.findById(rfid.getUserId()).orElseThrow(() -> new DatabaseException(""));
        TravelerTransaction transaction = TravelerTransaction.builder()
                .userId(rfid.getUserId())
                .amount(transactionRequest.getAmount())
                .type(transactionRequest.getType())
                .rfid(rfid.getRfid())
                .build();
        try{

            TravelerTransaction savedTransactions = travelerTransactionRepository.save(transaction);
            if(savedTransactions.getType() == TransactionType.DEBIT){
                user.setBalance(user.getBalance() - savedTransactions.getAmount());
            }else{
                user.setBalance(user.getBalance() + savedTransactions.getAmount());
            }

            userRepository.save(user);

            return savedTransactions;
        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }




    public TravelerAccount getUserByName(String name) throws UserNotFoundException {
        Optional<TravelerAccount> user = userRepository.findByName(name);
        if(user.isPresent()){
            return user.get();
        }else{
           throw new UserNotFoundException("No TravelerAccount found");
        }
    }

    public TravelerAccount getUserByEmail(String email) throws UserNotFoundException{
        Optional<TravelerAccount> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new UserNotFoundException("No TravelerAccount found");
        }
    }

    public TravelerAccount getUserById(Long id) throws UserNotFoundException{
        Optional<TravelerAccount> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new UserNotFoundException("No TravelerAccount found");
        }
    }

    public TravelerAccount createUser(TravelerRequest userRequest) throws DatabaseException {
        TravelerAccount user = TravelerAccount.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
        try{
            return userRepository.save(user);
        }catch (Exception e){
            throw new DatabaseException(e);
        }

    }



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
                                            .createdAt(it.getTimestamp())
                                            .build()
                            )
                            .collect(Collectors.toList())
                    )
                    .build();
        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }


}
