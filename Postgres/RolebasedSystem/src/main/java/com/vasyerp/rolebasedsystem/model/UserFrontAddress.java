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

    @ManyToOne
    @JoinColumn(name = "user_front_id")
    @JsonIgnore
    private UserFront userFront;

    @Column(name = "address_type", length = 50)
    private String addressType;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    @JsonIgnore
    private Country countryRef;

    @Transient
    public String getCountry() {
        return countryRef != null ? countryRef.getName() : null;
    }

    public void setCountry(String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) {
            this.countryRef = null;
            return;
        }
        if (this.countryRef == null) {
            this.countryRef = new Country();
        }
        this.countryRef.setName(countryName.trim());
    }
}
