package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.ApiResponse;
import com.vasyerp.rolebasedsystem.dto.CompleteDataDTO;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import com.vasyerp.rolebasedsystem.service.CompleteDataExcelExportService;
import com.vasyerp.rolebasedsystem.service.CompleteDataService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/complete")
@RequiredArgsConstructor
public class CompleteDataController {

    private enum AccessScope {
        SYSTEM_ADMIN,
        COMPANY_ADMIN,
        BRANCH_ADMIN
    }

    private final CompleteDataService service;
    private final CompleteDataExcelExportService excelExportService;
    private final UserFrontRepository userFrontRepository;

    @GetMapping
    public ResponseEntity<List<CompleteDataDTO>> getAllData(
            @RequestParam(value = "country", required = false) String country,
            HttpSession session) {

        Long sessionUserId = extractLong(session.getAttribute("userId"));
        UserFront currentUser = resolveUser(sessionUserId);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());
        }

        AccessScope accessScope = resolveAccessScope(currentUser);
        String countryFilter = (country == null || country.trim().isEmpty()) ? null : country.trim();

        if (accessScope == AccessScope.SYSTEM_ADMIN) {
            return ResponseEntity.ok(service.getAllData(countryFilter));
        }

        return ResponseEntity.ok(service.getDataByUser(currentUser.getUserFrontId(), false, countryFilter));
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportDataByLoggedInUser(
            @RequestParam(value = "country", required = false) String country,
            HttpSession session
    ) {
        Long sessionUserId = extractLong(session.getAttribute("userId"));
        UserFront currentUser = resolveUser(sessionUserId);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "No active session. Please login first.", null));
        }

        AccessScope accessScope = resolveAccessScope(currentUser);
        boolean isSystemAdmin = accessScope == AccessScope.SYSTEM_ADMIN;
        String countryFilter = (country == null || country.trim().isEmpty()) ? null : country.trim();

        try {
            Path exportedFile = excelExportService.exportDataByUserToExcel(
                    currentUser.getUserFrontId(),
                    isSystemAdmin,
                    countryFilter,
                    accessScope.name()
            );
            Resource resource = new FileSystemResource(exportedFile);

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>(false, "Export failed: file was not created.", null));
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + exportedFile.getFileName() + "\"")
                    .header("X-Export-Scope", accessScope.name())
                    .header("X-Export-User", currentUser.getName())
                    .body(resource);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Export failed: " + ex.getMessage(), null));
        }
    }

    private Long extractLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    private UserFront resolveUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userFrontRepository.findById(userId).orElse(null);
    }

    private AccessScope resolveAccessScope(UserFront user) {
        if ("admin".equalsIgnoreCase(user.getName()) && user.getParentCompany() == null) {
            return AccessScope.SYSTEM_ADMIN;
        }
        if (user.getParentCompany() == null) {
            return AccessScope.COMPANY_ADMIN;
        }
        return AccessScope.BRANCH_ADMIN;
    }
}
