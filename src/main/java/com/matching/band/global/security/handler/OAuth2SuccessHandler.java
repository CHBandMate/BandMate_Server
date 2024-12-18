package com.matching.band.global.security.handler;

import com.matching.band.domain.user.entity.UserEntity;
import com.matching.band.domain.user.repository.UserRepository;
import com.matching.band.global.api.constant.ErrorConstant;
import com.matching.band.global.security.constants.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Configuration
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final String SIGNUP_URL;
    private final String MAIN_URL;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(@Value("${url.base}") String BASE_URL,
                                @Value("${url.path.signup}") String SIGN_UP_PATH,
                                @Value("${url.path.main}") String MAIN_URL,
                                UserRepository userRepository) {
        this.userRepository = userRepository;
        this.SIGNUP_URL = BASE_URL + SIGN_UP_PATH;
        this.MAIN_URL = BASE_URL + MAIN_URL;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String identifier = oAuth2User.getName();

        UserEntity user = userRepository.findByOauthId(identifier)
                .orElseThrow(() -> new RuntimeException(ErrorConstant.NOT_FOUND.getErrorMessage()));

        String redirectUrl = getRedirectUrlByRole(user.getRole(), identifier);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String getRedirectUrlByRole(Role role, String identifier) {
        return UriComponentsBuilder.fromUriString(role == Role.NOT_REGISTERED ? SIGNUP_URL : MAIN_URL)
                .queryParam("identifier", identifier)
                .build()
                .toUriString();
    }
}
