package io.github.jeli01.kakao_bootcamp_community.auth.repository;

import io.github.jeli01.kakao_bootcamp_community.auth.domain.RefreshToken;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);

    void deleteByExpirationBefore(LocalDateTime currentTime);

}
