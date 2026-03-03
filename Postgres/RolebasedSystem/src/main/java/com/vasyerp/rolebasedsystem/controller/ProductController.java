package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.ApiResponse;
import com.vasyerp.rolebasedsystem.dto.CreateProductRequest;
import com.vasyerp.rolebasedsystem.dto.ProductDTO;
import com.vasyerp.rolebasedsystem.dto.UpdateProductRequest;
import com.vasyerp.rolebasedsystem.model.Image;
import com.vasyerp.rolebasedsystem.service.ImageService;
import com.vasyerp.rolebasedsystem.service.ProductService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;

    public ProductController(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/import/template")
    public ResponseEntity<byte[]> downloadProductImportTemplate() {
        try {
            byte[] file = productService.generateProductImportTemplate();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"product-import-template.xlsx\"")
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }

    @PostMapping(value = "/import/validate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, Object>>> validateProductImport(
            @RequestHeader("userId") Long userId,
            @RequestHeader("companyId") Long companyId,
            @RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> validation = productService.validateProductImportExcel(userId, companyId, file);
            boolean isValid = Boolean.TRUE.equals(validation.get("valid"));
            if (isValid) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Excel file is valid", validation));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Excel validation failed", validation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, Object>>> importProductsFromExcel(
            @RequestHeader("userId") Long userId,
            @RequestHeader("companyId") Long companyId,
            @RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = productService.importProductsFromExcel(userId, companyId, file);
            boolean isValid = Boolean.TRUE.equals(result.get("valid"));
            if (!isValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Import stopped. Excel file is invalid", result));
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Products imported successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts(
            @RequestHeader("userId") Long userId) {
        try {
            List<ProductDTO> products = productService.getVisibleProducts(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Products retrieved successfully", products));
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping(value = "/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadProductImage(
            @RequestHeader("userId") Long userId,
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {
        try {
            String message = imageService.uploadImage(userId, productId, file);
            return ResponseEntity.ok(new ApiResponse<>(true, message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping(value = "/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateProductImage(
            @RequestHeader("userId") Long userId,
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {
        try {
            String message = imageService.updateImage(userId, productId, file);
            return ResponseEntity.ok(new ApiResponse<>(true, message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}/image")
    public ResponseEntity<ApiResponse<String>> removeProductImage(
            @RequestHeader("userId") Long userId,
            @PathVariable Long productId) {
        try {
            String message = imageService.deleteImage(userId, productId);
            return ResponseEntity.ok(new ApiResponse<>(true, message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        try {
            Image image = imageService.getImage(productId);
            MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
            if (image.getType() != null && !image.getType().isBlank()) {
                contentType = MediaType.parseMediaType(image.getType());
            }
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getName() + "\"")
                    .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePrivate().mustRevalidate())
                    .body(image.getData());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
