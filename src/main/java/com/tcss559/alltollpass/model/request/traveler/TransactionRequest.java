package com.tcss559.alltollpass.model.request.traveler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcss559.alltollpass.entity.traveler.TransactionType;
import lombok.Data;

@Data
public class TransactionRequest {
    private String rfid;
    private double amount;
    private TransactionType type;
}
