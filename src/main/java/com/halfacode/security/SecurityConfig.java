package com.halfacode.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter authFilter;


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] AUTH_WHITELIST = {
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
    public UserDetailsService userDetailsService() {
        return new UserInfoUserDetailsService();
    }
       @Bean
       public AuthenticationProvider authenticationProvider() {
           DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
           authenticationProvider.setUserDetailsService(userDetailsService());
           authenticationProvider.setPasswordEncoder(passwordEncoder());
           return authenticationProvider;
       }

       @Bean
       public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
       }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagmet->sessionManagmet.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
                                        "/api/cart/**",
                                        "/api/shipments/**",
                                        "api/orders/**",
                                        "api/configuration/**",
                                        "/api/public/authenticate",
                                        "/actuator/*",
                                        "/api/roles/**",
                                        "/api/checkout/**",
                                        "/api/configuration/**",
                                        "/api/delivery/**",
                                        "/api/recommendations/**",
                                        "/api/shipments/**",

                                        "/api/orders**").permitAll()// Permit access to specified endpoints without authentication
                                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll() // Allow user registration
                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // Permit POST requests to /api/auth/** without authentication
                                .anyRequest().authenticated()
                                .and()
                                .authenticationProvider(authenticationProvider())
                                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                );

        return http.build();
    }
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                //  .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        {
                            try {
                                authorize.requestMatchers(AUTH_WHITELIST).permitAll()

                                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .httpBasic()
                                        .and()
                                        .authenticationProvider(authenticationProvider());
                                       // .addFilterBefore(authFilter,  BasicAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        return http.build();
    }*/
  /*     @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
           http
                   .csrf().disable()
                   .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authorizeHttpRequests(authorize ->
                           authorize
                                   .requestMatchers(AUTH_WHITELIST).permitAll()
                                   // .and()
                                   .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                                   .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                                   .anyRequest().authenticated()
                   );
           // .httpBasic();
           return http.build();
       }
    */
}
   /* @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers("/**")
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}*/

   /* @Autowired
    private JwtRequestFilter authFilter;

    private static final String[] AUTH_WHITELIST = {
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
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoUserDetailsService();
    }

   *//* @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(authFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/customers/**").permitAll() // Allow access to /api/customers without authentication
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }*//*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
              //  .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        {
                            try {
                                authorize.requestMatchers(AUTH_WHITELIST).permitAll()

                                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                                        .anyRequest().authenticated()
                                         .and()
                                         .httpBasic()
                                        .and()
                                        .authenticationProvider(authenticationProvider())
                                        .addFilterBefore(authFilter,  BasicAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        return http.build();
    }
}*/
   /* @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagmet->sessionManagmet.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
                                        "api/configuration/**",
                                        "/api/public/authenticate",
                                        "/actuator/*",

                                        "/api/orders**").permitAll()// Permit access to specified endpoints without authentication
                                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll() // Allow user registration
                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // Permit POST requests to /api/auth/** without authentication
                                .anyRequest().authenticated()
                                .and()
                                .authenticationProvider(authenticationProvider())
                                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                );

        return http.build();
    }
}*/