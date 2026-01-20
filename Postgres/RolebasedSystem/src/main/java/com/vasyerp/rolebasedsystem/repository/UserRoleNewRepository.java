package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserRoleNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleNewRepository extends JpaRepository<UserRoleNew, Long> {

    Optional<UserRoleNew> findByUserFrontIdAndRoleId(Long userFrontId, Long roleId);

    @Query("SELECT u.roleId FROM UserRoleNew u WHERE u.userFrontId = :userFrontId")
    List<Long> findRolesByUserFrontId(@Param("userFrontId") Long userFrontId);

    @Query("SELECT COUNT(u) > 0 FROM UserRoleNew u WHERE u.userFrontId = :userFrontId AND u.roleId IN (SELECT r.roleId FROM UserRole r WHERE r.roleName = :roleName)")
    boolean hasRole(@Param("userFrontId") Long userFrontId, @Param("roleName") String roleName);
}
