package com.example.demo.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.infrastructure.persistence.repository")
public class DatabaseConfig {
    // Configuración adicional de base de datos si es necesaria
}