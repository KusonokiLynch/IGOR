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

import com.example.IGORPROYECTO.service.UserDetailsServiceImpl;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ⚠️ Se mantiene desactivado por ahora para evitar conflictos
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/registro", "/guardar", "/css/**", "/js/**", "/login").permitAll()
                .requestMatchers("/gmail/oauth2callback").permitAll()

                .requestMatchers("/administrador/**").hasRole("DIRECTOR")
                .requestMatchers("/cliente/**").hasRole("CLIENTE") 
                .requestMatchers("/trabajador/**").hasRole("TRABAJADOR")
                .requestMatchers("/supervisor/**").hasRole("SUPERVISOR")

                .requestMatchers("/analisis/kpi/**").hasRole("DIRECTOR")
                .requestMatchers("/analisis/solicitud/**").hasAnyRole("TRABAJADOR", "SUPERVISOR","DIRECTOR","CLIENTE")
                .requestMatchers("/analisis/peticiones/**").hasAnyRole("DIRECTOR", "CLIENTE", "TRABAJADOR", "SUPERVISOR")
                .requestMatchers("/analisis/**").authenticated()

                .requestMatchers("/proyectos/consultar").authenticated()
                .requestMatchers("/proyectos").authenticated()

                .requestMatchers("/proyectos/nuevo").hasAnyRole("DIRECTOR", "TRABAJADOR", "SUPERVISOR")
                .requestMatchers("/proyectos/editar/**").hasAnyRole("DIRECTOR", "TRABAJADOR", "SUPERVISOR")
                .requestMatchers("/proyectos/eliminar/**").hasAnyRole("DIRECTOR", "TRABAJADOR", "SUPERVISOR")

                .requestMatchers("/proyectos/**").authenticated()

                .requestMatchers("/recursos/recursoNuevo").hasAnyRole("DIRECTOR", "TRABAJADOR", "SUPERVISOR")
                .requestMatchers("/recursos/guardar").hasAnyRole("DIRECTOR", "TRABAJADOR", "SUPERVISOR")
                .requestMatchers("/recursos/editar/**").hasAnyRole("DIRECTOR", "TRABAJADOR", "SUPERVISOR")
                .requestMatchers("/recursos/actualizar/**").hasAnyRole("DIRECTOR", "TRABAJADOR", "SUPERVISOR")
                .requestMatchers("/recursos/eliminar/**").hasAnyRole("DIRECTOR", "TRABAJADOR", "SUPERVISOR")
                .requestMatchers("/recursos/**").authenticated()

                .requestMatchers("/enviarCorreo").permitAll()

                .requestMatchers("/api/gmail/**").authenticated()
                .requestMatchers("/gmail/**").authenticated()

                .anyRequest().authenticated()   
            )

            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", false)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )

          

            .exceptionHandling(exception -> exception
                .accessDeniedPage("/test-403")
            );

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}