package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CreateProductRequest;
import com.vasyerp.rolebasedsystem.dto.ProductDTO;
import com.vasyerp.rolebasedsystem.dto.UpdateProductRequest;
import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.repository.ProductRepository;
import com.vasyerp.rolebasedsystem.repository.UserRoleNewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRoleNewRepository userRoleNewRepository;

    public ProductService(ProductRepository productRepository, UserRoleNewRepository userRoleNewRepository) {
        this.productRepository = productRepository;
        this.userRoleNewRepository = userRoleNewRepository;
    }

    public ProductDTO createProduct(Long userId, Long companyId, CreateProductRequest request) {
        if (!hasProductCreatePermission(userId, companyId)) {
            throw new RuntimeException("User does not have permission to create products for this company");
        }

        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setItemCode(request.getItemCode());
        product.setCompanyId(companyId);

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    public List<ProductDTO> getProductsByCompany(Long userId, Long companyId) {
        if (!userBelongsToCompany(userId, companyId)) {
            throw new RuntimeException("User does not have access to this company");
        }

        List<Product> products = productRepository.findByCompanyId(companyId);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!userBelongsToCompany(userId, product.getCompanyId())) {
            throw new RuntimeException("User does not have access to this product");
        }

        return convertToDTO(product);
    }

    public List<ProductDTO> searchProductsByName(Long userId, Long companyId, String searchTerm) {
        if (!userBelongsToCompany(userId, companyId)) {
            throw new RuntimeException("User does not have access to this company");
        }

        List<Product> products = productRepository.searchProductsByName(companyId, searchTerm);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long userId, Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
         if (!hasProductUpdatePermission(userId, product.getCompanyId())) {
            throw new RuntimeException("User does not have permission to update products for this company");
        }
        if (request.getProductName() != null && !request.getProductName().isEmpty()) {
            product.setProductName(request.getProductName());
        }
        if (request.getItemCode() != null && !request.getItemCode().isEmpty()) {
            product.setItemCode(request.getItemCode());
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    public void deleteProduct(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!hasProductDeletePermission(userId, product.getCompanyId())) {
            throw new RuntimeException("User does not have permission to delete products for this company");
        }

        productRepository.deleteById(productId);
    }

    public List<ProductDTO> getProductsByBranch(Long userId, Long companyId, Long branchId) {
        if (!userHasAccessToCompanyBranch(userId, companyId, branchId)) {
            throw new RuntimeException("User does not have access to this branch");
        }

        List<Product> products = productRepository.findByCompanyId(companyId);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private boolean hasProductCreatePermission(Long userId, Long companyId) {
        return userRoleNewRepository.hasRole(userId, "ADMIN") || 
               userRoleNewRepository.hasRole(userId, "MANAGER");
    }

    private boolean hasProductUpdatePermission(Long userId, Long companyId) {
        return userRoleNewRepository.hasRole(userId, "ADMIN") || 
               userRoleNewRepository.hasRole(userId, "MANAGER");
    }

    private boolean hasProductDeletePermission(Long userId, Long companyId) {
        return userRoleNewRepository.hasRole(userId, "ADMIN");
    }

    private boolean userBelongsToCompany(Long userId, Long companyId) {
        List<Long> userCompanies = getUserCompanies(userId);
        return userCompanies.contains(companyId);
    }

    private List<Long> getUserCompanies(Long userId) {
       return List.of(userId);
    }

    private boolean userHasAccessToCompanyBranch(Long userId, Long companyId, Long branchId) {
        return userBelongsToCompany(userId, companyId);
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getProductId(),
                product.getProductName(),
                product.getCompanyId(),
                product.getItemCode()
        );
    }
}
