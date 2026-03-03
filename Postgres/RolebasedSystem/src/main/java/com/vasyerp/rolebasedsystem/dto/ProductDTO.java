package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long productId;
    private String productName;
    private Long companyId;
    private String itemCode;
    private Double mrp;
    private Double sellingPrice;
    private Double purchasePrice;
    private String productVariantName;
    private String description;
    private Double stockQuantity;
}
