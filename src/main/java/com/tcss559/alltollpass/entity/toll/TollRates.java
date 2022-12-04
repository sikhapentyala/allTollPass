package com.tcss559.alltollpass.entity.toll;

import com.tcss559.alltollpass.entity.traveler.VehicleType;
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
public class TollRates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long tollId;
    private VehicleType vehicleType;
    private double tollRate;

    @Builder.Default
    private LocalDateTime createdTimestamp = LocalDateTime.now();
    private LocalDateTime updatedTimestamp;

}
