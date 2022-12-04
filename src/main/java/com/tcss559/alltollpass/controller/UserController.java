package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.entity.traveler.TravelerAccount;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.LoginRequest;
import com.tcss559.alltollpass.model.request.UserRequest;
import com.tcss559.alltollpass.model.response.LoginResponse;
import com.tcss559.alltollpass.model.response.UserResponse;
import com.tcss559.alltollpass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserResponse createUser(@RequestBody UserRequest userRequest){

        return userService.createUser(userRequest);

    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest){

        return userService.validateCredentials(loginRequest);

    }


}
