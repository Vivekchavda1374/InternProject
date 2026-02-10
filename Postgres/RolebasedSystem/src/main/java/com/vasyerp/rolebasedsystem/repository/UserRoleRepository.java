package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserRole;
import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    UserRole save(UserRole role);

    Optional<UserRole> findById(Long id);

    Optional<UserRole> findByRoleName(String roleName);

    List<UserRole> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);
}
