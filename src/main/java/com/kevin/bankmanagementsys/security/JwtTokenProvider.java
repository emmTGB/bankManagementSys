package com.kevin.bankmanagementsys.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.hibernate.bytecode.internal.bytebuddy.PrivateAccessorException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtTokenProvider {
    // 你可以更改此密钥
    private static final String SECRET_KEY = "thisIsAVeryLongSecretKeyThatIs256BitsLong12345";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 60*60*1000;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 7*24*60*60*1000;

    private SecretKey getSigningKey() {
        // 使用 Base64 解码，密钥长度应该是 256 位（32 字节）
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
    }

    // 生成 JWT Token
    public String createToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))  // 设置 1 小时过期时间
                .signWith(getSigningKey())
                .compact();
    }

    // 生成 Access Token
    public String createAccessToken(String username, String sessionId) {
        return Jwts.builder()
                .subject(username)
                .claim("sessionId", sessionId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // 生成 Refresh Token
    public String createRefreshToken(String username, String sessionId) {
        return Jwts.builder()
                .subject(username)
                .claim("sessionId", sessionId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // 验证 JWT Token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())  // 使用字节数组密钥
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 从 Token 中获取用户名
    public String getUsernameFromToken(String token) {
            return Jwts.parser()
                    .verifyWith(getSigningKey())  // 使用字节数组密钥
                    .build()
                    .parseSignedClaims(token)
                    .getBody()
                    .getSubject();
    }

    // 从 Token 中获取会话 ID
    public String getSessionIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // 使用字节数组密钥
                .build()
                .parseSignedClaims(token)
                .getBody()
                .get("sessionId", String.class);
    }
}