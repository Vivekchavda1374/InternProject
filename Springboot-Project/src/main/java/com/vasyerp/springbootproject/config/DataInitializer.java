package com.vasyerp.springbootproject.config;

import com.vasyerp.springbootproject.entity.Role;
import com.vasyerp.springbootproject.entity.User;
import com.vasyerp.springbootproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            // Only create demo users if they don't exist
            if (!userRepo.existsByUsername("user")) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEmail("user@example.com");
                user.setRole(Role.USER);
                user.setEnabled(true);
                userRepo.save(user);
                System.out.println("✓ Demo USER created - Username: user, Password: user123");
            }

            if (!userRepo.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@example.com");
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);
                userRepo.save(admin);
                System.out.println("✓ Demo ADMIN created - Username: admin, Password: admin123");
            }
        };
    }
}
