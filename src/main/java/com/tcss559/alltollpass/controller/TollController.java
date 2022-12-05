package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.model.request.toll.LocationRequest;
import com.tcss559.alltollpass.model.request.toll.TollRateRequest;
import com.tcss559.alltollpass.model.response.toll.LocationResponse;
import com.tcss559.alltollpass.service.TollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/toll")
public class TollController {

    @Autowired
    TollService tollService;

    @PatchMapping("/location")
    public LocationResponse updateLocation(LocationRequest locationRequest){
        return tollService.updateLocation(locationRequest);
    }

    @PostMapping("/rates")
    public void upsertTollRates(TollRateRequest tollRateRequest){
        tollService.upsertTollRates(tollRateRequest);
    }

    @GetMapping()
    public void getAllTolls(){

    }

    @GetMapping("/status")
    public void getTransactionStatus(@RequestHeader(value = "transaction_id") String transactionId){

    }

}
