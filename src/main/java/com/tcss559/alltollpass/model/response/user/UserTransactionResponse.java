package com.tcss559.alltollpass.model.response.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserTransactionResponse {
    private long userId;
    private List<TransactionDetail> transactions;
}
