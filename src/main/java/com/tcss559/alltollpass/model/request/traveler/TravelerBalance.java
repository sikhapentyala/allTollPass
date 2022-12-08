package com.tcss559.alltollpass.model.request.traveler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelerBalance {

    private Long userId;
    private double amount;
}
