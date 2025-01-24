package com.mate.band.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * OAuth2 인증 실패 Handler
 * @author : 최성민
 * @since : 2024-12-18
 * @version : 1.0
 */
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final String LOGIN_URL;

    public OAuth2FailureHandler(@Value("${url.client}") String BASE_URL, @Value("${url.path.login}") String LOGIN_PATH) {
        this.LOGIN_URL = BASE_URL + LOGIN_PATH;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String redirectUrl = UriComponentsBuilder.fromUriString(LOGIN_URL)
                .queryParam("error", exception.getMessage())
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

}
