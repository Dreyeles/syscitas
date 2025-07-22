package com.sisol.sys_citas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF (considera habilitarlo en producción)
            .authorizeHttpRequests(authorize -> authorize
                // *** RUTAS PÚBLICAS (ACCESIBLES SIN AUTENTICACIÓN) ***
                .requestMatchers(
                    "/",
                    "/inicio", // <--- ¡CAMBIO CLAVE AQUÍ! Permite el acceso a /inicio
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/auth/login",
                    "/auth/login/**",
                    "/auth/registro/mostrar",
                    "/auth/registrar"
                    // También puedes considerar añadir aquí otras rutas públicas como /nosotros, /servicios, etc.
                    // Por ejemplo:
                    // "/nosotros",
                    // "/servicios",
                    // "/error" // Si tienes una página de error dedicada
                ).permitAll()

                // *** RUTAS PROTEGIDAS (REQUIEREN AUTENTICACIÓN) ***
                .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/inicio", true) // La URL a la que irán después de iniciar sesión
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}