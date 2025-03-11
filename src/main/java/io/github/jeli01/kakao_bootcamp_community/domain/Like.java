package io.github.jeli01.kakao_bootcamp_community.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(name="likes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "post_id"})})
public class Like {
    @Id @GeneratedValue
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Post post;

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;
}
