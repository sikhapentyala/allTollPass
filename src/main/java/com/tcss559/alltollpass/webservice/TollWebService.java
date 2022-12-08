package com.tcss559.alltollpass.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Collections;

/**
 * @author sikha
 * This is SOAP based service - the main service of our project
 * This collects Toll from user when a toll agency does not identify the RFID
 * This uses service composition to achive above functionality
 */

@Endpoint
public class TollWebService {

    @Autowired
    AppConfig appConfig;

    // main method that calls services - input is agencyId, rfid, and transactionId at toll agency
    //http://localhost:8080/ws/toll.wsdl
    @PayloadRoot(namespace = "http://com/tcss559/alltollpass", localPart = "TollRequest")
    @ResponsePayload
    public TollResponse intiateTransaction(@RequestPayload TollRequest request){
        double amountForThisTransaction = 0.0;
        try {
            // Get the vehicle Type from user/traveller based on RFID
            RfidResponse rfidResponse = callServiceToGetVehicleType(request.getRfid());
            // Get the amount to be deducted/ toll rate from Agency based on vehicle type
            TollRateDetail tollRateDetail = callServiceToGetTollRateByVehicleType(request.getAgencyId(), rfidResponse.getVehicleType());
            // Collect Toll - deduct balance from users/traveler's account
            TravelerBalance travelerBalance = callServiceToUpdateUserBalance(rfidResponse, tollRateDetail);

            TollTransactionRequest.TollTransactionRequestBuilder transactionBuilder = TollTransactionRequest.builder()
                    .tollTransactionId(request.getTollTransactionId())
                    .rfid(request.getRfid())
                    .amount(tollRateDetail.getTollRate())
                    .agencyId(request.getAgencyId());

            amountForThisTransaction = tollRateDetail.getTollRate();
            if(travelerBalance.getAmount() >= 0){
                transactionBuilder.status(TransactionStatus.SUCCESS);
            } else if (travelerBalance.getAmount() < 0) {
                transactionBuilder.status(TransactionStatus.IN_PROCESS);
            }

            TollResponse response = new TollResponse();
            // insert the transaction into AllTollPass transactions
            response.setStatus(createTollTransaction(transactionBuilder.build()).toString());

            return response;

        }catch (Exception e){

            // IF there is any excpetion we inform the Agency to use fallback system to collect toll
            TollTransactionRequest failedTransaction = TollTransactionRequest.builder()
                    .tollTransactionId(request.getTollTransactionId())
                    .status(TransactionStatus.FALLBACK)
                    .rfid(request.getRfid())
                    .amount(amountForThisTransaction)
                    .agencyId(request.getAgencyId())
                    .build();


            TollResponse response = new TollResponse();
            //calling web service
            response.setStatus(createTollTransaction(failedTransaction).toString());

            return response;
        }

    }

    // This method is used for service composition as service 1
    public RfidResponse callServiceToGetVehicleType(String rfid){
        RestTemplate template = new RestTemplateBuilder()
                .messageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)))
                .build();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(appConfig.getServiceUrl()+ AppConstants.GET_VEHICLE_TYPE_BY_RFID).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
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


    // This method is used for service composition as service 2
    public TollRateDetail callServiceToGetTollRateByVehicleType(Long agencyId, VehicleType vehicleType){
        RestTemplate template = new RestTemplateBuilder()
                .messageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)))
                .build();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(appConfig.getServiceUrl()+ AppConstants.GET_TOLL_RATE_BY_AGENCY).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
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


    // This method is used for service composition as service 3
    public TravelerBalance callServiceToUpdateUserBalance(RfidResponse rfidResponse, TollRateDetail tollRateDetail){
        RestTemplate template = new RestTemplateBuilder()
                .messageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)))
                .build();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(appConfig.getServiceUrl()+ AppConstants.DEDUCT_AMOUNT).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<DebitRequest> entity = new HttpEntity<>(DebitRequest.builder()
                .userId(rfidResponse.getUserId())
                .tollLocation(tollRateDetail.getLocation())
                .rfid(rfidResponse.getRfid())
                .amount(tollRateDetail.getTollRate())
                .build(),
                headers
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

    //TODO : Check
    public TransactionStatus createTollTransaction(TollTransactionRequest request){
        RestTemplate template = new RestTemplateBuilder()
                .messageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)))
                .build();

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(appConfig.getServiceUrl()+ AppConstants.CREATE_TRANSACTION).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<TollTransactionRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<TransactionStatus> response = template.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, TransactionStatus.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            } else {
                throw new RuntimeException("Create transaction service threw " + response.getStatusCode().getReasonPhrase());
//                return TransactionStatus.FALLBACK;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
//            return TransactionStatus.FALLBACK;
        }
    }

    public static void main(String[] args) {
        RestTemplate template = new RestTemplateBuilder()
                .messageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)))
                .build();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:8080" + AppConstants.DEDUCT_AMOUNT).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<DebitRequest> entity = new HttpEntity<>(DebitRequest.builder()
                .userId(1L)
                .tollLocation("Florida")
                .rfid("rfid3")
                .amount(10)
                .build(),
                headers
        );

        try {
            ResponseEntity<TravelerBalance> response = template.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, TravelerBalance.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println(response.getBody());
            } else {
                throw new RuntimeException("Update balance service threw " + response.getStatusCode().getReasonPhrase());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
