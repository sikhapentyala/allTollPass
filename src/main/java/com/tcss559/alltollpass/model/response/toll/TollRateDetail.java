package com.tcss559.alltollpass.model.response.toll;

import com.tcss559.alltollpass.entity.traveler.VehicleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TollRateDetail {
    private VehicleType vehicleType;
    private String location;
    private double tollRate;
}
