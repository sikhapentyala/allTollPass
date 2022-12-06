package com.tcss559.alltollpass.model.request.traveler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DebitRequest {
    private Long userId;
    private String rfid;
    private double amount;
    private String tollLocation;
}
