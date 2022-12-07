package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.response.UserResponseXML;
import com.tcss559.alltollpass.model.response.toll.TransactionResponse;
import com.tcss559.alltollpass.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "admin-service", description = "AllTollPass Admin Service : A few services for the Administrator at AllTollPass")

public class AdminController {

    @Autowired
    AdminService adminService;

    // Make a user inactive
    @PatchMapping("/user/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) throws DatabaseException, UserNotFoundException {
        adminService.deleteUser(username);
    }

    // Get transactions for a given status
    @GetMapping("/reports/{status}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> getReports(@PathVariable String status) throws DatabaseException {
        return adminService.getTransactionsByStatus(status);
    }

    // Get user details given username
    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseXML> getUserDetailsByUserName(@PathVariable String username) throws DatabaseException {
        return ResponseEntity
                .ok()
//                .header(HttpHeaders.ACCEPT, "text/xml")
                .header(HttpHeaders.CONTENT_TYPE, "text/xml")
                .body(adminService.getUserDetailsByUserName(username));
    }

}