package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.entity.traveler.TravelerAccount;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.LoginRequest;
import com.tcss559.alltollpass.model.request.UserRequest;
import com.tcss559.alltollpass.model.response.LoginResponse;
import com.tcss559.alltollpass.model.response.UserResponse;
import com.tcss559.alltollpass.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author sikha
 * UserController defines the service endpoints (API URIs) for registeration and login services of the user entities in
 * the AllTollPass System
 */

@RestController
@RequestMapping("/user")
@Tag(name = "user-service", description = "UserAccounts Service : service endpoints (API URIs) for registeration and " +
        "login services of the user entities in the AllTollPass System")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * This API can be used to create user
     * @param userRequest - JSON entity
     * @return
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    //@Tag(name = "createUser", description = "Registers a user in the AllTollPass,  creates a login and assigns a id ")
    public UserResponse createUser(@RequestBody UserRequest userRequest){
        // inserts in User table and creates and inserts in corresponding table
        return userService.createUser(userRequest);

    }

    /**
     *
     * @param loginRequest - JSON entity
     * @return
     */
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    //@Tag(name = "authenticateUser", description = "Validates if the logging user is valid  ")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest){
        // just checks if username + password exists
        return userService.validateCredentials(loginRequest);

    }


}
