package com.tcss559.alltollpass.model.response.traveler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcss559.alltollpass.entity.traveler.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfidResponse {
    private Long userId;
    private String rfid;
    private VehicleType vehicleType;
}
