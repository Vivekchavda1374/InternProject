package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFrontAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFrontAddressRepository extends JpaRepository<UserFrontAddress, Long> {
    List<UserFrontAddress> findByUserFront_UserFrontId(Long userFrontId);
}