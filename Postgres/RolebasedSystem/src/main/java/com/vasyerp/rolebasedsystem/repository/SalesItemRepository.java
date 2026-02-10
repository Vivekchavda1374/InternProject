package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
}
