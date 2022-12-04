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
    private String user;
    private String password;

    private Role role;
    private String name;
    @Column(unique = true)
    private String email;

    private String isActive;

    @Builder.Default
    private LocalDateTime createdTimestamp = LocalDateTime.now();
    private LocalDateTime updatedTimestamp;
}
