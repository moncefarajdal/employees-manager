package com.employee.managment.employee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/employees/**").authenticated()
                        .requestMatchers("/api/v1/audit/**").authenticated()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        CustomUserDetails admin = new CustomUserDetails(
                "admin",
                passwordEncoder().encode("admin123"),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")),
                "ALL" // Admin has access to all departments
        );

        CustomUserDetails hr = new CustomUserDetails(
                "hr",
                passwordEncoder().encode("hr123"),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_HR_PERSONNEL")),
                "ALL" // HR has access to all departments
        );

        CustomUserDetails itManager = new CustomUserDetails(
                "it_manager",
                passwordEncoder().encode("manager123"),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER")),
                "IT"
        );

        CustomUserDetails salesManager = new CustomUserDetails(
                "sales_manager",
                passwordEncoder().encode("manager123"),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER")),
                "Sales"
        );

        return new InMemoryUserDetailsManager((UserDetails) admin, (UserDetails) hr, (UserDetails) itManager, (UserDetails) salesManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

