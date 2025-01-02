package com.mate.band.global.security.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisKey {
    REFRESH_TOKEN("RefreshToken");
    private final String prefix;

    public static String generateKey(RedisKey type, String identifier) {
        return String.format("%s:%s", type.getPrefix(), identifier);
    }
}
