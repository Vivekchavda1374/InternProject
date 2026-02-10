package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Purchase;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {

    Purchase save(Purchase purchase);

    Optional<Purchase> findById(Long id);

    List<Purchase> findAll();

    List<Purchase> findByBranch_UserFrontId(Long branchId);

    List<Purchase> findByCompany_UserFrontId(Long companyId);

    void deleteById(Long id);
}
