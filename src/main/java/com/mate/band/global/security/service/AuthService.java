package com.mate.band.global.security.service;

import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.repository.UserRepository;
import com.mate.band.global.config.RedisService;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.exception.ErrorCode;
import com.mate.band.global.security.constants.Auth;
import com.mate.band.global.security.dto.TokenRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final RedisService redisService;

    public void issueToken(HttpServletResponse response, TokenRequest tokenRequest) {
        String authTempCode = tokenRequest.authTempCode();

        if (redisService.validateAuthTempCode(authTempCode)) {
            redisService.deleteAuthTempCode(authTempCode);
            saveToken(response, tokenRequest.identifier());
        } else {
            throw new BusinessException(ErrorCode.NOT_EXIST_AUTH_TEMP_TOKEN);
        }
    }

    public void reissueToken(HttpServletResponse response, String refreshToken) {
        if (!JWTUtils.isValidToken(Auth.REFRESH_TYPE, refreshToken)) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_ALLOWED);
        }

        long userNo = Long.parseLong(JWTUtils.getSubjectFromToken(Auth.REFRESH_TYPE, refreshToken));
        String refreshTokenInRedis = redisService.getRefreshToken(userNo);

        if (refreshTokenInRedis.isEmpty()) {
            throw new BusinessException(ErrorCode.TOKEN_NUll);
        }

        if (!refreshToken.equals(refreshTokenInRedis)) {
            throw new BusinessException(ErrorCode.NOT_EXIST_REFRESH_TOKEN);
        }

        saveToken(response, userNo);
    }

    private void saveToken(HttpServletResponse response, long userNo) {
        UserEntity user = userRepository.findById(userNo).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        Map<String, String> tokenMap = JWTUtils.generateAuthenticatedTokens(user);
        String accessToken = tokenMap.get(Auth.ACCESS_TYPE.getValue());
        String refreshToken = tokenMap.get(Auth.REFRESH_TYPE.getValue());

        // RefreshToken Redis 저장
        // 에러 발생 해도 로그만 남기고 ATK는 발급 처리
        try {
            redisService.saveRefreshToken(user.getId(), refreshToken);
        } catch (RedisConnectionFailureException e) {
            log.error("Redis 연결 실패: {}", e.getLocalizedMessage());
        } catch (SerializationException e) {
            log.error("Redis 직렬화 오류: {}", e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("Redis 작업 중 알 수 없는 오류 발생: {}", e.getLocalizedMessage());
        }

        response.setHeader(Auth.ACCESS_HEADER.getValue(), Auth.ACCESS_PREFIX.getValue() + accessToken);
        response.setHeader(Auth.REFRESH_HEADER.getValue(), refreshToken);

        Authentication authentication = JWTUtils.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
