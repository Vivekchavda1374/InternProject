package com.vasyerp.rolebasedsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name", length = 150, nullable = false)
    private String productName;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "item_code", length = 50)
    private String itemCode;

    @Column(name = "mrp")
    private Double mrp;

    @Column(name = "selling_price")
    private Double sellingPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "stock_quantity")
    private Double stockQuantity;
}
