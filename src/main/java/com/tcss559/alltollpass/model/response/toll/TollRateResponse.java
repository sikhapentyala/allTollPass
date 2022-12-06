package com.tcss559.alltollpass.model.response.toll;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TollRateResponse {

    private Long agencyId;
    private String location;
    private List<TollRateDetail> rates;
}
