package com.vasyerp.rolebasedsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RolebasedSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RolebasedSystemApplication.class, args);
    }

}
