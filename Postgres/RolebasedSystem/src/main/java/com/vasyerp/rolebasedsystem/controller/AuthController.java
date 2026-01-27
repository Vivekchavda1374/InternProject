package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.ApiResponse;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserFrontRepository userFrontRepository;

    public AuthController(UserFrontRepository userFrontRepository) {
        this.userFrontRepository = userFrontRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<ApiResponse<UserFront>> login(@RequestParam String username, 
                                                       @RequestParam String password, 
                                                       HttpSession session) {
        UserFront user = userFrontRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("userId", user.getUserFrontId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("isAdmin", user.getParentCompanyId() == null);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", user));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid credentials", null));
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
                new Object() {
                    public Long userId = (Long) session.getAttribute("userId");
                    public String username = (String) session.getAttribute("username");
                    public Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
                }));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No active session", null));
    }
}