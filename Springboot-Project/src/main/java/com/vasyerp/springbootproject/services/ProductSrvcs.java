package com.vasyerp.springbootproject.services;

import java.util.List;

import com.vasyerp.springbootproject.repository.ProductRepo;
import com.vasyerp.springbootproject.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductSrvcs { // service layer for product operations
    private final ProductRepo productRepo; // repository dependency

    @Autowired
    public ProductSrvcs(ProductRepo productRepo) {
        this.productRepo = productRepo; // assign repository reference
    }

    public Product addProduct(Product product) {
        return productRepo.save(product);
    }

    public List<Product> getAllProductList() {
        return productRepo.findAll();
    }

    public Product searchProductById(long productId) {
        return productRepo.findById(productId).orElse(null);
    }

    public List<Product> sortByPrice() {
        return productRepo.findAll(Sort.by(Sort.Direction.ASC, "price"));
    }

    public List<Product> sortProductByName() {
        return productRepo.findAll(Sort.by(Sort.Direction.ASC, "productName"));
    }

    public List<Product> filterProductByCategory(String category) {
        return productRepo.findByCategoryIgnoreCase(category);
    }

    public void deleteById(Long id) {
        productRepo.deleteById(id);
    }
}
