package io.github.jeli01.kakao_bootcamp_community.auth.service;

import io.github.jeli01.kakao_bootcamp_community.auth.domain.RefreshToken;
import io.github.jeli01.kakao_bootcamp_community.auth.repository.RefreshTokenRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteByRefresh(String refresh) {
        refreshTokenRepository.deleteByRefresh(refresh);
    }

    public Boolean existsByRefresh(String refresh) {
        return refreshTokenRepository.existsByRefresh(refresh);
    }

    public void deleteByExpirationBefore(LocalDateTime currentTime) {
        refreshTokenRepository.deleteByExpirationBefore(currentTime);
    }
}
