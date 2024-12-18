package com.matching.band.global.security.info;

import com.matching.band.global.security.constants.OAuthType;
import com.matching.band.global.security.info.impl.KakaoOAuth2UserInfo;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(OAuthType oauthType, Map<String, Object> attributes) {
        switch (oauthType) {
            case KAKAO -> {return new KakaoOAuth2UserInfo((Map<String, Object>) attributes.get(OAuthType.KAKAO.getAttributeKey()));}
        }
        throw new OAuth2AuthenticationException("INVALID PROVIDER TYPE");
    }
}
