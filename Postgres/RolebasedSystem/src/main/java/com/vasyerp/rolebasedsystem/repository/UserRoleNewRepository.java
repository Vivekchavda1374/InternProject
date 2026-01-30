package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserRoleNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleNewRepository extends JpaRepository<UserRoleNew, Long> {

    Optional<UserRoleNew> findByUserFrontIdAndRoleId(Long userFrontId, Long roleId);

    List<UserRoleNew> findByUserFrontId(Long userFrontId);

}
