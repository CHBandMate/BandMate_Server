package com.mate.band.global.security.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Redis Key 관리 ENUM
 * @author : 최성민
 * @since : 2025-01-02
 * @version : 1.0
 */
@Getter
@AllArgsConstructor
public enum RedisKey {
    AUTH_TEMP_CODE("AuthTempCode"),
    REFRESH_TOKEN("RefreshToken");
    private final String prefix;

    public static String generateKey(RedisKey type, String identifier) {
        return String.format("%s:%s", type.getPrefix(), identifier);
    }
}
