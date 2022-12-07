package com.tcss559.alltollpass.model.external;

import com.tcss559.alltollpass.entity.traveler.TransactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SMSRequest {
    private String number;
    private TransactionType transactionType;
    private double amount;
    private double currentBalance;
}
