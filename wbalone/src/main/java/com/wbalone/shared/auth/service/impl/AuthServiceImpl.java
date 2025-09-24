package com.wbalone.shared.auth.service.impl;

import com.wbalone.core.security.model.AuthenticationToken;
import com.wbalone.shared.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthenticationToken login(String username, String password) {

        return null;
    }
}
