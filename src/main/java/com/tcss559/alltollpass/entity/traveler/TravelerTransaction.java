package com.tcss559.alltollpass.entity.traveler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
