package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.config.AppConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExternalService {

    public void sendSMS(){
        //TODO: send sms to user on mobile
        //user.getMobile();
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(AppConstants.SEND_SMS).build();
    }

    public void sendEmail() {
    }
}
