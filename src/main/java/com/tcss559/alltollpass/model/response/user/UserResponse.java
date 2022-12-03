package com.tcss559.alltollpass.model.response.user;

import com.tcss559.alltollpass.entity.user.UserRfid;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private double balance;
    private List<UserRfid> rfids;

}
