package com.mate.band.global.config;

import com.mate.band.global.security.constants.RedisKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final String EXPIRED_TYPE;        // 토큰 만료 시간 타입
    private final long REFRESH_EXPIRED_TIME;  // RefreshToken 만료 시간
    private final long AUTH_TEMP_CODE_EXPIRED_TIME;  // 임시 코드 만료 시간
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(@Value("${jwt.expired.type}") String EXPIRED_TYPE,
                        @Value("${jwt.expired.refresh}") long REFRESH_EXPIRED_TIME,
                        @Value("${jwt.expired.authTemp}") long AUTH_TEMP_CODE_EXPIRED_TIME,
                        RedisTemplate<String, Object> redisTemplate) {
        this.EXPIRED_TYPE = EXPIRED_TYPE;
        this.REFRESH_EXPIRED_TIME = REFRESH_EXPIRED_TIME;
        this.AUTH_TEMP_CODE_EXPIRED_TIME = AUTH_TEMP_CODE_EXPIRED_TIME;
        this.redisTemplate = redisTemplate;
    }

    public String saveAuthTempCode(long userId) {
        String authTempCode = UUID.randomUUID().toString();
        String key = RedisKey.generateKey(RedisKey.AUTH_TEMP_CODE, authTempCode);
        redisTemplate.opsForValue().set(key, String.valueOf(userId), AUTH_TEMP_CODE_EXPIRED_TIME, TimeUnit.SECONDS);
        return authTempCode;
    }

    public boolean validateAuthTempCode(String authTempCode) {
        return redisTemplate.opsForValue().get(RedisKey.generateKey(RedisKey.AUTH_TEMP_CODE, authTempCode)) != null;
    }

    public void deleteAuthTempCode(String authTempCode) {
        redisTemplate.delete(RedisKey.generateKey(RedisKey.AUTH_TEMP_CODE, authTempCode));
    }

    public void saveRefreshToken(long userId, String refreshToken) {
        TimeUnit expiredType;
        switch (EXPIRED_TYPE) {
            case "SECOND" -> expiredType = TimeUnit.SECONDS;
            case "HOUR" -> expiredType = TimeUnit.HOURS;
            case "DATE" -> expiredType = TimeUnit.DAYS;
            default -> expiredType = TimeUnit.MINUTES;
        }

        String key = RedisKey.generateKey(RedisKey.REFRESH_TOKEN, String.valueOf(userId));
        redisTemplate.opsForValue().set(key, refreshToken, REFRESH_EXPIRED_TIME, expiredType);
    }

    public String getRefreshToken(long userId) {
        String key = RedisKey.generateKey(RedisKey.REFRESH_TOKEN, String.valueOf(userId));
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(long userId) {
        String key = RedisKey.generateKey(RedisKey.REFRESH_TOKEN, String.valueOf(userId));
        redisTemplate.delete(key);
    }

}
