package com.tcss559.alltollpass.entity.toll;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author sikha
 * Entity for Toll Agency. The only propoerty apart from user details is location
 */

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Agency {

    @Id
    private Long id;

    private String location;

}
