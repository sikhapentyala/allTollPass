package com.tcss559.alltollpass.model.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RfidRequest {
    private long userId;
    private List<RfidDetail> rfids;
}
