package com.wbalone.shared.auth.service.impl;

import com.wbalone.system.mapper.UserMapper;
import com.wbalone.system.model.dto.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper; // 数据库操作接口

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查询用户认证信息（包含密码、角色等）
        UserAuthInfo userAuthInfo = userMapper.getUserAuthInfo(username);
        if (userAuthInfo == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if (userAuthInfo.getStatus() == 0) { // 假设0表示禁用
            throw new UsernameNotFoundException("用户已被禁用");
        }

        // 转换角色为Spring Security需要的权限格式（ROLE_前缀）
        var authorities = userAuthInfo.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // 封装为UserDetails对象（包含用户名、密码、权限）
        return new User(
                userAuthInfo.getUsername(),
                userAuthInfo.getPassword(), // 数据库中存储的BCrypt加密后的密码
                authorities
        );
    }
}