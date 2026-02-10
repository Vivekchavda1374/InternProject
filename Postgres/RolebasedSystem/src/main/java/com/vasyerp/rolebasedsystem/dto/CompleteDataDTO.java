package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteDataDTO {
    private Long id;
    private String type;
    private String companyName;
    private String branchName;
    private String parentCompany;
    private String gstNo;
    private String phoneNo;
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String roleName;
    private String productName;
    private String itemCode;
    private Double mrp;
    private Double sellingPrice;
    private String description;
    private Double stockQuantity;
    private Long userFrontId;
    private Long productCount;
    private Long hierarchyOrder;
}
