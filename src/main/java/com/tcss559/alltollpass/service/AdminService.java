package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.config.AppConfig;
import com.tcss559.alltollpass.entity.Role;
import com.tcss559.alltollpass.entity.User;
import com.tcss559.alltollpass.entity.toll.TollTransaction;
import com.tcss559.alltollpass.entity.toll.TransactionStatus;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.UserRequest;
import com.tcss559.alltollpass.model.response.UserResponse;
import com.tcss559.alltollpass.model.response.UserResponseXML;
import com.tcss559.alltollpass.model.response.toll.TransactionResponse;
import com.tcss559.alltollpass.repository.UserRepository;
import com.tcss559.alltollpass.repository.toll.AgencyRepository;
import com.tcss559.alltollpass.repository.toll.TollRateRepository;
import com.tcss559.alltollpass.repository.toll.TollTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    AppConfig appConfig;

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    TollRateRepository tollRateRepository;

    @Autowired
    TollTransactionRepository tollTransactionRepository;

    @Autowired
    UserRepository userRepository;

    public void deleteUser(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        try{

            user.setActive(false);
            userRepository.save(user);
            return;

        }catch (Exception e){
            throw new DatabaseException(e);
        }


    }

    public List<TransactionResponse> getTransactionsByStatus(String status) throws RuntimeException {

        TransactionStatus.valueOf(status);
        List<TollTransaction> transactions = tollTransactionRepository.findByStatus(TransactionStatus.valueOf(status));
        return transactions.stream().map(it ->
                        TransactionResponse.builder()
                                .tollTransactionId(it.getTollTransactionId())
                                .status(it.getStatus())
                                .rfid(it.getRfid())
                                .createTimestamp(it.getCreateTimestamp())
                                .updateTimestamp(it.getUpdateTimestamp())
                                .agencyId(it.getAgencyId())
                                .build())
                .collect(Collectors.toList());
    }

    public UserResponseXML getUserDetailsByUserName(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        try{
            return UserResponseXML.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .name(user.getName())
                    .build();
        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }
}
