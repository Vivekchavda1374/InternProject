package com.vasyerp.rolebasedsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Role Based System API")
                        .version("1.0")
                        .description("API documentation for Role Based System with Sales, Purchase, and Product management"));
    }
}
