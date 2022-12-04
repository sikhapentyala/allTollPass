package com.tcss559.alltollpass.model.response.traveler;

import com.tcss559.alltollpass.entity.traveler.TravelerRfid;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TravelerResponse {
    private Long userId;
    private double balance;
    private List<TravelerRfidResponse> rfids;

}
