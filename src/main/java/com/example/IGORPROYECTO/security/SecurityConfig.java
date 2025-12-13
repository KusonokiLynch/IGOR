package com.example.IGORPROYECTO.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.IGORPROYECTO.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpSession;

@Configuration
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            HttpSession session = request.getSession();
            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            
            if (redirectUrl != null) {
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect("/home");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF completamente
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ==== APIs REST - SIN AUTENTICACIÓN ====
                .requestMatchers("/api/**").permitAll()
                
                // ==== Rutas públicas ====
                .requestMatchers("/", "/registro", "/guardar", "/css/**", "/js/**", "/login").permitAll()
                .requestMatchers("/enviarCorreo").permitAll()
                
                // ==== Gmail ====
                .requestMatchers("/api/gmail/callback").permitAll()
                .requestMatchers("/api/gmail/**").authenticated()
                .requestMatchers("/gmail/**").authenticated()
                
                // ==== Roles específicos por módulo ====
                .requestMatchers("/administrador/**").hasRole("DIRECTOR")
                .requestMatchers("/cliente/**").hasRole("CLIENTE") 
                .requestMatchers("/trabajador/**").hasRole("TRABAJADOR")
                .requestMatchers("/supervisor/**").hasRole("SUPERVISOR")

                // ==== Análisis y Reportes ====
                .requestMatchers("/analisis/kpi/**").hasRole("DIRECTOR")
                .requestMatchers("/analisis/solicitud/**").hasAnyRole("TRABAJADOR", "SUPERVISOR","DIRECTOR")
                .requestMatchers("/analisis/peticiones/**").hasAnyRole("DIRECTOR", "CLIENTE", "TRABAJADOR", "SUPERVISOR")
                .requestMatchers("/analisis/**").authenticated()

                // ==== Proyectos ====
                .requestMatchers("/proyectos/nuevo/**").hasAnyRole("TRABAJADOR", "SUPERVISOR", "DIRECTOR")
                .requestMatchers("/proyectos/**").authenticated()
                
                // ==== Recursos ====
                .requestMatchers("/recursos/recursoNuevo").hasAnyRole("TRABAJADOR", "SUPERVISOR", "DIRECTOR")
                .requestMatchers("/recursos/**").authenticated()
                
                // ==== Todo lo demás requiere autenticación ====
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}