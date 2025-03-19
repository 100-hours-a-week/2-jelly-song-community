package io.github.jeli01.kakao_bootcamp_community.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String refresh;
    private LocalDateTime expiration;

    public RefreshToken(String email, String refresh, LocalDateTime expiration) {
        this.email = email;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
