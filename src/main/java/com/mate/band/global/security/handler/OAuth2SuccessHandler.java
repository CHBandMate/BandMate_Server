package com.mate.band.global.security.handler;

import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.global.security.constants.Auth;
import com.mate.band.global.security.domain.UserPrincipal;
import com.mate.band.global.security.constants.Role;
import com.mate.band.global.security.service.JWTUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Configuration
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final String SIGNUP_URL;
    private final String MAIN_URL;

    public OAuth2SuccessHandler(@Value("${url.base}") String BASE_URL,
                                @Value("${url.path.signup}") String SIGN_UP_PATH,
                                @Value("${url.path.main}") String MAIN_URL) {
        this.SIGNUP_URL = BASE_URL + SIGN_UP_PATH;
        this.MAIN_URL = BASE_URL + MAIN_URL;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity oAuth2User = userPrincipal.getUser();
        String redirectUrl = getRedirectUrlByRole(oAuth2User);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String getRedirectUrlByRole(UserEntity user) {
        // 토큰 발급 로직 추가
        Map<String, String> tokenMap = JWTUtils.generateAuthenticatedTokens(user);
        return UriComponentsBuilder.fromUriString(user.getRole() == Role.NOT_REGISTERED ? SIGNUP_URL : MAIN_URL)
                .queryParam("access", tokenMap.get(Auth.ACCESS_TYPE.getKey()))
                .queryParam("refresh", tokenMap.get(Auth.REFRESH_TYPE.getKey()))
                .build()
                .toUriString();
    }
}
