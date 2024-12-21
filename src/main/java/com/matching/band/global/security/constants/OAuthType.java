package com.matching.band.global.security.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OAuthType {
    KAKAO("kakao_account", "id", "id"),
    NAVER("response", "id", "email"),
    GOOGLE(null, "sub", "email");

    private final String attributeKey;
    private final String providerCode;
    private final String identifier;

    public static OAuthType from(String provider) {
        String upperCastedProvider = provider.toUpperCase();

        return Arrays.stream(OAuthType.values())
                .filter(item -> item.name().equals(upperCastedProvider))
                .findFirst()
                .orElseThrow();
    }
}
