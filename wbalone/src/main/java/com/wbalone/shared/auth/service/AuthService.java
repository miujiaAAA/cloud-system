package com.wbalone.shared.auth.service;

import com.wbalone.core.security.model.AuthenticationToken;

public interface AuthService {
    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    AuthenticationToken login(String username, String password);
}
