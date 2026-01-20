package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.ApiResponse;
import com.vasyerp.rolebasedsystem.dto.CreateProductRequest;
import com.vasyerp.rolebasedsystem.dto.ProductDTO;
import com.vasyerp.rolebasedsystem.dto.UpdateProductRequest;
import com.vasyerp.rolebasedsystem.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @RequestHeader("userId") Long userId,
            @RequestHeader("companyId") Long companyId,
            @RequestBody CreateProductRequest request) {
        try {
            ProductDTO product = productService.createProduct(userId, companyId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Product created successfully", product));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCompany(
            @RequestHeader("userId") Long userId,
            @PathVariable Long companyId) {
        try {
            List<ProductDTO> products = productService.getProductsByCompany(userId, companyId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Products retrieved successfully", products));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(
            @RequestHeader("userId") Long userId,
            @PathVariable Long productId) {
        try {
            ProductDTO product = productService.getProductById(userId, productId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Product retrieved successfully", product));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> searchProductsByName(
            @RequestHeader("userId") Long userId,
            @RequestParam Long companyId,
            @RequestParam String searchTerm) {
        try {
            List<ProductDTO> products = productService.searchProductsByName(userId, companyId, searchTerm);
            return ResponseEntity.ok(new ApiResponse<>(true, "Products found", products));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @RequestHeader("userId") Long userId,
            @PathVariable Long productId,
            @RequestBody UpdateProductRequest request) {
        try {
            ProductDTO product = productService.updateProduct(userId, productId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully", product));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @RequestHeader("userId") Long userId,
            @PathVariable Long productId) {
        try {
            productService.deleteProduct(userId, productId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/branch/{companyId}/{branchId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByBranch(
            @RequestHeader("userId") Long userId,
            @PathVariable Long companyId,
            @PathVariable Long branchId) {
        try {
            List<ProductDTO> products = productService.getProductsByBranch(userId, companyId, branchId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Branch products retrieved successfully", products));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
//show roles availabe , how to assign role
