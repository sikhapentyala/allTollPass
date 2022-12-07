package com.tcss559.alltollpass.model.request.traveler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcss559.alltollpass.entity.traveler.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RfidRequest {
    private long userId;
    private String rfid;
    @Schema(type = "String", allowableValues = {"SMALL","MEDIUM","LARGE"})
    private VehicleType vehicleType;
}
