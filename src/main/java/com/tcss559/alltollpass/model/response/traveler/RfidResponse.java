package com.tcss559.alltollpass.model.response.traveler;

import com.tcss559.alltollpass.entity.traveler.VehicleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RfidResponse {
    private Long userId;
    private String rfid;
    private VehicleType vehicleType;
}
