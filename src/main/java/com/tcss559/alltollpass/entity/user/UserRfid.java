package com.tcss559.alltollpass.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRfid implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String rfid;

    private Long userId;

    private VehicleType vehicleType;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private LocalDateTime createdTimestamp = LocalDateTime.now();

}
