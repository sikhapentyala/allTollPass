package com.tcss559.alltollpass.model.response.user;

import com.tcss559.alltollpass.entity.user.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDetail {
    private String rfid;
    private TransactionType type;
    private double amount;
    private LocalDateTime createdAt;
}
