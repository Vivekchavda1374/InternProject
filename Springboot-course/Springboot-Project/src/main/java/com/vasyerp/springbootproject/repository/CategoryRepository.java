package com.vasyerp.springbootproject.repository;

import com.vasyerp.springbootproject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
