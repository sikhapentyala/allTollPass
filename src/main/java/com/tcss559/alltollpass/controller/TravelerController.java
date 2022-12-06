package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.model.request.traveler.DebitRequest;
import com.tcss559.alltollpass.model.request.traveler.RfidRequest;
import com.tcss559.alltollpass.model.request.traveler.TravelerBalance;
import com.tcss559.alltollpass.model.response.traveler.RfidResponse;
import com.tcss559.alltollpass.model.response.traveler.TravelerResponse;
import com.tcss559.alltollpass.model.response.traveler.TravelerTransactionResponse;
import com.tcss559.alltollpass.service.TravelerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/traveler")
@Tag(name = "traveler-service", description = "TravelerAccount Service")
public class TravelerController {

    @Autowired
    TravelerService travelerService;

    // Traveller Balance Methods

    @PutMapping("/balance/credit")
    @ResponseStatus(HttpStatus.CREATED)
    public TravelerBalance rechargeAccount(@RequestBody TravelerBalance travelerBalance) throws DatabaseException, RfidNotFoundException {
        return travelerService.creditTransaction(travelerBalance);
    }

    @PutMapping("/balance/debit")
    @ResponseStatus(HttpStatus.CREATED)
    public TravelerBalance collectToll(@RequestBody DebitRequest debitRequest) throws DatabaseException, RfidNotFoundException {
        return travelerService.debitTransaction(debitRequest);
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public double getBalance(@RequestHeader("user_id") Long userId) throws DatabaseException, RfidNotFoundException {
        return travelerService.getBalance(userId);
    }

    // RFID  Methods - crud
    @PostMapping("/rfid")
    @ResponseStatus(HttpStatus.CREATED)
    public TravelerResponse addRfid(@RequestBody RfidRequest rfidRequest) throws DatabaseException, UserNotFoundException {
        return travelerService.addRfid(rfidRequest);
    }

    @GetMapping("/rfids")
    @ResponseStatus(HttpStatus.OK)
    public TravelerResponse getAllRfid(@RequestHeader("user_id") Long userId) throws DatabaseException, UserNotFoundException {
        return travelerService.getAllRfid(userId);
    }

    @GetMapping("/rfid")
    @ResponseStatus(HttpStatus.OK)
    public RfidResponse getByRfid(@RequestHeader("rfid") String rfid) throws DatabaseException, UserNotFoundException {
        return travelerService.getByRfid(rfid);
    }

    @DeleteMapping("/rfid")
    @ResponseStatus(HttpStatus.OK)
    public TravelerResponse deleteRfid(@RequestHeader("rfid") String rfid) throws DatabaseException, RfidNotFoundException {
         return travelerService.deleteRfid(rfid);
    }

    // Reports
    @GetMapping("/reports")
    @ResponseStatus(HttpStatus.OK)
    public TravelerTransactionResponse getReports(@RequestHeader("user_id") Long userId) throws DatabaseException, RfidNotFoundException {
        return travelerService.getTransactionReports(userId);
    }

    @PostMapping("/reports")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendReports(@RequestHeader("user_id") Long userId) throws DatabaseException, RfidNotFoundException {
        travelerService.sendReportByEmail(userId);
    }


    // Transaction

//    @PostMapping("/transaction")
//    @ResponseStatus(HttpStatus.CREATED)
//    public TravelerTransaction makeTransaction(@RequestBody TransactionRequest transactionRequest) throws DatabaseException, RfidNotFoundException {
//        return travelerService.createTransaction(transactionRequest);
//    }




}
