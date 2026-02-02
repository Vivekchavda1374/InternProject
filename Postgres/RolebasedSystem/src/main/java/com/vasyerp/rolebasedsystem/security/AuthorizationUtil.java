package com.vasyerp.rolebasedsystem.security;

import com.vasyerp.rolebasedsystem.service.UserFrontService;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtil {

    private final UserFrontService userFrontService;

    public AuthorizationUtil(UserFrontService userFrontService) {
        this.userFrontService = userFrontService;
    }

    public boolean hasRole(Long userId, String roleName) {
        return userFrontService.hasRole(userId, roleName);
    }

}
