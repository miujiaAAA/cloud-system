package com.wbalone.shared.auth.service.impl;

import com.wbalone.core.security.model.AuthenticationToken;
import com.wbalone.core.security.utils.JwtTokenProvider;
import com.wbalone.shared.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthenticationToken login(String username, String password) {
        // 1. 认证用户名和密码
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 2. 生成令牌
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // 3. 构建响应
        return AuthenticationToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn((int) (jwtTokenProvider.getAccessTokenExpiration() / 1000)) // 转换为秒
                .build();
    }
}