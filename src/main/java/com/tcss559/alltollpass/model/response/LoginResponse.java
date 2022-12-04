package com.tcss559.alltollpass.model.response;

import com.tcss559.alltollpass.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Long userId;
    private String username;
    private String name;
    private Role role;
}
