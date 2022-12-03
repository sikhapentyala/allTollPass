package com.tcss559.alltollpass.model.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcss559.alltollpass.entity.user.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RfidDetail {
    private String rfid;
    @Schema(type = "String", allowableValues = {"Hearts", "Diamonds", "Clubs", "Spades"})
    private VehicleType vehicleType;
}
