package com.tcss559.alltollpass.entity.traveler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author sikha
 * Entity for maintaining transaction observed for user
 * These are the transactions either to credit user account when he recharges his account
 * Or when he travells and toll is collected (debit)
 *
 */

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TravelerTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;

    @Column(unique = true)
    private String rfid;
    private double amount;
    private String tollLocation;
    private TransactionType type;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();



}
