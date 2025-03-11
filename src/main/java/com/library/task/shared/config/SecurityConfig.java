package com.library.task.shared.config;

import com.library.task.api.filter.JwtAuthenticationFilter;
import com.library.task.infrastructure.security.CustomUserDetailsService;
import com.library.task.infrastructure.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/api/auth/login").permitAll()

                // Only ADMIN can register new users
                .requestMatchers("/api/auth/signup").hasRole("ADMIN")

                // Book endpoints
                .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole("ADMIN", "LIBRARIAN", "PATRON")
                .requestMatchers(HttpMethod.POST, "/api/books/**").hasAnyRole("ADMIN", "LIBRARIAN")
                .requestMatchers(HttpMethod.PUT, "/api/books/**").hasAnyRole("ADMIN", "LIBRARIAN")
                .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasAnyRole("ADMIN", "LIBRARIAN")

                // Patron endpoints
                .requestMatchers("/api/patrons/**").hasAnyRole("ADMIN", "LIBRARIAN")

                // Borrowing endpoints
                .requestMatchers(HttpMethod.POST, "/api/borrow/**").hasAnyRole("ADMIN", "LIBRARIAN", "PATRON")
                .requestMatchers(HttpMethod.PUT, "/api/borrow/**").hasAnyRole("ADMIN", "LIBRARIAN", "PATRON")

                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil,userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
