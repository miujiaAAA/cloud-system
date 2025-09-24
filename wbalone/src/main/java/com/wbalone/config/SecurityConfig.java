package com.wbalone.config;

import com.wbalone.core.security.filter.JwtAuthenticationFilter;
import com.wbalone.core.security.exception.MyAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 启用Spring Security
@EnableMethodSecurity // 支持方法级别的权限控制（如@PreAuthorize）
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // JWT过滤器

    // 白名单路径（无需认证即可访问）
    private final String[] ignoreUrls = {
            "/api/v1/auth/login/**", // 登录接口
            "/doc.html", "/swagger-ui/**", "/v3/api-docs/**" // 接口文档
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 禁用CSRF（适用于无状态API）
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态会话（不创建Session）
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(ignoreUrls).permitAll() // 白名单路径允许匿名访问
                .anyRequest().authenticated() // 其他路径需要认证
            )
            .exceptionHandling(configurer ->
                    configurer
                            .authenticationEntryPoint(new MyAuthenticationEntryPoint()) // 未认证异常处理器
            );

        // 在用户名密码过滤器之前添加JWT过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 注入AuthenticationManager（用于登录时的认证）
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 密码加密器（使用BCrypt算法）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}