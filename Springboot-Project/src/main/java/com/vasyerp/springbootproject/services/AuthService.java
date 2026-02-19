package com.vasyerp.springbootproject.services;

import com.vasyerp.springbootproject.entity.Role;
import com.vasyerp.springbootproject.entity.User;
import com.vasyerp.springbootproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password, String email) {
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(Role.USER);
        user.setEnabled(true);

        return userRepo.save(user);
    }

    public User registerAdmin(String username, String password, String email) {
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(Role.ADMIN);
        user.setEnabled(true);

        return userRepo.save(user);
    }
}
