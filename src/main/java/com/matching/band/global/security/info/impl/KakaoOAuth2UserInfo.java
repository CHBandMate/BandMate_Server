package com.matching.band.global.security.info.impl;

import com.matching.band.global.security.constants.OAuthType;
import com.matching.band.global.security.info.OAuth2UserInfo;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class KakaoOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    @Override
    public String getUserIdentifier() {
        return String.valueOf(attributes.get(OAuthType.KAKAO.getIdentifier()));
    }
}
