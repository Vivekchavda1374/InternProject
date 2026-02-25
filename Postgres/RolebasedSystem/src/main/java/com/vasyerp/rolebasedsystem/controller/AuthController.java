package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.ApiResponse;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import com.vasyerp.rolebasedsystem.service.ScheduledExportContextService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    private final UserFrontRepository userFrontRepository;
    private final ScheduledExportContextService scheduledExportContextService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]\\$\\d\\d\\$[./A-Za-z0-9]{53}$");

    public AuthController(
            UserFrontRepository userFrontRepository,
            ScheduledExportContextService scheduledExportContextService
    ) {
        this.userFrontRepository = userFrontRepository;
        this.scheduledExportContextService = scheduledExportContextService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<ApiResponse<UserFront>> login(@RequestParam String name,
            @RequestParam String password,
            HttpServletRequest request) {
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null) {
            Long previousUserId = extractLong(existingSession.getAttribute("userId"));
            scheduledExportContextService.clearIfMatches(previousUserId);
            existingSession.invalidate();
        }

        UserFront user = userFrontRepository.findByName(name).orElse(null);
        if (user != null && isValidPassword(password, user)) {
            HttpSession session = request.getSession(true);
            String userType = resolveUserType(user);
            session.setAttribute("userId", user.getUserFrontId());
            session.setAttribute("name", user.getName());
            session.setAttribute("isAdmin", "SYSTEM_ADMIN".equals(userType));
            session.setAttribute("isCompany", "COMPANY_ADMIN".equals(userType));
            session.setAttribute("userType", userType);
            scheduledExportContextService.updateContext(user.getUserFrontId(), userType);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid credentials", null));
    }

    private boolean isValidPassword(String rawPassword, UserFront user) {
        String storedPassword = user.getPassword();
        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (BCRYPT_PATTERN.matcher(storedPassword).matches()) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }
        if (storedPassword.equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userFrontRepository.save(user);
            return true;
        }
        return false;
    }

    @PostMapping("/api/logout")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        Long currentUserId = extractLong(session.getAttribute("userId"));
        scheduledExportContextService.clearIfMatches(currentUserId);
        session.invalidate();
        return ResponseEntity.ok(new ApiResponse<>(true, "Logout successful", null));
    }

    @GetMapping("/api/session")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> getSession(HttpSession session) {
        Long userId = extractLong(session.getAttribute("userId"));
        if (userId != null) {
            String name = asString(session.getAttribute("name"));
            String userType = asString(session.getAttribute("userType"));
            Boolean isAdmin = asBoolean(session.getAttribute("isAdmin"));
            Boolean isCompany = asBoolean(session.getAttribute("isCompany"));

            if (userType == null) {
                UserFront user = userFrontRepository.findById(userId).orElse(null);
                userType = resolveUserType(user);
                if (name == null && user != null) {
                    name = user.getName();
                }
                isAdmin = "SYSTEM_ADMIN".equals(userType);
                isCompany = "COMPANY_ADMIN".equals(userType);
                session.setAttribute("name", name);
                session.setAttribute("userType", userType);
                session.setAttribute("isAdmin", isAdmin);
                session.setAttribute("isCompany", isCompany);
            }

            scheduledExportContextService.updateContext(userId, userType);

            Map<String, Object> sessionData = new LinkedHashMap<>();
            sessionData.put("userId", userId);
            sessionData.put("name", name);
            sessionData.put("isAdmin", isAdmin);
            sessionData.put("isCompany", isCompany);
            sessionData.put("userType", userType);

            return ResponseEntity.ok(new ApiResponse<>(true, "Session active",
                    sessionData));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No active session", null));
    }

    private String resolveUserType(UserFront user) {
        if (user == null) {
            return "UNKNOWN";
        }
        if ("admin".equalsIgnoreCase(user.getName()) && user.getParentCompany() == null) {
            return "SYSTEM_ADMIN";
        }
        if (user.getParentCompany() == null) {
            return "COMPANY_ADMIN";
        }
        return "BRANCH_ADMIN";
    }

    private Long extractLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    private String asString(Object value) {
        return value == null ? null : value.toString();
    }

    private Boolean asBoolean(Object value) {
        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }
        return Boolean.FALSE;
    }
}
