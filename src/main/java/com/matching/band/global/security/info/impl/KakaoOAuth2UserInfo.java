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
        // kakao_account
        attributes.get(OAuthType.KAKAO.getAttributeKey());
        return (String) attributes.get(OAuthType.KAKAO.getIdentifier());
    }
}
