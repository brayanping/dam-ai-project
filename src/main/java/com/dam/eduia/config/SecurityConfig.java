package com.dam.eduia.config;

import com.dam.eduia.security.FiltroJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private FiltroJwt filtroJwt;

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (no necesitan token)
                .requestMatchers("/api/auth/**").permitAll()
                // Todo lo demás necesita token
                .anyRequest().authenticated()
            )
            .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Encriptador de contraseñas
    @Bean
    public PasswordEncoder encriptadorPassword() {
        return new BCryptPasswordEncoder();
    }

    // Gestor de autenticación
    @Bean
    public AuthenticationManager gestorAutenticacion(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}