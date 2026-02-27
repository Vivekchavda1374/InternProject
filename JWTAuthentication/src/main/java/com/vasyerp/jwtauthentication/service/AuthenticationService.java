package com.vasyerp.jwtauthentication.service;

import com.vasyerp.jwtauthentication.dto.LoginRequest;
import com.vasyerp.jwtauthentication.dto.LoginResponse;
import com.vasyerp.jwtauthentication.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    

    private final JwtUtils jwtUtils;

    public AuthenticationService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            if (isValidCredentials(loginRequest.getUsername(), loginRequest.getPassword())) {
                String token = jwtUtils.generateToken(loginRequest.getUsername(), "ADMIN");

                LoginResponse response = new LoginResponse();
                response.setAccessToken(token);
                response.setTokenType("Bearer");
                response.setExpiresIn(900);
                response.setUsername(loginRequest.getUsername());
                response.setRole("ADMIN");

                return response;
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidCredentials(String username, String password) {
        try {
            return "admin".equals(username) && "1234".equals(password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
