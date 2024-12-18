package com.matching.band.global.security.domain;

import com.matching.band.domain.user.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

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
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()));
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

    /**
     * OAuth2User method implements
     */
    @Override
    public String getName() {
        return user.getOauthId();
    }

}
