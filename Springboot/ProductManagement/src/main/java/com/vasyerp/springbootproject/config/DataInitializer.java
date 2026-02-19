package com.vasyerp.springbootproject.config;

import com.vasyerp.springbootproject.entity.Role;
import com.vasyerp.springbootproject.entity.User;
import com.vasyerp.springbootproject.repository.RoleRepository;
import com.vasyerp.springbootproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashSet;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            Role userRole = roleRepository.findByRoleName("USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "USER")));
            Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));
            
            // Create demo USER
            User user = new User();
            user.setCompany("TechCorp");
            user.setBranch("Main Branch");
            user.setUsername("user123");
            user.setPassword("user123");
            user.setRoles(new HashSet<>() {{ add(userRole); }});
            userRepo.save(user);
            System.out.println("✓ Demo USER created - Company: TechCorp, Branch: Main Branch, Username: user123");

            // Create demo ADMIN
            User admin = new User();
            admin.setCompany("TechCorp");
            admin.setBranch("Head Office");
            admin.setUsername("admin123");
            admin.setPassword("admin123");
            admin.setRoles(new HashSet<>() {{ add(adminRole); }});
            userRepo.save(admin);
            System.out.println("✓ Demo ADMIN created - Company: TechCorp, Branch: Head Office, Username: admin123");
        };
    }
}
