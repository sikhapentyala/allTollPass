package com.tcss559.alltollpass.model.response.toll;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationResponse {
    private Long agencyId;
    private String location;
}
