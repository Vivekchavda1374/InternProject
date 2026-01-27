package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.ApiResponse;
import com.vasyerp.rolebasedsystem.model.UserRole;
import com.vasyerp.rolebasedsystem.repository.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleController {

    private final UserRoleRepository userRoleRepository;

    public RoleController(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserRole>> createRole(@RequestBody UserRole role) {
        try {
            UserRole savedRole = userRoleRepository.save(role);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Role created successfully", savedRole));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserRole>>> getAllRoles() {
        try {
            List<UserRole> roles = userRoleRepository.findAll();
            return ResponseEntity.ok(new ApiResponse<>(true, "Roles retrieved successfully", roles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<UserRole>> getRoleById(@PathVariable Long roleId) {
        try {
            UserRole role = userRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
            return ResponseEntity.ok(new ApiResponse<>(true, "Role retrieved successfully", role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long roleId) {
        try {
            if (!userRoleRepository.existsById(roleId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Role not found", null));
            }
            userRoleRepository.deleteById(roleId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Role deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}