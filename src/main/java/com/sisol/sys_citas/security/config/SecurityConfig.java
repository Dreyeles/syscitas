package com.sisol.sys_citas.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sisol.sys_citas.security.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.debug("Configurando SecurityFilterChain");
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/",
                    "/inicio",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/favicon.ico",
                    "/auth/login",
                    "/auth/login/**",
                    "/auth/registro/**",
                    "/auth/registrar",
                    "/auth/registro-paciente",
                    "/registro-paciente",
                    "/registrousuario"
                ).permitAll()
                .requestMatchers("/dashboard/**").authenticated()
                .requestMatchers("/dashboard-administrativo/**").hasAuthority("ROLE_PERSONAL_ADMINISTRATIVO")
                .requestMatchers("/dashboard-medico/**").hasAuthority("ROLE_MEDICO")
                .requestMatchers("/especialidad/**").hasAuthority("ROLE_PERSONAL_ADMINISTRATIVO")
                .requestMatchers("/medico/**").hasAuthority("ROLE_PERSONAL_ADMINISTRATIVO")
                .requestMatchers("/admin/servicios/**").hasAuthority("ROLE_PERSONAL_ADMINISTRATIVO")
                .requestMatchers("/servicio/api/**").hasAuthority("ROLE_PERSONAL_ADMINISTRATIVO")
                .requestMatchers("/especialidad/api/**").hasAuthority("ROLE_PERSONAL_ADMINISTRATIVO")
                .requestMatchers("/agenda-citas/**").authenticated()
                .requestMatchers("/api/agendar-cita/**").authenticated()
                .requestMatchers("/pagar/**").authenticated()
                .requestMatchers("/citas/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/inicio?showLoginModal=true")
                .loginProcessingUrl("/auth/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/inicio?showLoginModal=true&error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/inicio?showLoginModal=true&logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ATENCIÓN: Solo para pruebas, permite contraseñas en texto plano
        // IMPORTANTE: No cambiar a BCryptPasswordEncoder sin migrar contraseñas existentes
        // DEPRECATED: NoOpPasswordEncoder está marcado para eliminación en futuras versiones
        logger.warn("Usando NoOpPasswordEncoder - DEPRECATED - NO RECOMENDADO para producción");
        logger.warn("Considere migrar a BCryptPasswordEncoder cuando sea posible");
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }
}