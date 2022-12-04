package com.tcss559.alltollpass.model.request;

import com.tcss559.alltollpass.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String username;
    private String password;
    private Role role;
    private String name;
    private String email;
}
