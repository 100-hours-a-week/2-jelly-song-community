package io.github.jeli01.kakao_bootcamp_community.auth.repository;

import io.github.jeli01.kakao_bootcamp_community.auth.domain.RefreshToken;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {
    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);

    @Transactional
    void deleteByExpirationBefore(LocalDateTime currentTime);

}
