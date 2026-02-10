package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFrontAddress;
import java.util.List;
import java.util.Optional;

public interface UserFrontAddressRepository {

    UserFrontAddress save(UserFrontAddress address);

    Optional<UserFrontAddress> findById(Long id);

    List<UserFrontAddress> findAll();

    List<UserFrontAddress> findByUserFront_UserFrontId(Long userFrontId);

    void deleteById(Long id);
}