package io.github.jeli01.kakao_bootcamp_community.auth.scheduler;

import io.github.jeli01.kakao_bootcamp_community.auth.service.RefreshTokenService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenScheduler {
    private final RefreshTokenService refreshTokenService;

    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredTokens() {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("Expired refresh tokens are being deleted... Current Time: {}", currentTime);
        refreshTokenService.deleteByExpirationBefore(LocalDateTime.now());
        log.info("Expired refresh tokens deleted successfully.");
    }
}
