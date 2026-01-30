package com.vasyerp.rolebasedsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_front_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFrontAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_front_address_id")
    private Long userFrontAddressId;

    @OneToOne
    @JoinColumn(name = "user_front_id")
    @JsonIgnore
    private UserFront userFront;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "address_line_1", length = 255)
    private String addressLine1;

    @Column(name = "address_line_2", length = 255)
    private String addressLine2;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    private String country;
}
