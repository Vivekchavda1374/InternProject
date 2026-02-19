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
    private Double totalPurchaseAmount;
    private Double totalSalesAmount;
    private Long totalProducts;

    public CompleteDataDTO(
            Long companyId,
            String companyName,
            String gstNo,
            String phoneNo,
            String addressType,
            String addressLine1,
            String addressLine2,
            String city,
            String state,
            String country,
            Long branchId,
            String branchName,
            Double totalPurchaseAmount,
            Double totalSalesAmount,
            Long totalProducts
    ) {
        this.id          = branchId != null ? branchId : companyId;
        this.type        = branchId != null ? "Branch" : "Company";
        this.companyName = companyName;
        this.branchName  = branchName;
        this.parentCompany       = branchId != null ? companyName : null;
        this.gstNo               = gstNo;
        this.phoneNo             = phoneNo;
        this.addressType         = addressType;
        this.addressLine1        = addressLine1;
        this.addressLine2        = addressLine2;
        this.city                = city;
        this.state               = state;
        this.country             = country;
        this.userFrontId         = branchId != null ? branchId : companyId;
        this.productCount        = totalProducts != null ? totalProducts : 0L;
        this.totalPurchaseAmount = totalPurchaseAmount != null ? totalPurchaseAmount : 0.0;
        this.totalSalesAmount    = totalSalesAmount != null ? totalSalesAmount : 0.0;
        this.totalProducts       = totalProducts != null ? totalProducts : 0L;
    }
}