package com.tcss559.alltollpass.model.request.toll;

import com.tcss559.alltollpass.entity.traveler.VehicleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TollRateRequest {
    private Long agencyId;
    private VehicleType vehicleType;
    private double tollRate;
}
