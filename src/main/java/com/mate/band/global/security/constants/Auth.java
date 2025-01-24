package com.mate.band.global.security.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 인증, 토큰 관련 ENUM
 * @author : 최성민
 * @since : 2024-12-22
 * @version : 1.0
 */
@AllArgsConstructor
@Getter
public enum Auth {
    ACCESS_TYPE("access"),
    REFRESH_TYPE("refresh"),
    ACCESS_HEADER("Authorization"),
    REFRESH_HEADER("Refresh"),
    ACCESS_PREFIX("Bearer ")
    ;

    private final String value;
}
