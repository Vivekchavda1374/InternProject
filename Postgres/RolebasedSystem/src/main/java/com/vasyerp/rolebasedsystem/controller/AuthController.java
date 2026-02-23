package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.ApiResponse;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    private final UserFrontRepository userFrontRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]\\$\\d\\d\\$[./A-Za-z0-9]{53}$");

    public AuthController(UserFrontRepository userFrontRepository) {
        this.userFrontRepository = userFrontRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<ApiResponse<UserFront>> login(@RequestParam String name,
            @RequestParam String password,
            HttpSession session) {
        UserFront user = userFrontRepository.findByName(name).orElse(null);
        if (user != null && isValidPassword(password, user)) {
            session.setAttribute("userId", user.getUserFrontId());
            session.setAttribute("name", user.getName());
            session.setAttribute("isAdmin", user.getName().equals("admin"));
            session.setAttribute("isCompany", !(user.getParentCompany() != null));
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
        session.invalidate();
        return ResponseEntity.ok(new ApiResponse<>(true, "Logout successful", null));
    }

    @GetMapping("/api/session")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> getSession(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Session active",
                    Map.of(
                            "userId", session.getAttribute("userId"),
                            "name", session.getAttribute("name"),
                            "isAdmin", session.getAttribute("isAdmin"),
                            "isCompany", session.getAttribute("isCompany"))));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No active session", null));
    }
}
