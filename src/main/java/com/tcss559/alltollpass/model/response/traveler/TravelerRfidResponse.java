package com.tcss559.alltollpass.model.response.traveler;

import com.tcss559.alltollpass.entity.traveler.VehicleType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TravelerRfidResponse {
    private String rfid;
    private VehicleType vehicleType;
    private LocalDateTime createdTimestamp;
}
