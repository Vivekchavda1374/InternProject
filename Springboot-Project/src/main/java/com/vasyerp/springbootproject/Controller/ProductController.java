package com.vasyerp.springbootproject.Controller;

import java.util.List;

import com.vasyerp.springbootproject.entity.Product;
import com.vasyerp.springbootproject.services.ProductSrvcs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductSrvcs productSrvcs;

    @Autowired
    public ProductController(ProductSrvcs productSrvcs) {
        this.productSrvcs = productSrvcs;
    }

    @GetMapping
    public List<Product> getAll() {
        return productSrvcs.getAllProductList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable long id) {
        Product p = productSrvcs.searchProductById(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product saved = productSrvcs.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/category/{category}")
    public List<Product> byCategory(@PathVariable String category) {
        return productSrvcs.filterProductByCategory(category);
    }

    @GetMapping("/sort/price")
    public List<Product> sortByPrice() {
        return productSrvcs.sortByPrice();
    }

    @GetMapping("/sort/name")
    public List<Product> sortByName() {
        return productSrvcs.sortProductByName();
    }
}
