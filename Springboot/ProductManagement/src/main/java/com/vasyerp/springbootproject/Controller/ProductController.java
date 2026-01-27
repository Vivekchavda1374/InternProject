package com.vasyerp.springbootproject.Controller;

import java.util.List;

import com.vasyerp.springbootproject.entity.Product;
import com.vasyerp.springbootproject.services.ProductSrvcs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductSrvcs productSrvcs;

    @Autowired
    public ProductController(ProductSrvcs productSrvcs) {
        this.productSrvcs = productSrvcs;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Product> getAll() {
        return productSrvcs.getAllProductList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Product> getById(@PathVariable long id) {
        Product p = productSrvcs.searchProductById(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product saved = productSrvcs.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product productDetails) {
        Product updated = productSrvcs.updateProduct(id, productDetails);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Product product = productSrvcs.searchProductById(id);
        if (product == null) return ResponseEntity.notFound().build();
        productSrvcs.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Product> byCategory(@PathVariable String category) {
        return productSrvcs.filterProductByCategory(category);
    }

    @GetMapping("/sort/price")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Product> sortByPrice() {
        return productSrvcs.sortByPrice();
    }

    @GetMapping("/sort/name")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Product> sortByName() {
        return productSrvcs.sortProductByName();
    }

}
