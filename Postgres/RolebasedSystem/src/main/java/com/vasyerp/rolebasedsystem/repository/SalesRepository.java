package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByBranch_UserFrontId(Long branchId);
    List<Sales> findByCompany_UserFrontId(Long companyId);
}
