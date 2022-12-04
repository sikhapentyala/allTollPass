package com.tcss559.alltollpass.model.response.traveler;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TravelerTransactionResponse {
    private long userId;
    private List<TransactionDetail> transactions;
}
