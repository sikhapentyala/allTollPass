package com.tcss559.alltollpass.entity.traveler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author sikha
 * Entity for maintaining rfids for every user.
 * Assumption is that a traveller can have many rfid, and each rfid is registered to a vehicle.
 *
 */

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TravelerRfid implements Serializable {

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
