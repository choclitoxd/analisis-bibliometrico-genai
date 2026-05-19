package com.bibliometria.config;

import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración general de la aplicación y beans de red.
 */
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
