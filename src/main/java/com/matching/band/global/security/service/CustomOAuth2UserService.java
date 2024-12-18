package com.matching.band.global.security.service;

import com.matching.band.domain.user.entity.UserEntity;
import com.matching.band.domain.user.repository.UserRepository;
import com.matching.band.global.security.constants.OAuthType;
import com.matching.band.global.security.constants.Role;
import com.matching.band.global.security.domain.UserPrincipal;
import com.matching.band.global.security.info.OAuth2UserInfo;
import com.matching.band.global.security.info.OAuth2UserInfoFactory;
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

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 로그인 진행 시 키가 되는 필드값. Primary Key와 같은 의미.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 서비스를 구분하는 코드 ex) Github, Naver
        String providerCode = userRequest.getClientRegistration().getRegistrationId();

        // 어떤 소셜로그인을 사용했는지 반환받는 정적 메서드
        OAuthType oauthType = OAuthType.from(providerCode);

        // 소셜쪽에서 전달받은 값들을 Map 형태로 받음
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 소셜로그인의 종류에 상관없이 사용자의 식별자를 받아오는 코드
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oauthType, attributes);
        String oauthId = oAuth2UserInfo.getUserIdentifier();

        UserEntity user = getUser(oauthId, oauthType);

        // Security context에 저장할 객체 생성
        return new UserPrincipal(user, attributes, userNameAttributeName);
    }

    private UserEntity getUser(String oauthId, OAuthType oauthType) {
        Optional<UserEntity> optionalUser = userRepository.findByOAuthInfo(oauthId, oauthType);
        if (optionalUser.isEmpty()) {
            UserEntity unregisteredUser = UserEntity.builder()
                    .oauthId(oauthId)
                    .role(Role.NOT_REGISTERED)
                    .oauthType(oauthType)
                    .build();
            return userRepository.save(unregisteredUser);
        }
        return optionalUser.get();
    }
}