package com.wbalone.shared.auth.controller;

import cn.hutool.core.bean.BeanUtil;
import com.wbalone.core.security.model.AuthenticationToken;
import com.wbalone.core.security.utils.JwtTokenProvider;
import com.wbalone.result.Result;
import com.wbalone.shared.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final AuthService authService;
    @PostMapping("/login")
    public Result<AuthenticationToken> login(@RequestBody LoginRequest loginRequest) {

        AuthenticationToken authenticationToken = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return Result.success(authenticationToken);
    }

    // 登录请求参数类
    public static class LoginRequest {
        private String username;
        private String password;

        // getter和setter
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}