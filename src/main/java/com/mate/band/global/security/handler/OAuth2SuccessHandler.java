package com.mate.band.global.security.handler;

import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.global.config.RedisService;
import com.mate.band.global.security.constants.Auth;
import com.mate.band.global.security.constants.Role;
import com.mate.band.global.security.domain.UserPrincipal;
import com.mate.band.global.security.service.JWTUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Configuration
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final String SIGNUP_URL;
    private final String MAIN_URL;
    private final RedisService redisService;

    public OAuth2SuccessHandler(@Value("${url.base}") String BASE_URL,
                                @Value("${url.path.signup}") String SIGN_UP_PATH,
                                @Value("${url.path.main}") String MAIN_URL,
                                RedisService redisService) {
        this.SIGNUP_URL = BASE_URL + SIGN_UP_PATH;
        this.MAIN_URL = BASE_URL + MAIN_URL;
        this.redisService = redisService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity oAuth2User = userPrincipal.getUser();
        String redirectUrl = getRedirectUrlByRole(oAuth2User);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String getRedirectUrlByRole(UserEntity user) {
        Map<String, String> tokenMap = JWTUtils.generateAuthenticatedTokens(user);

        // RefreshToken RedisKey 저장
        try {
            redisService.saveRefreshToken(user.getUserNo(), tokenMap.get(Auth.REFRESH_TYPE.getKey()));
        } catch (RedisConnectionFailureException e) {
            log.error("Redis 연결 실패: {}", e.getLocalizedMessage());
        } catch (SerializationException e) {
            log.error("Redis 직렬화 오류: {}", e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("Redis 작업 중 알 수 없는 오류 발생: {}", e.getLocalizedMessage());
        }

        return UriComponentsBuilder.fromUriString(user.getRole() == Role.NOT_REGISTERED ? SIGNUP_URL : MAIN_URL)
                .queryParam("access", tokenMap.get(Auth.ACCESS_TYPE.getKey()))
                .queryParam("refresh", tokenMap.get(Auth.REFRESH_TYPE.getKey()))
                .build()
                .toUriString();
    }
}
