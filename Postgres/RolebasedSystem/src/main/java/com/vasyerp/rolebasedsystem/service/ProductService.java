package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CreateProductRequest;
import com.vasyerp.rolebasedsystem.dto.ProductDTO;
import com.vasyerp.rolebasedsystem.dto.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(Long userId, Long companyId, CreateProductRequest request);

    ProductDTO updateProduct(Long userId, Long productId, UpdateProductRequest request);

    void deleteProduct(Long userId, Long productId);

    List<ProductDTO> getProductsByCompany(Long userId, Long targetCompanyId);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long userId, Long productId);

    List<ProductDTO> searchProductsByName(Long userId, Long companyId, String searchTerm);

    List<ProductDTO> getProductsByBranch(Long userId, Long companyId, Long branchId);
}
