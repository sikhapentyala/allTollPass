package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.entity.toll.TransactionStatus;
import com.tcss559.alltollpass.model.request.toll.LocationRequest;
import com.tcss559.alltollpass.model.request.toll.TollRateRequest;
import com.tcss559.alltollpass.model.request.toll.TollTransactionRequest;
import com.tcss559.alltollpass.model.response.toll.LocationResponse;
import com.tcss559.alltollpass.model.response.toll.TollRateResponse;
import com.tcss559.alltollpass.model.response.toll.TransactionResponse;
import com.tcss559.alltollpass.service.TollService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sikha
 * TollController defines the service endpoints (API URIs) to provide services for Toll Agency
 */

@RestController
@RequestMapping("/toll")
@Tag(name = "toll-service", description = "TollAgency Service : the service endpoints (API URIs) to provide services " +
        "for Toll Agency")
public class TollController {

    @Autowired
    TollService tollService;

    /**
     * A webAPI to check what HTTP methods does this web service use : can be used by developers of Toll Agency
     * @return
     */
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

    /**
     * This API provides API to get the status of a given transaction
     * of a toll agency that is processed by AllTollPass in the header.
     * @param transactionId - Header param defining transactionId maintained at toll agency
     * @param agencyId - Header param defining the toll agency id
     * @return in the header param
     */
    @RequestMapping(value = "/status", method = RequestMethod.HEAD)
    public ResponseEntity<?> getStatusInHeader(@RequestHeader(value = "transaction_id") String transactionId, @RequestHeader(value = "agency_id") Long agencyId){
        TransactionResponse response = tollService.getTransactionStatusByAgencyId(transactionId, agencyId);
        return ResponseEntity.status(HttpStatus.OK).header("status", response.getStatus().toString()).build();
    }

    /**
     * This API is used to update the location of the toll agency
     * @param locationRequest
     * @return
     */
    @PatchMapping("/location")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LocationResponse updateLocation(LocationRequest locationRequest){
        return tollService.updateLocation(locationRequest);
    }

    /**
     * This API is used to insert or update toll rates for a given toll agency
     * @param tollRateRequest
     * @return
     */
    @PutMapping("/rates")
    @ResponseStatus(HttpStatus.CREATED)
    public TollRateResponse upsertTollRates(TollRateRequest tollRateRequest){
        return tollService.upsertTollRates(tollRateRequest);
    }

    /**
     * This API is used to get the rates of toll agency
     * @param agencyId - header param
     * @return
     */
    @GetMapping("/rates")
    @ResponseStatus(HttpStatus.OK)
    public TollRateResponse getTollRates(@RequestHeader(value = "agency_id") Long agencyId){
        return tollService.getTollRates(agencyId);
    }

    /**
     * This API is used to get the rates of toll agency
     * @param tollRateRequest - JSON file
     * @return
     */
    @DeleteMapping("/rates")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TollRateResponse deleteTollRates(TollRateRequest tollRateRequest){
        return tollService.deleteTollRate(tollRateRequest);
    }


    /**
     * This API returns the reports for toll agency the transactions processed at AllTollPass - in JSON
     * @param agencyId
     * @return
     */
    @GetMapping("/reports")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> getTransactionReportForAgency(@RequestHeader(value = "agency_id") Long agencyId){
        return tollService.getTransactionReportForAgency(agencyId);
    }

    /**
     * This API provides API to get the status of a given transaction
     * of a toll agency that is processed by AllTollPass in the header.
     * @param transactionId - Header param defining transactionId maintained at toll agency
     * @param agencyId - Header param defining the toll agency id
     * @return
     */
    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse getTransactionStatus(@RequestHeader(value = "transaction_id") String transactionId, @RequestHeader(value = "agency_id") Long agencyId){
        return tollService.getTransactionStatusByAgencyId(transactionId, agencyId);
    }

    /**
     * This API can be used to insert a transaction maintained by AllTollPass
     * @param request
     * @return
     */
    // TODO : We are using in SOAP?
    @PutMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionStatus createTransanction(TollTransactionRequest request){
        return tollService.createTollTransaction(request);
    }

    // TODO:  write and endpoint for view


}
