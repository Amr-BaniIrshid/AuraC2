package com.server.contestControl.authServer.config;

import com.server.contestControl.authServer.filter.EmailVerificationFilter;
import com.server.contestControl.authServer.filter.JwtAuthFilter;
import com.server.contestControl.authServer.filter.LogoutFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
    private final EmailVerificationFilter emailVerificationFilter;
    private final LogoutFilter logoutFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**", "/verify/**").permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers("/api/contest/**").hasAnyRole("TEAM", "ADMIN")
                        .requestMatchers("/api/submissions/**").hasAnyRole("TEAM", "ADMIN")
                        .requestMatchers("/api/callback/judge0/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(emailVerificationFilter, JwtAuthFilter.class)
                .addFilterBefore(logoutFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
