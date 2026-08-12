package com.ru.app.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 配置 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 禁用 CSRF（根据需求可选）
                .csrf(AbstractHttpConfigurer::disable)

                // 授权配置
                .authorizeHttpRequests(auth -> auth
                        // 允许匿名访问的端点
                        .requestMatchers(
                                "/api/login",
                                "/api/**",
                                "/api/refresh-token",
                                "/ws/**",
                                "/broadcast",
                                "/sendToUser",
                                "/notice/send"
                        ).permitAll()

                        // 其他所有请求需要认证
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://47.238.153.138"
        ));

        // 允许的方法
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 允许的头部
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-CSRF-TOKEN", "X-Requested-With","content-disposition"));

        // 允许凭证
        config.setAllowCredentials(true);

        // 暴露的头部
        config.setExposedHeaders(List.of("Authorization","content-disposition"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // CSRF 配置（如果启用 CSRF 保护时使用）
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieCustomizer(customizer -> {
            customizer.domain("localhost")
                    .path("/")
                    .secure(false) // 在开发环境中可以设置为false，生产环境应设置为true
                    .sameSite("Lax"); // 设置SameSite属性
        });
        return repository;
    }
}