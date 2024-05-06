package malenquillaa.java.spring.bank.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authorization"))
                .components(new Components().addSecuritySchemes("Bearer Authorization", createAPIKeyScheme()))
                .info(new Info().title("Spring Bank API")
                        .description("Made by Malenquillaa")
                        .version("v0.0.1"));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public GroupedOpenApi accountOpenApi() {
        return GroupedOpenApi.builder()
                .group("Account Management")
                .pathsToMatch("/api/v1/account/**")
                .build();
    }
}