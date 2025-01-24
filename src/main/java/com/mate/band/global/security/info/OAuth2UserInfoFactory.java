package com.mate.band.global.security.info;

import com.mate.band.global.security.constants.OAuthType;
import com.mate.band.global.security.info.impl.KakaoOAuth2UserInfo;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

/**
 * OAuth 플랫폼 별 사용자 객체 생성 클래스
 * @author : 최성민
 * @since : 2024-12-18
 * @version : 1.0
 */
public class OAuth2UserInfoFactory {

    /**
     * OAuth 플랫폼 별 사용자 객체를 반환
     * @param oauthType  OAuth 플랫폼
     * @param attributes OAuth 제공 회원 정보
     * @return OAuth2UserInfo
     */
    public static OAuth2UserInfo getOAuth2UserInfo(OAuthType oauthType, Map<String, Object> attributes) {
        switch (oauthType) {
            case KAKAO -> {return new KakaoOAuth2UserInfo(attributes);}
        }
        throw new OAuth2AuthenticationException("INVALID PROVIDER TYPE");
    }
}
