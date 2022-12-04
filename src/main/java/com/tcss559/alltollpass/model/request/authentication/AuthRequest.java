package com.tcss559.alltollpass.model.request.authentication;

import com.tcss559.alltollpass.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {
    private String username;
    private String password;
    private Role role;
}
