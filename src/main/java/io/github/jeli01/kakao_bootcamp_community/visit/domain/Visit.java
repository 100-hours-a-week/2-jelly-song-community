package io.github.jeli01.kakao_bootcamp_community.visit.domain;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Visit {
    @Id @GeneratedValue
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Board board;

    @Column(nullable = false)
    private Long count;

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;
}
