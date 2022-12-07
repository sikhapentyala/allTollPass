package com.tcss559.alltollpass.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.mailslurp.apis.AttachmentControllerApi;
import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.clients.ApiClient;
import com.mailslurp.clients.ApiException;
import com.mailslurp.clients.Configuration;
import com.mailslurp.models.InboxDto;
import com.mailslurp.models.SendEmailOptions;
import com.mailslurp.models.UploadAttachmentOptions;
import com.tcss559.alltollpass.config.AppConfig;
import com.tcss559.alltollpass.config.AppConstants;
import com.tcss559.alltollpass.entity.User;
import com.tcss559.alltollpass.entity.traveler.TransactionType;
import com.tcss559.alltollpass.model.external.SMSRequest;
import com.tcss559.alltollpass.model.response.traveler.TransactionDetail;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

@Service
public class ExternalService {

    @Autowired
    AppConfig appConfig;

    private static final Long TIMEOUT = 30000L;

    public void sendSMS(SMSRequest smsRequest){
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(String.format(AppConstants.SEND_SMS, appConfig.getTwilioSid())).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(appConfig.getTwilioSid(), appConfig.getTwilioToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        String body = "Your account has been %sED with amount %s. Your current balance is %s. ";
        String requestBody = String.format(body, smsRequest.getTransactionType().toString(), smsRequest.getAmount(), smsRequest.getCurrentBalance());;
        if(smsRequest.getCurrentBalance() <= 0){
            requestBody = requestBody + "Please recharge your account immediately.";
        }
        map.add("Body",requestBody);
        map.add("To",smsRequest.getNumber());
        map.add("From", appConfig.getTwilioNumber());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(uriComponents.toUriString(),
                        HttpMethod.POST,
                        entity,
                        String.class);
        System.out.println(response.getBody());
    }

    public void sendEmail(List<?> list, User user, Class<?> pojoType) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // IMPORTANT set api client timeouts
        defaultClient.setConnectTimeout(TIMEOUT.intValue());
        defaultClient.setWriteTimeout(TIMEOUT.intValue());
        defaultClient.setReadTimeout(TIMEOUT.intValue());

        // IMPORTANT set API KEY and client
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
        defaultClient.setHttpClient(httpClient);
        defaultClient.setApiKey(appConfig.getMailSlurpKey());

        InboxControllerApi inboxControllerApi = new InboxControllerApi(defaultClient);
        try{
            InboxDto inbox = inboxControllerApi.createInbox(null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            // Below Mapper will convert the POJO to csv as bytes
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(pojoType).withHeader();
            schema = schema.withColumnSeparator(',');
            ObjectWriter myObjectWriter = mapper.writer(schema);
            byte[] bytes = myObjectWriter.writeValueAsBytes(list);


            AttachmentControllerApi attachmentControllerApi = new AttachmentControllerApi();

            UploadAttachmentOptions uploadAttachmentOptions = new UploadAttachmentOptions();
            uploadAttachmentOptions.setFilename("Reports.csv");
            uploadAttachmentOptions.contentType("text/csv");
            uploadAttachmentOptions.base64Contents(Base64.getEncoder().encodeToString(bytes));
            // for legacy reasons returns array of ids regardless of how many you have added
            List<String> attachmentIds = attachmentControllerApi.uploadAttachment(uploadAttachmentOptions);

            SendEmailOptions sendEmailOptions = new SendEmailOptions()
                    .attachments(attachmentIds)
                    .to(singletonList(user.getEmail()))
                    .subject("ALLTOLLPASS - Reports")
                    .body(String.format("Hello %s, \n PFA Reports", user.getName()));
            inboxControllerApi.sendEmail(inbox.getId(), sendEmailOptions);
        }catch (ApiException e){
            System.out.println("Error while sending email to " + user.getEmail() + " : " + e.getResponseBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: Remove this method after all testing is done with external services
    public static void main(String[] args) throws IOException {

        TransactionDetail t1 = TransactionDetail.builder()
                .tollLocation("location1")
                .createdTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a\t")))
                .amount(10)
                .type(TransactionType.DEBIT)
                .rfid("sasir")
                .build();

        TransactionDetail t2 = TransactionDetail.builder()
                .tollLocation("location2")
                .createdTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a\t")))
                .amount(20)
                .type(TransactionType.CREDIT)
                .rfid("sikha")
                .build();

        List<TransactionDetail> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);

//        User user = User.builder()
//                .email("sikha@uw.edu")
//                .name("Sikha Pentyala")
//                .build();

//        sendEmail(list, user, TransactionDetail.class);

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(TransactionDetail.class).withHeader();
        schema = schema.withColumnSeparator(',');
        ObjectWriter myObjectWriter = mapper.writer(schema);
        myObjectWriter.writeValue(new File("/Users/z004tgz/Desktop/sample.csv"), list);
//        byte[] bytes = myObjectWriter.writeValueAsBytes(list);
    }



}
