package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCompanyId(Long companyId);

    Optional<Product> findByProductNameAndCompanyId(String productName, Long companyId);

    Optional<Product> findByItemCodeAndCompanyId(String itemCode, Long companyId);

    @Query("SELECT p FROM Product p WHERE p.companyId = :companyId ORDER BY p.productId DESC")
    List<Product> findAllByCompanyIdWithDetails(@Param("companyId") Long companyId);

    @Query("SELECT p FROM Product p WHERE p.companyId = :companyId AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchProductsByName(@Param("companyId") Long companyId, @Param("searchTerm") String searchTerm);

    boolean existsByProductIdAndCompanyId(Long productId, Long companyId);
}
