package com.olalekan.CoolBank.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI coolBankOpenApi(){
        return new OpenAPI()
                .info(new Info()
                                .title("Cool Bank API")
                                .description("This is a banking API that allows P2P transfer and funding via paystack")
                                .contact(new Contact()
                                        .name("Abdulsalam Bunyamin Olalekan")
                                        .email("bunyaminusalam@gmail.com")
                                        .url("https://github.com/olalekan67"))
                                .license(new License()
                                        .name("Apache 2.0")
                                        .url("http://springdoc.org")))

                // 1. Add the Security Requirement (Lock icon)
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                // 2. Define the Security Scheme (How to authorize)
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}

