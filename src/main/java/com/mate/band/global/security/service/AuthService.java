package com.mate.band.global.security.service;

import com.mate.band.global.security.dto.TokenRequest;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.repository.UserRepository;
import com.mate.band.global.config.RedisService;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.security.constants.Auth;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.serializer.SerializationException;
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
            UserEntity user = userRepository.findById(tokenRequest.identifier()).orElseThrow(() -> new BusinessException("존재하지 않는 회원"));
            Map<String, String> tokenMap = JWTUtils.generateAuthenticatedTokens(user);

            // RefreshToken Redis 저장
            try {
                redisService.saveRefreshToken(user.getUserNo(), tokenMap.get(Auth.REFRESH_TYPE.getValue()));
            } catch (RedisConnectionFailureException e) {
                log.error("Redis 연결 실패: {}", e.getLocalizedMessage());
            } catch (SerializationException e) {
                log.error("Redis 직렬화 오류: {}", e.getLocalizedMessage());
            } catch (Exception e) {
                log.error("Redis 작업 중 알 수 없는 오류 발생: {}", e.getLocalizedMessage());
            }

            response.setHeader(Auth.ACCESS_HEADER.getValue(), Auth.ACCESS_PREFIX.getValue() + tokenMap.get(Auth.ACCESS_TYPE.getValue()));
        } else {
            throw new BusinessException("존재하지 않는 인증코드");
        }
    }


}
