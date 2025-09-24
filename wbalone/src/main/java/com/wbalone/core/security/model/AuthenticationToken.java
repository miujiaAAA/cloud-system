package com.wbalone.core.security.model;

import lombok.Builder;
import lombok.Data;

/**
 * 认证令牌响应对象
 *
 * @author Ray.Hao
 * @since 0.0.1
 */
@Data
@Builder
public class AuthenticationToken {

    private String tokenType;

    private String accessToken;

    private String refreshToken;

    private Integer expiresIn;

}
