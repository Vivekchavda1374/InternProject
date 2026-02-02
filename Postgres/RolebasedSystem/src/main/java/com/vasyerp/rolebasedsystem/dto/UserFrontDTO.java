package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFrontDTO {
    private Long userFrontId;
    private String name;
    private Long parentCompanyId;
    private String gstNo;
    private String phoneNo;
    private List<AddressDTO> addresses;
}
