package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.entity.user.User;
import com.tcss559.alltollpass.entity.user.UserRfid;
import com.tcss559.alltollpass.entity.user.UserTransactions;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.user.RfidRequest;
import com.tcss559.alltollpass.model.request.user.TransactionRequest;
import com.tcss559.alltollpass.model.request.user.UserRequest;
import com.tcss559.alltollpass.model.response.user.UserResponse;
import com.tcss559.alltollpass.model.response.user.UserTransactionResponse;
import com.tcss559.alltollpass.service.AuthenticationService;
import com.tcss559.alltollpass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "user-service", description = "User Service")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByName(@PathVariable(name = "name") String name) throws UserNotFoundException {
        return userService.getUserByName(name);
    }

    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByEmail(@PathVariable(name = "email") String email) throws UserNotFoundException {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByid(@PathVariable(name = "id") long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody UserRequest userRequest) throws DatabaseException {
        return userService.createUser(userRequest);
    }

    @PutMapping("/rfid")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addRfid(@RequestBody RfidRequest rfidRequest) throws DatabaseException, UserNotFoundException {
        return userService.addRfid(rfidRequest);
    }

    @GetMapping("/rfid")
    public List<UserRfid> getAllRfid(@RequestHeader("user_id") Long userId) throws DatabaseException, UserNotFoundException {
        return userService.getAllRfid(userId);
    }

    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public UserTransactions makeTransaction(@RequestBody TransactionRequest transactionRequest) throws DatabaseException, RfidNotFoundException {
        return userService.createTransaction(transactionRequest);
    }

    @PutMapping("/recharge")
    @ResponseStatus(HttpStatus.CREATED)
    public UserTransactions rechargeAccount(@RequestBody TransactionRequest transactionRequest) throws DatabaseException, RfidNotFoundException {
        return userService.createTransaction(transactionRequest);
    }

    @GetMapping("/balance")
    public double getBalance(@RequestHeader("user_id") Long userId) throws DatabaseException, RfidNotFoundException {
        return userService.getBalance(userId);
    }

    @GetMapping("/credentials")
    public void getCreds(@RequestBody TransactionRequest transactionRequest) throws DatabaseException, RfidNotFoundException {
        //TODO: Get Creds
    }

    @GetMapping("/reports")
    public UserTransactionResponse getReports(@RequestHeader("user_id") Long userId) throws DatabaseException, RfidNotFoundException {
        return userService.getReports(userId);
    }

    @PostMapping("/reports")
    public void sendReports(@RequestBody TransactionRequest transactionRequest) throws DatabaseException, RfidNotFoundException {
        //TODO: Get Reports on transactions
    }

    @DeleteMapping("/rfid")
    public long deleteRfid(@RequestHeader("rfid") String rfid) throws DatabaseException, RfidNotFoundException {
        long deletedRecords = userService.deleteRfid(rfid);
        if(deletedRecords != 1){
            return deletedRecords;
        }else{
            throw new DatabaseException("Record not deleted");
        }
    }
}
