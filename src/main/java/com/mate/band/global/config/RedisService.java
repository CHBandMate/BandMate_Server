package com.mate.band.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 데이터 저장
    public void saveToRedis(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 데이터 조회
    public String getFromRedis(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

}
