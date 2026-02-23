package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFront;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFrontRepository {

    UserFront save(UserFront userFront);

    Optional<UserFront> findById(Long id);

    List<UserFront> findAll();

    Optional<UserFront> findByName(String name);

    List<UserFront> findByParentCompanyIsNull();

    List<UserFront> findByParentCompany(UserFront parentCompany);

    boolean existsByParentCompany(UserFront parentCompany);

    boolean existsById(Long id);

    void deleteById(Long id);

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM user_front
                WHERE parent_company_id = :parentCompanyId
                  AND LOWER(TRIM(name)) = LOWER(TRIM(:branchName))
            )
            """, nativeQuery = true)
    Boolean existsBranchByParentCompanyIdAndName(
            @Param("parentCompanyId") Long parentCompanyId,
            @Param("branchName") String branchName);
}

