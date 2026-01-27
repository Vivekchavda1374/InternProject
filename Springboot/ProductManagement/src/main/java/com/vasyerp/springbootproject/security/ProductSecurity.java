package com.vasyerp.springbootproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ProductSecurity {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails Vivek_User = User.builder()
                .username("Vivek_User")
                .password("{noop}test123")
                .roles("User")
                .build();


        UserDetails Vivek_Admin = User.builder()
                .username("Vivek_Admin")
                .password("{noop}test123")
                .roles("User", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(Vivek_User, Vivek_Admin);
    }
}