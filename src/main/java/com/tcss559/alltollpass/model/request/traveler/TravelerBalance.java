package com.tcss559.alltollpass.model.request.traveler;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TravelerBalance {

    private Long userId;
    private double amount;
}
