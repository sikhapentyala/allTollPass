package com.tcss559.alltollpass.model.request.traveler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class TravelerRequest {
    private long id;
    private String name;
    private String email;
    private String rfid;
}
