package com.tcss559.alltollpass.entity.traveler;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TravelerAccount implements Serializable {

    //this is user id from User table
    @Id
    private Long id;
    @Builder.Default
    private double balance = 0;

}
