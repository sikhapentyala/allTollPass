package com.tcss559.alltollpass.model.request;

import com.tcss559.alltollpass.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String username;
    private String password;
    private Role role;
}
