package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(servers = {@Server(url = "https://9293.408procr.amypo.ai/", description = "Default Server URL")})
public class OpenApiConfig {
    // This class ensures Swagger requests go to the correct relative path
}