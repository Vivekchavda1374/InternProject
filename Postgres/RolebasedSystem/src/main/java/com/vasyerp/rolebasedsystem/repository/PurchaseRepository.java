package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByBranch_UserFrontId(Long branchId);
    List<Purchase> findByCompany_UserFrontId(Long companyId);
}
