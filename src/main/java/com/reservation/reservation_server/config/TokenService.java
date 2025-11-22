package com.reservation.reservation_server.config;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final RedisService redisService;

    public TokenService(RedisService redisService) {
        this.redisService = redisService;
    }

    // Refresh Token 저장
    public void storeRefreshToken(Long userId, String refreshToken) {
        String key = "refresh:" + userId;
        long refreshTokenTTL = 7 * 24 * 60 * 60; // 7일

        redisService.save(key, refreshToken, refreshTokenTTL);
    }

    // Redis에서 RefreshToken 검증
    public boolean validateRefreshToken(Long userId, String refreshToken) {
        Object storedToken = redisService.get("refresh:" + userId);
        if (storedToken == null) return false;

        return storedToken.equals(refreshToken);
    }
}
