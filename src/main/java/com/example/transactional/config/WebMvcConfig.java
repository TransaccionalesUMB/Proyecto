package com.example.transactional.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de MVC para la aplicación
 * Define rutas y controladores de vista
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        // Redirigir la raíz a la página de inicio
        registry.addRedirectViewController("/", "/home");
        
        // Redirigir /products a /products/list para mantener compatibilidad
        registry.addRedirectViewController("/products", "/products/list");
    }
}
