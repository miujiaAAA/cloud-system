package com.wbalone.shared.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wbalone.system.mapper.UserMapper;
import com.wbalone.system.model.dto.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户认证信息
        UserAuthInfo userAuthInfo = userMapper.getUserAuthInfo(username);
        if (userAuthInfo == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        
        // 检查用户状态
        if (userAuthInfo.getStatus() == 0) {
            throw new UsernameNotFoundException("用户已被禁用");
        }

        // 转换角色权限
        Set<String> roles = userAuthInfo.getRoles();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (CollUtil.isNotEmpty(roles)) {
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }

        // 构建UserDetails对象
        return org.springframework.security.core.userdetails.User
                .withUsername(userAuthInfo.getUsername())
                .password(userAuthInfo.getPassword())
                .authorities(authorities)
                .build();
    }
}