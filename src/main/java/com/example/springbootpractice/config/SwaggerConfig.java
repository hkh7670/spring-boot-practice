package com.example.springbootpractice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(title = "spring-boot-practice",
        description = "Backend API 명세서",
        version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
            .version("v1.0.0")
            .title("API")
            .description("");

        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(
            jwt); // 헤더에 토큰 포함
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
            .name(jwt)
            .type(SecurityScheme.Type.HTTP)
            .scheme("Bearer")
            .bearerFormat("JWT")
        );

        return new OpenAPI()
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components);
    }
}