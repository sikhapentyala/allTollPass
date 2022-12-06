package com.tcss559.alltollpass.webservice;

import com.tcss559.alltollpass.TollRequest;
import com.tcss559.alltollpass.TollResponse;
import com.tcss559.alltollpass.config.AppConfig;
import com.tcss559.alltollpass.config.AppConstants;
import com.tcss559.alltollpass.entity.toll.TransactionStatus;
import com.tcss559.alltollpass.entity.traveler.VehicleType;
import com.tcss559.alltollpass.exception.TollNotFoundException;
import com.tcss559.alltollpass.model.request.toll.TollTransactionRequest;
import com.tcss559.alltollpass.model.request.traveler.DebitRequest;
import com.tcss559.alltollpass.model.request.traveler.TravelerBalance;
import com.tcss559.alltollpass.model.response.toll.TollRateDetail;
import com.tcss559.alltollpass.model.response.toll.TollRateResponse;
import com.tcss559.alltollpass.model.response.traveler.RfidResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class TollWebService {

    @Autowired
    AppConfig appConfig;

    @PayloadRoot(namespace = "http://localhost:8080/toll", localPart = "TollRequest")
    @ResponsePayload
    public TollResponse intiateTransaction(@RequestPayload TollRequest request){
        try {
            RfidResponse rfidResponse = callServiceToGetVehicleType(request.getRfid());
            TollRateDetail tollRateDetail = callServiceToGetTollRateByVehicleType(request.getAgencyId(), rfidResponse.getVehicleType());
            TravelerBalance travelerBalance = callServiceToUpdateUserBalance(rfidResponse, tollRateDetail);

            TollTransactionRequest.TollTransactionRequestBuilder transactionBuilder = TollTransactionRequest.builder()
                    .tollTransactionId(request.getTollTransactionId())
                    .rfid(request.getRfid())
                    .agencyId(request.getAgencyId());


            if(travelerBalance.getAmount() >= 0){
                transactionBuilder.status(TransactionStatus.SUCCESS);
            } else if (travelerBalance.getAmount() < 0) {
                transactionBuilder.status(TransactionStatus.IN_PROCESS);
            }

            TollResponse response = new TollResponse();
            response.setStatus(createTollTransaction(transactionBuilder.build()).toString());

            return response;

        }catch (Exception e){

            //
            TollTransactionRequest failedTransaction = TollTransactionRequest.builder()
                    .tollTransactionId(request.getTollTransactionId())
                    .status(TransactionStatus.FALLBACK)
                    .rfid(request.getRfid())
                    .agencyId(request.getAgencyId())
                    .build();


            TollResponse response = new TollResponse();
            response.setStatus(createTollTransaction(failedTransaction).toString());

            return response;
        }

    }

    public RfidResponse callServiceToGetVehicleType(String rfid){
        RestTemplate template = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(appConfig.getServiceUrl()+ AppConstants.GET_VEHICLE_TYPE_BY_RFID).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("rfid", rfid);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<RfidResponse> response = template.exchange(uriComponents.toUri(), HttpMethod.GET, entity, RfidResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("callServiceToGetVehicleType service threw " + response.getStatusCode().getReasonPhrase());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    // This method is used for service composition as service 1
    public TollRateDetail callServiceToGetTollRateByVehicleType(Long agencyId, VehicleType vehicleType){
        RestTemplate template = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(appConfig.getServiceUrl()+ AppConstants.GET_TOLL_RATE_BY_AGENCY).build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("agency_id", String.valueOf(agencyId));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TollRateResponse> response = template.exchange(uriComponents.toUri(), HttpMethod.GET, entity, TollRateResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody().getRates().stream()
                        .filter(it -> it.getVehicleType() == vehicleType)
                        .findAny().orElseThrow(() -> new TollNotFoundException("Rate does not exist for given vehicle: " + vehicleType));

            } else {
                throw new RuntimeException("Get rate by vehicle type service threw " + response.getStatusCode().getReasonPhrase());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    // This method is used for service composition as service 2
    public TravelerBalance callServiceToUpdateUserBalance(RfidResponse rfidResponse, TollRateDetail tollRateDetail){
        RestTemplate template = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(appConfig.getServiceUrl()+ AppConstants.DEDUCT_AMOUNT).build();

        HttpEntity<DebitRequest> entity = new HttpEntity<>(DebitRequest.builder()
                .userId(rfidResponse.getUserId())
                .tollLocation(tollRateDetail.getLocation())
                .rfid(rfidResponse.getRfid())
                .amount(tollRateDetail.getTollRate())
                .build()
        );

        try {
            ResponseEntity<TravelerBalance> response = template.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, TravelerBalance.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            } else {
                throw new RuntimeException("Update balance service threw " + response.getStatusCode().getReasonPhrase());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public TransactionStatus createTollTransaction(TollTransactionRequest request){
        RestTemplate template = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(appConfig.getServiceUrl()+ AppConstants.DEDUCT_AMOUNT).build();

        HttpEntity<TollTransactionRequest> entity = new HttpEntity<>(request);

        try {
            ResponseEntity<TransactionStatus> response = template.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, TransactionStatus.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            } else {
                throw new RuntimeException("Update balance service threw " + response.getStatusCode().getReasonPhrase());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



}
