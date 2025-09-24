package com.wbalone.core.security.exception;

import com.wbalone.result.ResultCode;
import com.wbalone.util.ResponseUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                         AuthenticationException authException) throws IOException {
        if (authException instanceof BadCredentialsException) {
            // 用户名或密码错误
            ResponseUtils.writeErrMsg(response, ResultCode.USER_PASSWORD_ERROR);
        } else {
            // 令牌无效、过期等
            ResponseUtils.writeErrMsg(response, ResultCode.ACCESS_TOKEN_INVALID);
        }
    }
}