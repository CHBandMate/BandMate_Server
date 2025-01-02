package com.mate.band.global.config;

import com.mate.band.global.security.constants.RedisKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final String EXPIRED_TYPE;        // 토큰 만료 시간 타입
    private final long ACCESS_EXPIRED_TIME;   // AccessToken 만료 시간
    private final long REFRESH_EXPIRED_TIME;  // RefreshToken 만료 시간
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(@Value("${jwt.expired.type}") String EXPIRED_TYPE,
                        @Value("${jwt.expired.access}") long ACCESS_EXPIRED_TIME,
                        @Value("${jwt.expired.refresh}") long REFRESH_EXPIRED_TIME,
                        RedisTemplate<String, Object> redisTemplate) {
        this.ACCESS_EXPIRED_TIME = ACCESS_EXPIRED_TIME;
        this.REFRESH_EXPIRED_TIME = REFRESH_EXPIRED_TIME;
        this.EXPIRED_TYPE = EXPIRED_TYPE;
        this.redisTemplate = redisTemplate;
    }

    // 데이터 저장
    public void saveToRedis(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 데이터 조회
    public String getFromRedis(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void saveRefreshToken(long userNo, String refreshToken) {
        TimeUnit expiredType;
        switch (EXPIRED_TYPE) {
            case "SECOND" -> expiredType = TimeUnit.SECONDS;
            case "HOUR" -> expiredType = TimeUnit.HOURS;
            case "DATE" -> expiredType = TimeUnit.DAYS;
            default -> expiredType = TimeUnit.MINUTES;
        }

        String key = RedisKey.generateKey(RedisKey.REFRESH_TOKEN, String.valueOf(userNo));
        redisTemplate.opsForValue().set(key, refreshToken, REFRESH_EXPIRED_TIME, expiredType);
    }

    public String getRefreshToken(long userNo) {
        String key = RedisKey.generateKey(RedisKey.REFRESH_TOKEN, String.valueOf(userNo));
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(long userNo) {
        String key = RedisKey.generateKey(RedisKey.REFRESH_TOKEN, String.valueOf(userNo));
        redisTemplate.delete(key);
    }

}
