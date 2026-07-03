package com.greet.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Greeting REST API Documentation")
                        .version("1.0.0")
                        .description("Interactive API specification for the Spring Boot Greeting Application. " +
                                "Contains endpoint schemas, authentication mechanisms, and role boundaries.")
                                        .contact(new Contact()
                                                .name("Kiran API Support")
                                                .email("support@greetapi.com")))
                        .addSecurityItem(new
                                SecurityRequirement().addList(securitySchemeName))
                        .components(new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("Enter your Bearer JWT token " +
                                                        "(excluding the 'Bearer ' prefix) to access secure endpoints.")));
    }
}
