package com.tcss559.alltollpass.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String mobile;

    private Role role;
    private String name;
    private String email;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private LocalDateTime createdTimestamp = LocalDateTime.now();
    private LocalDateTime updatedTimestamp;
}
