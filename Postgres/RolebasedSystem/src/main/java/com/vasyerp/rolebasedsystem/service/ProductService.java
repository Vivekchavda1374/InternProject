package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CreateProductRequest;
import com.vasyerp.rolebasedsystem.dto.ProductDTO;
import com.vasyerp.rolebasedsystem.dto.UpdateProductRequest;
import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.ProductRepository;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserFrontRepository userFrontRepository;

    public ProductService(ProductRepository productRepository, UserFrontRepository userFrontRepository) {
        this.productRepository = productRepository;
        this.userFrontRepository = userFrontRepository;
    }

    public ProductDTO createProduct(Long userId, Long companyId, CreateProductRequest request) {
        if (!hasProductCreatePermission(userId, companyId)) {
            throw new RuntimeException("User does not have permission to create products for this company");
        }

        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Determine the actual company ID for the product
        Long actualCompanyId;
        if (currentUser.getParentCompanyId() == null) {
            // Company user - use their own ID
            actualCompanyId = currentUser.getUserFrontId();
        } else {
            // Branch user - use parent company ID
            actualCompanyId = currentUser.getParentCompanyId();
        }

        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setItemCode(request.getItemCode());
        product.setCompanyId(actualCompanyId);
        product.setMrp(request.getMrp());
        product.setSellingPrice(request.getSellingPrice());
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
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
        if (request.getMrp() != null) {
            product.setMrp(request.getMrp());
        }
        if (request.getSellingPrice() != null) {
            product.setSellingPrice(request.getSellingPrice());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
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

    public List<ProductDTO> getProductsByCompany(Long userId, Long targetCompanyId) {
        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAllowed = false;

        // 1. Admin can see everything
        if ("admin".equals(currentUser.getName())) {
            isAllowed = true;
        }
        // 2. Company can see itself and its branches
        else if (currentUser.getParentCompanyId() == null) {
            if (targetCompanyId.equals(currentUser.getUserFrontId())) {
                isAllowed = true;
            } else {
                UserFront targetUser = userFrontRepository.findById(targetCompanyId).orElse(null);
                if (targetUser != null && currentUser.getUserFrontId().equals(targetUser.getParentCompanyId())) {
                    isAllowed = true;
                }
            }
        }
        // 3. Branch can only see itself
        else {
            if (targetCompanyId.equals(currentUser.getUserFrontId())) {
                isAllowed = true;
            }
        }

        if (!isAllowed) {
            // Alternatively, return empty list or throw exception.
            // For filter, empty list is safer than 403 error for UX, but exception is more
            // secure.
            // Keeping mostly consistent with previous error handling logic.
            // But let's verify if the user is trying to see "All" vs "One".
            // The method name implies "Get products OF company X".
            throw new RuntimeException("Access denied to view products of this company/branch");
        }

        List<Product> products = productRepository.findByCompanyId(targetCompanyId);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }

    public List<ProductDTO> searchProductsByName(Long userId, Long companyId, String searchTerm) {
        List<Product> products = productRepository.searchProductsByName(companyId, searchTerm);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByBranch(Long userId, Long companyId, Long branchId) {
        List<Product> products = productRepository.findByCompanyId(companyId);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private boolean hasProductCreatePermission(Long userId, Long companyId) {
        return isCompany(userId) || isBranch(userId);
    }

    private boolean hasProductUpdatePermission(Long userId, Long companyId) {
        return isCompany(userId) || isBranch(userId);
    }

    private boolean hasProductDeletePermission(Long userId, Long companyId) {
        return isCompany(userId);
    }

    private boolean isCompany(Long userId) {
        return userFrontRepository.findById(userId)
                .map(user -> user.getParentCompanyId() == null)
                .orElse(false);
    }

    private boolean isBranch(Long userId) {
        return userFrontRepository.findById(userId)
                .map(user -> user.getParentCompanyId() != null)
                .orElse(false);
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getProductId(),
                product.getProductName(),
                product.getCompanyId(),
                product.getItemCode(),
                product.getMrp(),
                product.getSellingPrice(),
                product.getDescription(),
                product.getStockQuantity());
    }
}
