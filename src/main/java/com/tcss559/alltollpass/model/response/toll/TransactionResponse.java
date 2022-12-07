package com.tcss559.alltollpass.model.response.toll;

import com.tcss559.alltollpass.entity.toll.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long agencyId;
    private String rfid;
    private String tollTransactionId;
    private TransactionStatus status;
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
}
