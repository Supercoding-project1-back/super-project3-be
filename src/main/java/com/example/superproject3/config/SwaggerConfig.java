package com.example.superproject3.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
//        String jwt = "JWT";
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
//        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
//                .name(jwt)
//                .type(SecurityScheme.Type.APIKEY)
//                .in(SecurityScheme.In.HEADER)
//                .name("Token"));

        return new OpenAPI()
                .info(new Info()
                        .title("Super Project 3 API")
                        .description("Super Project 3의 API 문서입니다."));
//                        .description("Super Project 3의 API 문서입니다."))
//                .addSecurityItem(securityRequirement)
//                .components(components);

    }
}