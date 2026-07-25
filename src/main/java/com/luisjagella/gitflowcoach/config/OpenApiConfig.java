package com.luisjagella.gitflowcoach.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gitflowCoachOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("GitFlow Coach API")
                        .description("API para orientar equipes na adoção de Git, organização de tarefas e boas práticas de versionamento")
                        .version("v1"));
    }
}
