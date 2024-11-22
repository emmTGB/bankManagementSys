package com.kevin.bankmanagementsys.config;


import com.kevin.bankmanagementsys.security.JwtAuthenticationFilter;
import com.kevin.bankmanagementsys.security.JwtTokenProvider;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> cors.configurationSource(configSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz  // 免token接口
                        .requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/v3/api-docs.yaml",
                            "/v3/api-docs.json",
                            "/swagger-ui.html",
                            "/swagger-resources/**",
                            "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/user/login", "/auth/user/register", "/auth/user/refresh").permitAll()
                        .requestMatchers("/auth/employee/login", "/auth/employee/register").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                ;

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource configSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setExposedHeaders(List.of(
                "Access-Token",
                "Refresh-Token",
                "ID"
        ));
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
