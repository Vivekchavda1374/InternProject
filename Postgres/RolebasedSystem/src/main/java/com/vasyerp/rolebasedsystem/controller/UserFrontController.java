package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.*;
import com.vasyerp.rolebasedsystem.model.UserRole;
import com.vasyerp.rolebasedsystem.service.UserFrontService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-front")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserFrontController {

    private final UserFrontService userFrontService;

    public UserFrontController(UserFrontService userFrontService) {
        this.userFrontService = userFrontService;
    }

    @PostMapping("/{userFrontId}/addresses")
    public ResponseEntity<ApiResponse<AddressDTO>> addAddress(
            @PathVariable Long userFrontId,
            @RequestBody CreateAddressRequest request) {
        try {
            AddressDTO address = userFrontService.addAddress(userFrontId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Address added successfully", address));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{userFrontId}/addresses")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getAddresses(
            @PathVariable Long userFrontId) {
        try {
            List<AddressDTO> addresses = userFrontService.getAddresses(userFrontId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Addresses retrieved successfully", addresses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable Long addressId) {
        try {
            userFrontService.deleteAddress(addressId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Address deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    @PostMapping("/company/create")
    public ResponseEntity<ApiResponse<UserFrontDTO>> createCompany(
            @RequestHeader("userId") Long userId,
            @RequestBody CreateUserFrontRequest request) {
        try {
            UserFrontDTO company = userFrontService.createCompany(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Company created successfully", company));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    @GetMapping("/companies/{userId}")
    public ResponseEntity<ApiResponse<List<UserFrontDTO>>> getCompaniesByUser(
            @PathVariable Long userId) {
        try {
            List<UserFrontDTO> companies = userFrontService.getCompaniesByUser(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Companies retrieved successfully", companies));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<List<UserFrontDTO>>> getAllCompanies() {
        try {
            List<UserFrontDTO> companies = userFrontService.getAllCompanies();
            return ResponseEntity.ok(new ApiResponse<>(true, "Companies retrieved successfully", companies));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/branch/create")
    public ResponseEntity<ApiResponse<UserFrontDTO>> createBranch(
            @RequestBody CreateUserFrontRequest request) {
        try {
            UserFrontDTO branch = userFrontService.createBranch(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Branch created successfully", branch));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/branches")
    public ResponseEntity<ApiResponse<List<UserFrontDTO>>> getAllBranches() {
        try {
            List<UserFrontDTO> branches = userFrontService.getAllCompanies().stream()
                    .filter(company -> company.getParentCompanyId() != null)
                    .toList();
            return ResponseEntity.ok(new ApiResponse<>(true, "All branches retrieved successfully", branches));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/branches/{companyId}")
    public ResponseEntity<ApiResponse<List<UserFrontDTO>>> getBranchesByCompany(
            @PathVariable Long companyId) {
        try {
            List<UserFrontDTO> branches = userFrontService.getBranchesByCompany(companyId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Branches retrieved successfully", branches));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/branches/{companyId}/search")
    public ResponseEntity<ApiResponse<List<UserFrontDTO>>> searchBranchesByName(
            @PathVariable Long companyId,
            @RequestParam String searchTerm) {
        try {
            List<UserFrontDTO> branches = userFrontService.getBranchesByCompany(companyId);
            List<UserFrontDTO> filtered = branches.stream()
                    .filter(b -> b.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                    .toList();
            return ResponseEntity.ok(new ApiResponse<>(true, "Branches found", filtered));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/roles/all")
    public ResponseEntity<ApiResponse<List<UserRole>>> getAllRoles() {
        try {
            List<UserRole> roles = userFrontService.getAllRoles();
            return ResponseEntity.ok(new ApiResponse<>(true, "All roles retrieved successfully", roles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{userFrontId}")
    public ResponseEntity<ApiResponse<UserFrontDTO>> getUserFrontById(
            @PathVariable Long userFrontId) {
        try {
            UserFrontDTO userFront = userFrontService.getUserFrontById(userFrontId);
            return ResponseEntity.ok(new ApiResponse<>(true, "User/Company/Branch retrieved successfully", userFront));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{userFrontId}")
    public ResponseEntity<ApiResponse<UserFrontDTO>> updateUserFront(
            @PathVariable Long userFrontId,
            @RequestBody UpdateUserFrontRequest request) {
        try {
            UserFrontDTO updatedUserFront = userFrontService.updateUserFront(userFrontId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "User/Company/Branch updated successfully", updatedUserFront));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userFrontId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserFront(
            @PathVariable Long userFrontId) {
        try {
            userFrontService.deleteUserFront(userFrontId);
            return ResponseEntity.ok(new ApiResponse<>(true, "User/Company/Branch deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/assign-role")
    public ResponseEntity<ApiResponse<UserRoleDTO>> assignRoleToUser(
            @RequestBody AssignUserRoleRequest request) {
        try {
            UserRoleDTO userRole = userFrontService.assignRoleToUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Role assigned successfully", userRole));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userFrontId}/revoke-role/{roleId}")
    public ResponseEntity<ApiResponse<UserRoleDTO>> revokeRoleFromUser(
            @PathVariable Long userFrontId,
            @PathVariable Long roleId) {
        try {
            UserRoleDTO userRole = userFrontService.revokeRoleFromUser(userFrontId, roleId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Role revoked successfully", userRole));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{userFrontId}/roles")
    public ResponseEntity<ApiResponse<UserRoleDTO>> getUserRoles(
            @PathVariable Long userFrontId) {
        try {
            UserRoleDTO userRoles = userFrontService.getUserRoles(userFrontId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Roles retrieved successfully", userRoles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
