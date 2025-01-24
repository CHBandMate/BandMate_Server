package com.mate.band.global.security.info.impl;

import com.mate.band.global.security.constants.OAuthType;
import com.mate.band.global.security.info.OAuth2UserInfo;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Kakao OAuth2 회원 객체
 * @author : 최성민
 * @since : 2024-12-18
 * @version : 1.0
 */
@AllArgsConstructor
public class KakaoOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    @Override
    public String getUserIdentifier() {
        return String.valueOf(attributes.get(OAuthType.KAKAO.getIdentifier()));
    }
}
