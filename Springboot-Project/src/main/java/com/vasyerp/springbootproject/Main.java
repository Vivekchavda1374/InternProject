package com.vasyerp.springbootproject;

import com.vasyerp.springbootproject.entity.Product;
import com.vasyerp.springbootproject.services.ProductSrvcs;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Main implements CommandLineRunner {

    private final ProductSrvcs productSrvcs;

    @Autowired
    public Main(ProductSrvcs productSrvcs) {
        this.productSrvcs = productSrvcs;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed database with sample products if empty
        if (productSrvcs.getAllProductList().isEmpty()) {
            productSrvcs.addProduct(new Product("ERP", 50, 3, "Software", null));
            productSrvcs.addProduct(new Product("Product", 30, 1, "Hardware", null));
            productSrvcs.addProduct(new Product("Java", 100, 5, "Software", null));
            productSrvcs.addProduct(new Product("Book", 5000, 10, "Hardware", null));
        }
    }
}