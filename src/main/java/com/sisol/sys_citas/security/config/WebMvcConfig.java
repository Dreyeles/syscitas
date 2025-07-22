package com.sisol.sys_citas.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry; // <-- ¡Esta importación es clave!

import com.sisol.sys_citas.security.AutorizacionInterceptor;

@Configuration // Indica que esta clase es una fuente de definición de beans para el contexto de la aplicación.
public class WebMvcConfig implements WebMvcConfigurer { // Implementa WebMvcConfigurer para personalizar la configuración de Spring MVC.

    private final AutorizacionInterceptor autorizacionInterceptor;

    // Constructor que inyecta el AutorizacionInterceptor.
    public WebMvcConfig(AutorizacionInterceptor autorizacionInterceptor){
        this.autorizacionInterceptor = autorizacionInterceptor;
    }

    // Registrar el interceptor
    @Override // Sobreescribe el método de la interfaz WebMvcConfigurer.
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // Este bloque está comentado, lo que significa que el interceptor no está activo actualmente.
        /*
        registry.addInterceptor(autorizacionInterceptor)
        .addPathPatterns("/**") // Aplica el interceptor a todas las rutas.
        // No aplicar el interceptor en estas rutas (excluye ciertas rutas del interceptor).
        .excludePathPatterns(
            "/auth/**",
            "/css/**",
            "/js/**",
            "/img/**",
            "/paciente/registrar",
            "/paciente/consultar/*",
            "/",
            "/inicio"
        );
        */
    }

    // Configurar recursos estáticos
    @Override // Sobreescribe el método para configurar cómo Spring sirve los recursos estáticos (CSS, JS, imágenes).
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapea la URL "/css/**" a los archivos en "classpath:/static/css/"
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        // Mapea la URL "/js/**" a los archivos en "classpath:/static/js/"
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        // Mapea la URL "/img/**" a los archivos en "classpath:/static/img/"
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
    }

}