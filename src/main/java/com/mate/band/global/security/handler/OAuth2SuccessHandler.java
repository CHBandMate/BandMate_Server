package com.mate.band.global.security.handler;

import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.global.config.RedisService;
import com.mate.band.global.security.domain.UserPrincipal;
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

@Configuration
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final String AUTH_URL;
    private final RedisService redisService;

    public OAuth2SuccessHandler(@Value("${url.client}") String BASE_URL,
                                @Value("${url.path.auth}") String AUTH_URL,
                                RedisService redisService) {
        this.AUTH_URL = BASE_URL + AUTH_URL;
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
        // 임시 코드 생성
        String authTempCode = "";
        try {
            authTempCode = redisService.saveAuthTempCode(user.getId());
        } catch (RedisConnectionFailureException e) {
            log.error("Redis 연결 실패: {}", e.getLocalizedMessage());
        } catch (SerializationException e) {
            log.error("Redis 직렬화 오류: {}", e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("Redis 작업 중 알 수 없는 오류 발생: {}", e.getLocalizedMessage());
        }

        return UriComponentsBuilder.fromUriString(AUTH_URL)
                .queryParam("identifier", user.getId())
                .queryParam("code", authTempCode)
                .build()
                .toUriString();
    }

}
