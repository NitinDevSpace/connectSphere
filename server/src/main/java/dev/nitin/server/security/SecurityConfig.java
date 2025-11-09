package dev.nitin.server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // disable CSRF for Postman testing
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // only allow login/register endpoints without token
                        .requestMatchers("/api/auth/**").permitAll()
                        // everything else needs authentication
                        .anyRequest().authenticated()
                )
                // run JWT filter before anonymous filter
                .addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class)
                // disable form login or HTTP basic
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
