package com.tcss559.alltollpass.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique=true)
    private String email;

    private String password;

    @Builder.Default
    private double balance = 0;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private LocalDateTime createdTimestamp = LocalDateTime.now();

    private LocalDateTime updatedTimestamp;

}
