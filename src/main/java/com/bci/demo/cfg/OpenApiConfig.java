package com.bci.demo.cfg;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Tag usersTag = new Tag()
                .name("Api Gestión de Usuarios BCI")
                .description("Operaciones sobre usuarios");

        return new OpenAPI()
                .info(new Info()
                        .title("API Usuarios BCI")
                        .version("1.0.0")
                        .description("API para el registro y gestión de usuarios"))
                .tags(List.of(usersTag))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
