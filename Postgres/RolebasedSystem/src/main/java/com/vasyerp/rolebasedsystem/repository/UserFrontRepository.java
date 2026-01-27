package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFront;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFrontRepository extends JpaRepository<UserFront, Long> {

    Optional<UserFront> findByName(String name);

    @Query("SELECT u FROM UserFront u WHERE u.parentCompanyId IS NULL")
    List<UserFront> findAllCompanies();

    @Query("SELECT u FROM UserFront u WHERE u.parentCompanyId = :parentCompanyId")
    List<UserFront> findBranchesByCompany(@Param("parentCompanyId") Long parentCompanyId);

    boolean existsByParentCompanyId(Long parentCompanyId);

    UserFront findByUsername(String username);
}
