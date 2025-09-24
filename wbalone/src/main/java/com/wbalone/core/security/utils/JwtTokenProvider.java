package com.wbalone.core.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Data
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey; // 从配置文件读取密钥

    @Value("${jwt.expiration}")
    private long accessTokenExpiration; // 访问令牌有效期（毫秒）

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration; // 刷新令牌有效期（毫秒）

    private Key key;

    // 初始化：将密钥转换为JWT需要的Key对象
    @PostConstruct
    public void init() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes); // HS256算法要求密钥至少32位
    }

    // 生成访问令牌
    public String generateAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails.getUsername(), accessTokenExpiration);
    }

    // 生成刷新令牌
    public String generateRefreshToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails.getUsername(), refreshTokenExpiration);
    }

    // 通用令牌生成方法
    private String generateToken(String username, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username) // 用户名作为令牌主题
                .setIssuedAt(now) // 签发时间
                .setExpiration(expiryDate) // 过期时间
                .signWith(key, SignatureAlgorithm.HS256) // 签名算法
                .compact();
    }

    // 从令牌中获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 验证令牌有效性
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true; // 验证成功
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 令牌无效（过期、签名错误等）
        }
    }
}