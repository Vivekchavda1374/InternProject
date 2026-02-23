package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFront;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompleteDataRepository extends JpaRepository<UserFront, Long> {

    @Query(value = """
            SELECT
                c.user_front_id,
                c.name,
                b.gst_no,
                b.phone_no,
                bfa.address_type,
                bfa.address_line_1,
                bfa.address_line_2,
                bfa.city,
                bfa.state,
                bcn.name,
                b.user_front_id,
                b.name,
                COALESCE((
                    SELECT SUM(p.total_amount)
                    FROM purchase p
                    WHERE p.company_id = c.user_front_id
                      AND p.branch_id = b.user_front_id
                ), 0.0),
                COALESCE((
                    SELECT SUM(s.total_amount)
                    FROM sales s
                    WHERE s.company_id = c.user_front_id
                      AND s.branch_id = b.user_front_id
                ), 0.0),
                COALESCE((
                    SELECT COUNT(DISTINCT pi.product_id)
                    FROM purchase_item pi
                    JOIN purchase p2 ON p2.purchase_id = pi.purchase_id
                    WHERE p2.company_id = c.user_front_id
                      AND p2.branch_id = b.user_front_id
                ), 0)
            FROM user_front c
                LEFT JOIN user_front_address cfa ON cfa.user_front_id = c.user_front_id
                    AND cfa.user_front_address_id = (
                        SELECT MIN(a2.user_front_address_id)
                        FROM user_front_address a2
                        WHERE a2.user_front_id = c.user_front_id
                    )
                LEFT JOIN country ccn ON ccn.country_id = cfa.country_id
                LEFT JOIN user_front b ON b.parent_company_id = c.user_front_id
                LEFT JOIN user_front_address bfa ON bfa.user_front_id = b.user_front_id
                    AND bfa.user_front_address_id = (
                        SELECT MIN(a2.user_front_address_id)
                        FROM user_front_address a2
                        WHERE a2.user_front_id = b.user_front_id
                    )
                LEFT JOIN country bcn ON bcn.country_id = bfa.country_id
            WHERE c.parent_company_id IS NULL
              AND b.user_front_id IS NOT NULL
              AND (:companyId IS NULL OR c.user_front_id = :companyId)
              AND (:branchId IS NULL OR b.user_front_id = :branchId)
              AND (:country IS NULL OR LOWER(COALESCE(bcn.name, ccn.name)) = :country)
            ORDER BY c.user_front_id ASC, b.user_front_id ASC
            """, nativeQuery = true)
    List<Object[]> findCompleteBranchRows(
            @Param("companyId") Long companyId,
            @Param("branchId") Long branchId,
            @Param("country") String country
    );

    @Query(value = """
            SELECT
                c.user_front_id,
                c.name,
                c.gst_no,
                c.phone_no,
                ufa.address_type,
                ufa.address_line_1,
                ufa.address_line_2,
                ufa.city,
                ufa.state,
                cn.name,
                COALESCE((
                    SELECT SUM(p.total_amount)
                    FROM purchase p
                    WHERE p.company_id = c.user_front_id
                ), 0.0),
                COALESCE((
                    SELECT SUM(s.total_amount)
                    FROM sales s
                    WHERE s.company_id = c.user_front_id
                ), 0.0),
                COALESCE((
                    SELECT COUNT(DISTINCT pi.product_id)
                    FROM purchase_item pi
                    JOIN purchase p2 ON p2.purchase_id = pi.purchase_id
                    WHERE p2.company_id = c.user_front_id
                ), 0)
            FROM user_front c
            LEFT JOIN user_front_address ufa ON ufa.user_front_id = c.user_front_id
                AND ufa.user_front_address_id = (
                    SELECT MIN(a2.user_front_address_id)
                    FROM user_front_address a2
                    WHERE a2.user_front_id = c.user_front_id
                )
            LEFT JOIN country cn ON cn.country_id = ufa.country_id
            WHERE c.parent_company_id IS NULL
              AND (:companyId IS NULL OR c.user_front_id = :companyId)
              AND (:country IS NULL OR LOWER(cn.name) = :country)
            ORDER BY c.user_front_id ASC
            """, nativeQuery = true)
    List<Object[]> findCompleteCompanyRows(
            @Param("companyId") Long companyId,
            @Param("country") String country
    );

    @Query(value = """
            SELECT u.user_front_id, u.parent_company_id
            FROM user_front u
            WHERE u.user_front_id = :userId
            """, nativeQuery = true)
    List<Object[]> findUserScope(@Param("userId") Long userId);
}
