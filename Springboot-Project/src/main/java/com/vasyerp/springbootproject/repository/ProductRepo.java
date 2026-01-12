package com.vasyerp.springbootproject.repository;

import java.util.List;

import com.vasyerp.springbootproject.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByCategoryIgnoreCase(String category);
}