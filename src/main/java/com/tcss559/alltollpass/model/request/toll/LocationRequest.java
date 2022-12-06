package com.tcss559.alltollpass.model.request.toll;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationRequest {
    private Long agencyId;
    private String location;

}
