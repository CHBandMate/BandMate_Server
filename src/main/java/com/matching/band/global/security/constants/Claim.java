package com.matching.band.global.security.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Claim {
    TOKEN_TYPE("tokenType"),
    OAUTH_ID("oauthId"),
    OAUTH_TYPE("oauthType"),
    ROLE("role");
    private String key;
}
