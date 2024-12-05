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

    private static String concatSessionKey(String username, SessionType sessionType){
        return sessionType.name() + '_' +username;
    }

    public void saveSession(String username, String refreshToken, SessionType sessionType){
        redisTemplate.opsForValue().set(concatSessionKey(username, sessionType), refreshToken, REFRESH_TOKEN_EXPIRY, TimeUnit.SECONDS);
    }

    public boolean validateRefreshToken(String username, String refreshToken, SessionType sessionType){
        String storedToken = redisTemplate.opsForValue().get(concatSessionKey(username, sessionType));
        return storedToken != null && storedToken.equals(refreshToken);
    }

    public long getRefreshTokenExpiry(String username, SessionType sessionType){
        Long expiry = redisTemplate.getExpire(concatSessionKey(username, sessionType), TimeUnit.SECONDS);
        return expiry < 0 ? 0 : expiry;
    }

    public void invalidateSession(String username, SessionType sessionType){
        redisTemplate.delete(concatSessionKey(username, sessionType));
    }
}
