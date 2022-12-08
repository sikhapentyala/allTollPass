package com.tcss559.alltollpass.model.response.toll;

import com.tcss559.alltollpass.entity.traveler.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TollRateDetail {
    private VehicleType vehicleType;
    private String location;
    private double tollRate;
}
