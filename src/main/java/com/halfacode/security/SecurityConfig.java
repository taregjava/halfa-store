package com.halfacode.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/api/public/**",
            "/api/categories/**",
            "/path/to/images/**",
            "/api/products/**",
            "/api/users/**",
            "/api/car/**",
            "/api/shipments/**",
            "/api/orders/**",
            "/api/public/authenticate",
            "/actuator/*",
            "/swagger-ui/**"
    };

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
/*  http.

                authenticationProvider(customAuthenticationProvider()) // Use your custom authentication provider
*/
 /*   @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }*/

    /**/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/v3/api-docs/**",
                                        "/api/public/**",
                                        "/api/categories/**",
                                        "/path/to/images/**",
                                        "/api/products/**",
                                        "api/users/**",
                                        "/api/car/**",
                                        "/api/shipments/**",
                                        "api/orders/**",
                                        "/api/public/authenticate",
                                        "/actuator/*",

                                        "/api/**").permitAll()// Permit access to specified endpoints without authentication
                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // Permit POST requests to /api/auth/** without authentication
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}