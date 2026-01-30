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
    public boolean isAdmin(Long userId) {
        return hasRole(userId, "ADMIN");
    }

    public boolean isUser(Long userId) {
        return hasRole(userId, "USER");
    }

    public boolean hasAnyRole(Long userId, String... roleNames) {
        for (String roleName : roleNames) {
            if (hasRole(userId, roleName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAllRoles(Long userId, String... roleNames) {
        for (String roleName : roleNames) {
            if (!hasRole(userId, roleName)) {
                return false;
            }
        }
        return true;
    }

    public boolean canCreateProduct(Long userId) {
        return hasAnyRole(userId, "ADMIN");
    }

    public boolean canUpdateProduct(Long userId) {
        return hasAnyRole(userId, "ADMIN");
    }

    public boolean canDeleteProduct(Long userId) {
        return isAdmin(userId);
    }

    public boolean canViewProduct(Long userId) {
        return true;
    }

    public boolean belongsToCompany(Long userId, Long companyId) {
         return true;
    }


    public boolean hasAccessToBranch(Long userId, Long companyId, Long branchId) {
        return true;
    }
}
