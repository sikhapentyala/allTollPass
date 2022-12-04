package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.entity.user.TransactionType;
import com.tcss559.alltollpass.entity.user.User;
import com.tcss559.alltollpass.entity.user.UserRfid;
import com.tcss559.alltollpass.entity.user.UserTransactions;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.user.RfidRequest;
import com.tcss559.alltollpass.model.request.user.TransactionRequest;
import com.tcss559.alltollpass.model.request.user.UserRequest;
import com.tcss559.alltollpass.model.response.user.TransactionDetail;
import com.tcss559.alltollpass.model.response.user.UserResponse;
import com.tcss559.alltollpass.model.response.user.UserTransactionResponse;
import com.tcss559.alltollpass.repository.UserRepository;
import com.tcss559.alltollpass.repository.UserRfidRepository;
import com.tcss559.alltollpass.repository.UserTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRfidRepository userRfidRepository;

    @Autowired
    UserTransactionsRepository userTransactionsRepository;


    public User getUserByName(String name) throws UserNotFoundException {
        Optional<User> user = userRepository.findByName(name);
        if(user.isPresent()){
            return user.get();
        }else{
           throw new UserNotFoundException("No User found");
        }
    }

    public User getUserByEmail(String email) throws UserNotFoundException{
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new UserNotFoundException("No User found");
        }
    }

    public User getUserById(Long id) throws UserNotFoundException{
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new UserNotFoundException("No User found");
        }
    }

    public User createUser(UserRequest userRequest) throws DatabaseException {
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
        try{
            return userRepository.save(user);
        }catch (Exception e){
            throw new DatabaseException(e);
        }

    }

    public UserResponse addRfid(RfidRequest rfidRequest) throws DatabaseException, UserNotFoundException {
        User user = userRepository.findById(rfidRequest.getUserId()).orElseThrow(() -> new UserNotFoundException(""));
        Set<UserRfid> userRfidList = new HashSet<>();
                rfidRequest.getRfids().forEach(rfid ->
                        userRfidList.add(
                                UserRfid.builder()
                                        .userId(rfidRequest.getUserId())
                                        .rfid(rfid.getRfid())
                                        .vehicleType(rfid.getVehicleType())
                                        .build()
                        )

        );
        try{
            List<UserRfid> list = userRfidRepository.saveAll(userRfidList);
            return UserResponse.builder()
                    .userId(user.getId())
                    .rfids(list)
                    .balance(user.getBalance())
                    .email(user.getEmail())
                    .balance(user.getBalance())
                    .name(user.getName())
                    .build();

        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    public UserTransactions createTransaction(TransactionRequest transactionRequest) throws DatabaseException, RfidNotFoundException {
        UserRfid rfid = userRfidRepository.findByRfid(transactionRequest.getRfid()).orElseThrow(() -> new RfidNotFoundException(""));
        User user = userRepository.findById(rfid.getUserId()).orElseThrow(() -> new DatabaseException(""));
        UserTransactions transaction = UserTransactions.builder()
                .userId(rfid.getUserId())
                .amount(transactionRequest.getAmount())
                .type(transactionRequest.getType())
                .rfid(rfid.getRfid())
                .build();
        try{

            UserTransactions savedTransactions = userTransactionsRepository.save(transaction);
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

    public List<UserRfid> getAllRfid(Long userId) {
        return userRfidRepository.findByUserId(userId);
    }

    public double getBalance(Long userId) {
        return userRepository.findById(userId).orElseThrow().getBalance();
    }

    public UserTransactionResponse getReports(Long userId) {
        try {
            List<UserTransactions> transactions = userTransactionsRepository.findByUserId(userId);
            return UserTransactionResponse.builder()
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

    public long deleteRfid(String rfid) throws DatabaseException {
        try{
            return userRfidRepository.deleteByRfid(rfid);

        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }
}
