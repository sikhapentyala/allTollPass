package com.tcss559.alltollpass.model.request.toll;

import com.tcss559.alltollpass.entity.toll.TransactionStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TollTransactionRequest {
    private Long agencyId;
    private String rfid;
    private String tollTransactionId;
    private TransactionStatus status;
}
