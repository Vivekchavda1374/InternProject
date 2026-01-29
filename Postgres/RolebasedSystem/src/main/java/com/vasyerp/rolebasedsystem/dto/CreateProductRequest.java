package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    private String productName;
    private String itemCode;
    private Double mrp;
    private Double sellingPrice;
    private String description;
    private Double stockQuantity;
}
