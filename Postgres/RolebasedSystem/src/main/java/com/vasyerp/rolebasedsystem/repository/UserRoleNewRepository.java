package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserRoleNew;
import java.util.List;
import java.util.Optional;

public interface UserRoleNewRepository {

    UserRoleNew save(UserRoleNew userRoleNew);

    Optional<UserRoleNew> findById(Long id);

    void deleteById(Long id);

    Optional<UserRoleNew> findByUserFrontIdAndRoleId(Long userFrontId, Long roleId);

    List<UserRoleNew> findByUserFrontId(Long userFrontId);
}
