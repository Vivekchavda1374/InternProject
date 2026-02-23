package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);

    List<Product> findByCompanyId(Long companyId);

    Optional<Product> findByProductNameAndCompanyId(String productName, Long companyId);

    Optional<Product> findByItemCodeAndCompanyId(String itemCode, Long companyId);

    List<Product> findAllByCompanyIdWithDetails(Long companyId);

    List<Product> searchProductsByName(Long companyId, String searchTerm);

    List<Product> findByCompanyIdIn(List<Long> companyIds);

    boolean existsByProductIdAndCompanyId(Long productId, Long companyId);
}
