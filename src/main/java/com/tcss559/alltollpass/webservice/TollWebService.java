package com.tcss559.alltollpass.webservice;

import com.tcss559.alltollpass.TollRequest;
import com.tcss559.alltollpass.service.TollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class TollWebService {

    @Autowired
    TollService tollService;

    @PayloadRoot(namespace = "http://in28minutes.com/students", localPart = "GetStudentDetailsRequest")
    @ResponsePayload
    public void intiateTransaction(@RequestPayload TollRequest request){

        tollService.makeTransaction(request);

    }
}
