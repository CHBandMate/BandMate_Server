package com.mate.band.global.security.service;

import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.repository.UserRepository;
import com.mate.band.global.security.constants.OAuthType;
import com.mate.band.global.security.constants.Role;
import com.mate.band.global.security.domain.UserPrincipal;
import com.mate.band.global.security.info.OAuth2UserInfo;
import com.mate.band.global.security.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * OAuth2 로그인에 성공하면 사용자 정보를 가져와 처리</br>
 * (OAuth2 인증 성공 -> Authentication 객체 생성 전 단계)
 * @author : 최성민
 * @since : 2024-12-18
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // provider로 부터 전달 된 회원 정보
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 사용자 정보 식별 key (google: sub, kakao: id ...)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // provider 식별자(Kakao, Google, Naver...), OAuthType ENUM 생성
        String providerCode = userRequest.getClientRegistration().getRegistrationId();
        OAuthType oauthType = OAuthType.from(providerCode);

        // provider 별 회원 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oauthType, attributes);
        String oauthId = oAuth2UserInfo.getUserIdentifier(); // provider 사용자 고유 식별 ID (kakao: id, github: id ...)
        UserEntity user = getUser(oauthId, oauthType);
        return new UserPrincipal(user, attributes, userNameAttributeName);
    }

    private UserEntity getUser(String oauthId, OAuthType oauthType) {
        Optional<UserEntity> optionalUser = userRepository.findByOAuthInfo(oauthId, oauthType);
        if (optionalUser.isEmpty()) {
            UserEntity unregisteredUser = UserEntity.builder()
                    .oauthType(oauthType)
                    .oauthId(oauthId)
                    .role(Role.NOT_REGISTERED)
                    .deleteYn(false)
                    .exposeYn(false)
                    .build();
            return userRepository.save(unregisteredUser);
        }
        return optionalUser.get();
    }
}