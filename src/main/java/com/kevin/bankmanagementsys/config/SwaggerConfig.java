package com.kevin.bankmanagementsys.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BankManagement System")
                        .version("1.0")
                        .description("BankManagement System")
                        .contact(new Contact()
                                .name("kevin")
                                .email("lsg3079425053@outlook.com")
                                .url("https://space.bilibili.com/98793349")
                        )
                );
    }

    @Bean
    public GroupedOpenApi customGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**", "/auth/**")
                .build();
    }
}