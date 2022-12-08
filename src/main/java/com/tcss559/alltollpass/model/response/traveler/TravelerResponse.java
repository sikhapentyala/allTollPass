package com.tcss559.alltollpass.model.response.traveler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TravelerResponse {
    private Long userId;
    private Double balance;
    private List<TravelerRfidResponse> rfids;

}
