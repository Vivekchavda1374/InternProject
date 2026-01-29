package com.vasyerp.rolebasedsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserFrontRequest {

    @NonNull()
    private String name;

    private String password;

    private String gstNo;

    private String phoneNo;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;

    @JsonProperty("parentCompanyId")
    private Long parentCompanyId;
}
