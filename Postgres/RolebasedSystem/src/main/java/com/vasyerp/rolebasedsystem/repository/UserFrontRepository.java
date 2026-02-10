package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFront;
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
}
