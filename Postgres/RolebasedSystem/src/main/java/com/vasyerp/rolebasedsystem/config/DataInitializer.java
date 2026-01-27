package com.vasyerp.rolebasedsystem.config;

import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserFrontRepository userFrontRepository;

    public DataInitializer(UserFrontRepository userFrontRepository) {
        this.userFrontRepository = userFrontRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userFrontRepository.findByUsername("admin") == null) {
            UserFront admin = new UserFront();
            admin.setName("Default Company");
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setParentCompanyId(null);
            userFrontRepository.save(admin);
        }

        UserFront adminCompany = userFrontRepository.findByUsername("admin");
        if (userFrontRepository.findByUsername("user") == null && adminCompany != null) {
            UserFront user = new UserFront();
            user.setName("Default Branch");
            user.setUsername("user");
            user.setPassword("user");
            user.setParentCompanyId(adminCompany.getUserFrontId());
            userFrontRepository.save(user);
        }
    }
}