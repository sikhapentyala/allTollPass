package com.tcss559.alltollpass.model.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    private long id;
    private String name;
    private String email;
    private String rfid;
}
