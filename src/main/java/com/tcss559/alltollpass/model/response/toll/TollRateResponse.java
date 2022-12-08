package com.tcss559.alltollpass.model.response.toll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TollRateResponse {

    private Long agencyId;
    private String location;
    private List<TollRateDetail> rates;
}
