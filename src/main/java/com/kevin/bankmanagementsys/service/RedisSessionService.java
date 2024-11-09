package com.kevin.bankmanagementsys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisSessionService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60;

    public void saveSession(String sessionId, String refreshToken){
        redisTemplate.opsForValue().set(sessionId, refreshToken, REFRESH_TOKEN_EXPIRY, TimeUnit.SECONDS);
    }

    public boolean validateRefreshToken(String sessionId, String refreshToken){
        String storedToken = redisTemplate.opsForValue().get(sessionId);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    public void invalidateSession(String sessionId){
        redisTemplate.delete(sessionId);
    }
}
