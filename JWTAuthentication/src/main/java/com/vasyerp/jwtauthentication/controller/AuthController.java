package com.vasyerp.jwtauthentication.controller;

import com.vasyerp.jwtauthentication.dto.LoginRequest;
import com.vasyerp.jwtauthentication.dto.LoginResponse;
import com.vasyerp.jwtauthentication.service.AuthenticationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Username and password are required"));
            }

            LoginResponse loginResponse = authenticationService.authenticate(loginRequest);

            if (loginResponse == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid username or password"));
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Getter
    public static class ErrorResponse {
        public String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }

    }
}
