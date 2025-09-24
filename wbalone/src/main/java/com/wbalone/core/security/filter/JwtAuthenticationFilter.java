package com.wbalone.core.security.filter;

import com.wbalone.core.security.utils.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService; // 自定义用户详情服务

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 从请求头中获取令牌（格式：Bearer <token>）
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 截取Bearer后的令牌
            try {
                username = jwtTokenProvider.getUsernameFromToken(token); // 从令牌中获取用户名
            } catch (Exception e) {
                // 令牌无效（无需处理，后续会拦截）
            }
        }

        // 验证令牌并设置用户上下文
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtTokenProvider.validateToken(token)) { // 验证令牌有效性
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // 获取用户详情
                // 创建认证对象
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将认证信息存入安全上下文
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response); // 继续执行后续过滤器
    }
}