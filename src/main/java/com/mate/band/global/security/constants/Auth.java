package com.mate.band.global.security.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Auth {
    ACCESS_TYPE("access", "Access 타입"),
    REFRESH_TYPE("refresh", "Refresh 타입"),
    ACCESS_HEADER("Authorization", "헤더 Access 토큰"),
    REFRESH_HEADER("Authorization-Refresh", "헤더 Refresh 토큰")
    ;

    private final String key;
    private final String desc;
}
