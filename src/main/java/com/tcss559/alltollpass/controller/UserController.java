package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.entity.User;
import com.tcss559.alltollpass.entity.traveler.TravelerAccount;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {


    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public TravelerAccount getUserByName(@PathVariable(name = "name") String name) throws UserNotFoundException {
        return travelerService.getUserByName(name);
    }

    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public TravelerAccount getUserByEmail(@PathVariable(name = "email") String email) throws UserNotFoundException {
        return travelerService.getUserByEmail(email);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TravelerAccount getUserByid(@PathVariable(name = "id") long id) throws UserNotFoundException {
        return travelerService.getUserById(id);
    }

    @PostMapping()
    public void createUser(User authentication){

    }

    @GetMapping()
    public void validateLogin(@RequestHeader MultiValueMap<String, String> headers){

        String userName = headers.get("user_name").get(0);
    }


}
