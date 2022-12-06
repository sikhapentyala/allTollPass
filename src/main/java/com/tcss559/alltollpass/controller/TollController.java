package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.entity.toll.TransactionStatus;
import com.tcss559.alltollpass.model.request.toll.LocationRequest;
import com.tcss559.alltollpass.model.request.toll.TollRateRequest;
import com.tcss559.alltollpass.model.request.toll.TollTransactionRequest;
import com.tcss559.alltollpass.model.response.toll.LocationResponse;
import com.tcss559.alltollpass.model.response.toll.TollRateResponse;
import com.tcss559.alltollpass.model.response.toll.TransactionResponse;
import com.tcss559.alltollpass.service.TollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/toll")
public class TollController {

    @Autowired
    TollService tollService;


    @RequestMapping(value="", method = RequestMethod.OPTIONS)
    ResponseEntity<?> tollOptions()
    {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET,
                        HttpMethod.PATCH,
                        HttpMethod.PUT,
                        HttpMethod.DELETE,
                        HttpMethod.OPTIONS,
                        HttpMethod.HEAD
                )
                .build();
    }

    @RequestMapping(value = "/status", method = RequestMethod.HEAD)
    public ResponseEntity<?> getStatusInHeader(@RequestHeader(value = "transaction_id") String transactionId, @RequestHeader(value = "agency_id") Long agencyId){
        TransactionResponse response = tollService.getTransactionStatusByAgencyId(transactionId, agencyId);
        return ResponseEntity.status(HttpStatus.OK).header("status", response.getStatus().toString()).build();
    }

    @PatchMapping("/location")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LocationResponse updateLocation(LocationRequest locationRequest){
        return tollService.updateLocation(locationRequest);
    }

    @PutMapping("/rates")
    @ResponseStatus(HttpStatus.CREATED)
    public TollRateResponse upsertTollRates(TollRateRequest tollRateRequest){
        return tollService.upsertTollRates(tollRateRequest);
    }

    @GetMapping("/rates")
    @ResponseStatus(HttpStatus.OK)
    public TollRateResponse getTollRates(@RequestHeader(value = "agency_id") Long agencyId){
        return tollService.getTollRates(agencyId);
    }

    @DeleteMapping("/rates")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TollRateResponse deleteTollRates(TollRateRequest tollRateRequest){
        return tollService.deleteTollRate(tollRateRequest);
    }

    // Delete Single rate as an enhancement

    @GetMapping("/reports")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> getTransactionReportForAgency(@RequestHeader(value = "agency_id") Long agencyId){
        return tollService.getTransactionReportForAgency(agencyId);
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse getTransactionStatus(@RequestHeader(value = "transaction_id") String transactionId, @RequestHeader(value = "agency_id") Long agencyId){
        return tollService.getTransactionStatusByAgencyId(transactionId, agencyId);
    }


    @PutMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionStatus createTransanction(TollTransactionRequest request){
        return tollService.createTollTransaction(request);
    }

    // TODO:  write and endpoint for view


}
