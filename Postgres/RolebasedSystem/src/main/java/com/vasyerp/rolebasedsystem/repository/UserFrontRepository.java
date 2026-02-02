package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFront;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFrontRepository extends JpaRepository<UserFront, Long> {

    Optional<UserFront> findByName(String name);

    boolean existsByParentCompany(UserFront parentCompany);

}
