package io.github.jeli01.kakao_bootcamp_community.user.domain;

import jakarta.persistence.Column;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String nickname;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String profileImage;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'USER'")
    private String role;

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    private LocalDateTime deleteDate;

    public User(String email, String password, String nickname, String profileImage, String role,
                LocalDateTime createDate, LocalDateTime updateDate, LocalDateTime deleteDate) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.deleteDate = deleteDate;
    }

    public void changeUserBasic(String profileImage, String nickname) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.updateDate = LocalDateTime.now();
    }

    public void changePassword(String password) {
        this.password = password;
        this.updateDate = LocalDateTime.now();
    }

    public void delete() {
        deleteDate = LocalDateTime.now();
    }

    public User(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
