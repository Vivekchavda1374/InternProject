package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CreateProductRequest;
import com.vasyerp.rolebasedsystem.dto.ProductDTO;
import com.vasyerp.rolebasedsystem.dto.UpdateProductRequest;
import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.ProductRepository;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import org.springframework.cache.annotation.CacheEvict;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserFrontRepository userFrontRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              UserFrontRepository userFrontRepository) {
        this.productRepository = productRepository;
        this.userFrontRepository = userFrontRepository;
    }


    @Override
    @CacheEvict(value = {"products", "completeDataService"}, allEntries = true)
    public ProductDTO createProduct(Long userId, Long companyId, CreateProductRequest request) {

        if (!hasProductCreatePermission(userId, companyId)) {
            throw new RuntimeException("User does not have permission to create products");
        }

        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long actualCompanyId = (currentUser.getParentCompany() == null)
                ? currentUser.getUserFrontId()
                : currentUser.getParentCompany().getUserFrontId();

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


    @Override
    @CacheEvict(value = {"products", "completeDataService"}, allEntries = true)
    public ProductDTO updateProduct(Long userId, Long productId, UpdateProductRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!hasProductUpdatePermission(userId, product.getCompanyId())) {
            throw new RuntimeException("No permission to update");
        }

        if (request.getProductName() != null) product.setProductName(request.getProductName());
        if (request.getItemCode() != null) product.setItemCode(request.getItemCode());
        if (request.getMrp() != null) product.setMrp(request.getMrp());
        if (request.getSellingPrice() != null) product.setSellingPrice(request.getSellingPrice());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getStockQuantity() != null) product.setStockQuantity(request.getStockQuantity());

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }


    @Override
    @CacheEvict(value = {"products", "completeDataService"}, allEntries = true)
    public void deleteProduct(Long userId, Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!hasProductDeletePermission(userId, product.getCompanyId())) {
            throw new RuntimeException("No permission to delete");
        }

        productRepository.deleteById(productId);
    }

    @Override
    @Cacheable(value = "products", key = "'allProducts'")
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Cacheable(value = "products", key = "'product:' + #productId")
    public ProductDTO getProductById(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }

    @Override
    @Cacheable(value = "products", key = "'company:' + #targetCompanyId")
    public List<ProductDTO> getProductsByCompany(Long userId, Long targetCompanyId) {

        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validateCompanyAccess(currentUser, targetCompanyId);

        return productRepository.findByCompanyId(targetCompanyId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Cacheable(value = "products",
            key = "'search:' + #companyId + ':' + #searchTerm")
    public List<ProductDTO> searchProductsByName(Long userId,
                                                 Long companyId,
                                                 String searchTerm) {

        return productRepository.searchProductsByName(companyId, searchTerm)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ProductDTO> getProductsByBranch(Long userId, Long companyId, Long branchId) {
        return List.of();
    }

     private void validateCompanyAccess(UserFront currentUser, Long targetCompanyId) {

        boolean isAllowed = false;

        if ("admin".equals(currentUser.getName())) {
            isAllowed = true;
        } else if (currentUser.getParentCompany() == null) {
            if (targetCompanyId.equals(currentUser.getUserFrontId())) {
                isAllowed = true;
            }
        } else {
            if (targetCompanyId.equals(currentUser.getUserFrontId())) {
                isAllowed = true;
            }
        }

        if (!isAllowed) {
            throw new RuntimeException("Access denied");
        }
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
                .map(user -> user.getParentCompany() == null)
                .orElse(false);
    }

    private boolean isBranch(Long userId) {
        return userFrontRepository.findById(userId)
                .map(user -> user.getParentCompany() != null)
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
