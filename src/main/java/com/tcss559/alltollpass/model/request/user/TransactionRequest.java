package com.tcss559.alltollpass.model.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcss559.alltollpass.entity.user.TransactionType;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRequest {
    private long userId;
    private String rfid;
    private double amount;
    private TransactionType type;
}
