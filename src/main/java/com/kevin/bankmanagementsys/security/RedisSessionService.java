package com.kevin.bankmanagementsys.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisSessionService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60;

    public void saveSession(String username, String refreshToken){
        redisTemplate.opsForValue().set(username, refreshToken, REFRESH_TOKEN_EXPIRY, TimeUnit.SECONDS);
    }

    public boolean validateRefreshToken(String username, String refreshToken){
        String storedToken = redisTemplate.opsForValue().get(username);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    public long getRefreshTokenExpiry(String username){
        Long expiry = redisTemplate.getExpire(username, TimeUnit.SECONDS);
        return expiry < 0 ? 0 : expiry;
    }

    public void invalidateSession(String username){
        redisTemplate.delete(username);
    }
}
