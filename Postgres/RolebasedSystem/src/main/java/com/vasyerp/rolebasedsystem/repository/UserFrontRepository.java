package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFront;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFrontRepository extends JpaRepository<UserFront, Long> {

    Optional<UserFront> findByName(String name);

    List<UserFront> findByParentCompanyIsNull();

    List<UserFront> findByParentCompany(UserFront parentCompany);

    boolean existsByParentCompany(UserFront parentCompany);

}
