package com.vasyerp.springbootproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    private double price;

    private double quantity;

    private String category;

    public Product() {
    }

    public Product(String productName, double price, double quantity, String category, Long productId) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Product{productName='" + productName + '\'' + ", price=" + price + ", quantity=" + quantity + ", category='" + category + '\'' + ", productId=" + productId + '}';
    }
}