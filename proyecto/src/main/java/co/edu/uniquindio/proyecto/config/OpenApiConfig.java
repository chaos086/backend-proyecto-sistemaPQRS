package co.edu.uniquindio.proyecto.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT emitido por POST /api/auth/login")))
                .info(new Info()
                        .title("API del Sistema PQRS - Universidad del Quindío")
                        .description("API REST para la gestión de Peticiones, Quejas, Reclamos y Sugerencias (PQRS) de la Universidad del Quindío.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("desarrollo@uniquindio.edu.co")
                                .url("https://www.uniquindio.edu.co"))
                        .license(new License()
                                .name("Licencia Académica")
                                .url("https://www.uniquindio.edu.co/academica")));
    }
}