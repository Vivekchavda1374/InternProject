package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFront;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompleteDataRepository extends JpaRepository<UserFront, Long> {

    @Query("""
            SELECT
                c.userFrontId,
                c.name,
                c.gstNo,
                c.phoneNo,
                ufa.addressType,
                ufa.addressLine1,
                ufa.addressLine2,
                ufa.city,
                ufa.state,
                cn.name,
                b.userFrontId,
                b.name,
                COALESCE((
                    SELECT SUM(p.totalAmount)
                    FROM Purchase p
                    WHERE p.company.userFrontId = c.userFrontId
                      AND p.branch.userFrontId = b.userFrontId
                ), 0.0),
                COALESCE((
                    SELECT SUM(s.totalAmount)
                    FROM Sales s
                    WHERE s.company.userFrontId = c.userFrontId
                      AND s.branch.userFrontId = b.userFrontId
                ), 0.0),
                COALESCE((
                    SELECT COUNT(DISTINCT pi.product.productId)
                    FROM PurchaseItem pi
                    WHERE pi.purchase.company.userFrontId = c.userFrontId
                      AND pi.purchase.branch.userFrontId = b.userFrontId
                ), 0L)
            FROM UserFront c
                LEFT JOIN c.addresses ufa
                LEFT JOIN ufa.countryRef cn
                LEFT JOIN c.branches b
            WHERE c.parentCompany IS NULL
              AND b.userFrontId IS NOT NULL
              AND (:companyId IS NULL OR c.userFrontId = :companyId)
              AND (:branchId IS NULL OR b.userFrontId = :branchId)
              AND (:country IS NULL OR LOWER(cn.name) = :country)
              AND (ufa IS NULL OR ufa.userFrontAddressId = (
                    SELECT MIN(a2.userFrontAddressId)
                    FROM UserFrontAddress a2
                    WHERE a2.userFront.userFrontId = c.userFrontId
              ))
            ORDER BY c.userFrontId ASC, b.userFrontId ASC
            """)
    List<Object[]> findCompleteBranchRows(
            @Param("companyId") Long companyId,
            @Param("branchId") Long branchId,
            @Param("country") String country
    );

    @Query("""
            SELECT
                c.userFrontId,
                c.name,
                c.gstNo,
                c.phoneNo,
                ufa.addressType,
                ufa.addressLine1,
                ufa.addressLine2,
                ufa.city,
                ufa.state,
                cn.name,
                COALESCE((
                    SELECT SUM(p.totalAmount)
                    FROM Purchase p
                    WHERE p.company.userFrontId = c.userFrontId
                ), 0.0),
                COALESCE((
                    SELECT SUM(s.totalAmount)
                    FROM Sales s
                    WHERE s.company.userFrontId = c.userFrontId
                ), 0.0),
                COALESCE((
                    SELECT COUNT(DISTINCT pi.product.productId)
                    FROM PurchaseItem pi
                    WHERE pi.purchase.company.userFrontId = c.userFrontId
                ), 0L)
            FROM UserFront c
            LEFT JOIN c.addresses ufa
            LEFT JOIN ufa.countryRef cn
            WHERE c.parentCompany IS NULL
              AND (:companyId IS NULL OR c.userFrontId = :companyId)
              AND (:country IS NULL OR LOWER(cn.name) = :country)
              AND (ufa IS NULL OR ufa.userFrontAddressId = (
                    SELECT MIN(a2.userFrontAddressId)
                    FROM UserFrontAddress a2
                    WHERE a2.userFront.userFrontId = c.userFrontId
              ))
            ORDER BY c.userFrontId ASC
            """)
    List<Object[]> findCompleteCompanyRows(
            @Param("companyId") Long companyId,
            @Param("country") String country
    );

    @Query("""
            SELECT u.userFrontId, p.userFrontId
            FROM UserFront u
            LEFT JOIN u.parentCompany p
            WHERE u.userFrontId = :userId
            """)
    List<Object[]> findUserScope(@Param("userId") Long userId);
}
