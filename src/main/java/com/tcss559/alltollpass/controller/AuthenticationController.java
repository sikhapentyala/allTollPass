package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.entity.Authentication;
import com.tcss559.alltollpass.model.request.authentication.AuthRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @PostMapping()
    public void createUser(Authentication authentication){

    }

    @GetMapping()
    public void validateLogin(@RequestHeader MultiValueMap<String, String> headers){

        String userName = headers.get("user_name").get(0);
    }


}
