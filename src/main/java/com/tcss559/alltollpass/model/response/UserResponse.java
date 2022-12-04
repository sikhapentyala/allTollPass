package com.tcss559.alltollpass.model.response;

import com.tcss559.alltollpass.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private String name;
    private String email;
}
