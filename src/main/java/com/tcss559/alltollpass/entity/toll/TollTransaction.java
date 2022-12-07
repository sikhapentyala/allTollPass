package com.tcss559.alltollpass.entity.toll;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author sikha
 * Entity for maintaining transaction observed at AllTollPass
 * These are the transactions to made for Toll Agencies.
 * The toll Agency will provide agencyid, rfid and its local unique transaction id
 *
 */


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TollTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long agencyId;
    private String rfid;
    private String tollTransactionId;

    //TODO: amount
    private double amount;

    private TransactionStatus status;

    @Builder.Default
    private LocalDateTime createTimestamp = LocalDateTime.now();
    private LocalDateTime updateTimestamp;

}
