package com.matching.band.global.security.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Auth {
    ACCESS_TYPE("access", "Access 토큰"),
    REFRESH_TYPE("refresh", "Refresh 토큰");
    private final String key;
    private final String title;
}
