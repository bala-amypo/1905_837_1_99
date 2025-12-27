package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // This forces Swagger to send requests to the relative path "/"
        // The browser will automatically resolve this to "https://YOUR-URL/..."
        Server server = new Server();
        server.setUrl("/");
        server.setDescription("Default Server");

        return new OpenAPI().servers(List.of(server));
    }
}