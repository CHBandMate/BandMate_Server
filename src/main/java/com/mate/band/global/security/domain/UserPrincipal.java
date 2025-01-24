package com.mate.band.global.security.domain;

import com.mate.band.domain.user.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Security에서 사용할 User의 Principal 객체
 * @author : 최성민
 * @since : 2024-12-18
 * @version : 1.0
 */
@Getter
public class UserPrincipal implements OAuth2User {

    private UserEntity user;
    private String nameAttributeKey;
    private Map<String, Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(UserEntity user) {
        this.user = user;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()));
    }

    public UserPrincipal(UserEntity user, Map<String, Object> attributes, String nameAttributeKey) {
        this.user = user;
        this.nameAttributeKey = nameAttributeKey;
        this.attributes = attributes;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()));
    }

    @Override
    public String getName() {
        return user.getOauthId();
    }

}
