package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long userFrontAddressId;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String addressType;
}