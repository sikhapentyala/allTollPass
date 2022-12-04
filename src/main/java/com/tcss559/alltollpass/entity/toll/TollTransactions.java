package com.tcss559.alltollpass.entity.toll;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TollTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long tollId;
    private String rfid;
    private String tollTransactionId;
    private TransactionStatus status;

    @Builder.Default
    private LocalDateTime createTimestamp = LocalDateTime.now();
    private LocalDateTime updateTimestamp;

}
