package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Sales;
import java.util.List;
import java.util.Optional;

public interface SalesRepository {

    Sales save(Sales sales);

    Optional<Sales> findById(Long id);

    List<Sales> findAll();

    List<Sales> findByBranch_UserFrontId(Long branchId);

    List<Sales> findByCompany_UserFrontId(Long companyId);

    void deleteById(Long id);
}
