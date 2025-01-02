package com.mate.band.global.security.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
