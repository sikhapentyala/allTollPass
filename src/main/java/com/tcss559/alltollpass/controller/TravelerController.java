package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.entity.traveler.TravelerRfid;
import com.tcss559.alltollpass.entity.traveler.TravelerTransaction;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.traveler.RfidRequest;
import com.tcss559.alltollpass.model.request.traveler.TransactionRequest;
import com.tcss559.alltollpass.model.request.traveler.TravelerBalance;
import com.tcss559.alltollpass.model.response.traveler.TravelerResponse;
import com.tcss559.alltollpass.model.response.traveler.TravelerTransactionResponse;
import com.tcss559.alltollpass.service.TravelerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/traveler")
@Tag(name = "traveler-service", description = "TravelerAccount Service")
public class TravelerController {

    @Autowired
    TravelerService travelerService;

    // Traveller Balance Methods

    @PutMapping("/balance")
    @ResponseStatus(HttpStatus.CREATED)
    public TravelerBalance rechargeAccount(@RequestBody TravelerBalance travelerBalance) throws DatabaseException, RfidNotFoundException {
        return travelerService.updateTravelerAccount(travelerBalance);
    }

    @GetMapping("/balance")
    public double getBalance(@RequestHeader("user_id") Long userId) throws DatabaseException, RfidNotFoundException {
        return travelerService.getBalance(userId);
    }

    // RFID  Methods - crud
    @PostMapping("/rfid")
    @ResponseStatus(HttpStatus.CREATED)
    public TravelerResponse addRfid(@RequestBody RfidRequest rfidRequest) throws DatabaseException, UserNotFoundException {
        return travelerService.addRfid(rfidRequest);
    }

    @GetMapping("/rfid")
    public List<TravelerRfid> getAllRfid(@RequestHeader("user_id") Long userId) throws DatabaseException, UserNotFoundException {
        return travelerService.getAllRfid(userId);
    }

    @DeleteMapping("/rfid")
    public long deleteRfid(@RequestHeader("rfid") String rfid) throws DatabaseException, RfidNotFoundException {
        long deletedRecords = travelerService.deleteRfid(rfid);
        if(deletedRecords != 1){
            return deletedRecords;
        }else{
            throw new DatabaseException("Record not deleted");
        }
    }


    // Transaction

    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public TravelerTransaction makeTransaction(@RequestBody TransactionRequest transactionRequest) throws DatabaseException, RfidNotFoundException {
        return travelerService.createTransaction(transactionRequest);
    }

    @GetMapping("/reports")
    public TravelerTransactionResponse getReports(@RequestHeader("user_id") Long userId) throws DatabaseException, RfidNotFoundException {
        return travelerService.getReports(userId);
    }

    @PostMapping("/reports")
    public void sendReports(@RequestBody TransactionRequest transactionRequest) throws DatabaseException, RfidNotFoundException {
        //TODO: Get Reports on transactions
    }


}
