package com.wbalone.config;

import cn.hutool.core.util.ArrayUtil;
import com.wbalone.config.property.SecurityProperties;
import com.wbalone.core.security.exception.MyAccessDeniedHandler;
import com.wbalone.core.security.exception.MyAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(requestMatcherRegistry -> {
                            // 忽略认证的 URI 地址
                            String[] ignoreUrls = securityProperties.getIgnoreUrls();
                            if (ArrayUtil.isNotEmpty(ignoreUrls)) {
                                requestMatcherRegistry.requestMatchers(ignoreUrls).permitAll();
                            }
                            // 其他请求都需要认证
                            requestMatcherRegistry.anyRequest().authenticated();
                        }
                )
                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(new MyAuthenticationEntryPoint()) // 未认证异常处理器
                                .accessDeniedHandler(new MyAccessDeniedHandler()) // 无权限访问异常处理器
                )

                // 禁用默认的 Spring Security 特性，适用于前后端分离架构
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态认证，不使用 Session
                )
                .csrf(AbstractHttpConfigurer::disable)      // 禁用 CSRF 防护，前后端分离无需此防护机制
                .formLogin(AbstractHttpConfigurer::disable) // 禁用默认的表单登录功能，前后端分离采用 Token 认证方式
                .httpBasic(AbstractHttpConfigurer::disable) // 禁用 HTTP Basic 认证，避免弹窗式登录
                // 禁用 X-Frame-Options 响应头，允许页面被嵌套到 iframe 中
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 限流过滤器
                // 验证码校验过滤器
                // 验证和解析过滤器
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}