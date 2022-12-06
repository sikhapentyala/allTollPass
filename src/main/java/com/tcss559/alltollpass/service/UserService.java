package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.entity.Role;
import com.tcss559.alltollpass.entity.User;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.LoginRequest;
import com.tcss559.alltollpass.model.request.UserRequest;
import com.tcss559.alltollpass.model.response.LoginResponse;
import com.tcss559.alltollpass.model.response.UserResponse;
import com.tcss559.alltollpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TravelerService travelerService;

    public UserResponse createUser(UserRequest userRequest) throws DatabaseException {
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .mobile(userRequest.getMobile())
                .name(userRequest.getName())
                .role(userRequest.getRole())
                .email(userRequest.getEmail())
                .build();

        try{
            User savedUser = userRepository.save(user);
            if (savedUser.getRole() == Role.TRAVELER){
                travelerService.createTravelerAccount(savedUser.getId());
            }
            return UserResponse.builder()
                    .id(savedUser.getId())
                    .username(savedUser.getUsername())
                    .password(savedUser.getPassword())
                    .email(savedUser.getEmail())
                    .role(savedUser.getRole())
                    .name(savedUser.getName())
                    .build();
        }catch (Exception e){
            throw new DatabaseException(e);
        }

    }

    public LoginResponse validateCredentials(LoginRequest loginRequest) {

        //TODO: check if user isActive findByUsernameAndPasswordAndRoleAndIsActive
        User user = userRepository.findByUsernameAndPasswordAndRole(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getRole())
                .orElseThrow(() -> new UserNotFoundException("No Such User Exists"));
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
