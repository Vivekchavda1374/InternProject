package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFrontDTO {
    private Long userFrontId;
    private String name;

    private Long parentCompanyId;
    private String gstNo;
    private String phoneNo;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
}
