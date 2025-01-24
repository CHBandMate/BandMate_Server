package com.mate.band.global.security.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰 Claim 관리 ENUM
 * @author : 최성민
 * @since : 2024-12-30
 * @version : 1.0
 */
@AllArgsConstructor
@Getter
public enum Claim {
    TOKEN_TYPE("tokenType"),
    OAUTH_ID("oauthId"),
    OAUTH_TYPE("oauthType"),
    ROLE("role");
    private String key;
}
