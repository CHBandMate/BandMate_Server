package com.mate.band.global.security.service;

import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.repository.UserRepository;
import com.mate.band.global.config.RedisService;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.exception.ErrorCode;
import com.mate.band.global.security.constants.Auth;
import com.mate.band.global.security.dto.TokenRequestDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * PackageName :
 * FileName : Auth service
 * Author :
 * Date : 01.24.2025
 * Description :
 *
 * @author : 최성민
 * @version : 1.0
 * @since : 2025-01-02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RedisService redisService;

    /**
     * JWT를 발급하고 Redis에 저장한다.
     * @param response     HttpServletResponse
     * @param tokenRequest 토큰 발급 요청 데이터
     */
    public void issueToken(HttpServletResponse response, TokenRequestDTO tokenRequest) {
        String authTempCode = tokenRequest.authTempCode();

        if (redisService.validateAuthTempCode(authTempCode)) {
            redisService.deleteAuthTempCode(authTempCode);
            saveToken(response, tokenRequest.identifier());
        } else {
            throw new BusinessException(ErrorCode.NOT_EXIST_AUTH_TEMP_TOKEN);
        }
    }

    /**
     * RefreshToken으로 JWT를 재발급하고 Redis에 저장한다.
     * @param response     HttpServletResponse
     * @param refreshToken Refresh 토큰값
     */
    public void reissueToken(HttpServletResponse response, String refreshToken) {
        if (!JWTUtils.isValidToken(Auth.REFRESH_TYPE, refreshToken)) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_ALLOWED);
        }

        long userId = Long.parseLong(JWTUtils.getSubjectFromToken(Auth.REFRESH_TYPE, refreshToken));
        String refreshTokenInRedis = redisService.getRefreshToken(userId);

        if (refreshTokenInRedis == null) {
            throw new BusinessException(ErrorCode.TOKEN_NUll);
        }

        if (!refreshToken.equals(refreshTokenInRedis)) {
            throw new BusinessException(ErrorCode.NOT_EXIST_REFRESH_TOKEN);
        }

        saveToken(response, userId);
    }

    /**
     * Redis에 RefreshToken을 저장하고 AccessToken을 Response Header 담는다.</br>
     * RefreshToken 발급이 실패해도 AccessToken 재발급은 진행한다.
     * @param response     HttpServletResponse
     * @param userId 회원Id
     */
    private void saveToken(HttpServletResponse response, long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
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

    /**
     * Redis에서 회원의 RefreshToken을 제거하고 로그아웃을 처리한다.
     * @param user @AuthUser
     */
    public void logout(UserEntity user) {
        redisService.deleteRefreshToken(user.getId());
    }

}
