package com.foodles.auth.service;

import com.foodles.auth.exception.RefreshTokenException;
import com.foodles.auth.model.RefreshToken;
import com.foodles.auth.repository.RefreshTokenRepository;
import com.foodles.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    @Value("${security.refresh-token.expiration-time}")
    private long jwtExpiration;

    public RefreshToken createRefreshToken(String username) {
        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(userRepository.findByEmail(username).orElseThrow(() -> new RefreshTokenException("Invalid token")))
                        .token(UUID.randomUUID().toString())
                        .expiry(Instant.now().plusMillis(jwtExpiration))
                        .build()
        );
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenException("Refresh token not found!"));
    }

    public RefreshToken verifyToken(RefreshToken token) {
        if (token.getExpiry().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token +" Refresh token expired!");
        }
        return refreshTokenRepository.save(
                RefreshToken.builder()
                .user(token.getUser())
                .token(UUID.randomUUID().toString())
                .expiry(Instant.now().plusMillis(jwtExpiration))
                .build()
        );
    }

    public void deleteByUserId(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
