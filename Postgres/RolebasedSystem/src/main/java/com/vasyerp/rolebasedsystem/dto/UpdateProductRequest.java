package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    private String productName;
    private String itemCode;
    private Double mrp;
    private Double sellingPrice;
    private Double purchasePrice;
    private String productVariantName;
    private String description;
    private Double stockQuantity;
}
