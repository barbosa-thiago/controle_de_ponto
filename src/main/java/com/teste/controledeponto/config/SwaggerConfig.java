package com.teste.controledeponto.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI customOpenAPI() {
        var security = new Components().addSecuritySchemes("Authorization", new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
        );

        var apiInfo = new Info()
            .title("Controle de Ponto - API")
            .description("API de controle de ponto Ilia")
        .version("1.0.0");

        return new OpenAPI()
            .info(apiInfo)
            .addSecurityItem(new SecurityRequirement().addList("Authorization"))
        .components(security);
    }
}
