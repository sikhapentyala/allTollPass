package com.tcss559.alltollpass.model.response.traveler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcss559.alltollpass.entity.traveler.TransactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({
        "rfid",
        "type",
        "amount",
        "tollLocation",
        "created_at"
})
public class TransactionDetail {
    private String rfid;
    private TransactionType type;
    private double amount;
    private String tollLocation;

    @JsonProperty("created_at")
    private String createdTimestamp;
}
