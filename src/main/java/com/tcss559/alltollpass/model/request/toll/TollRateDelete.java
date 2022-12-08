package com.tcss559.alltollpass.model.request.toll;

import com.tcss559.alltollpass.entity.traveler.VehicleType;
import lombok.Data;

@Data
public class TollRateDelete {
    private Long agencyId;
    private VehicleType vehicleType;
}
