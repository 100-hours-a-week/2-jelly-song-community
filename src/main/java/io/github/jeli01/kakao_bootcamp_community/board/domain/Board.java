package io.github.jeli01.kakao_bootcamp_community.board.domain;

import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String boardImage;

    @Column(columnDefinition = "TEXT")
    private String boardImageOriginName;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private User writer;

    @Column(nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long visitCount;

    @Column(nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long likeCount;

    @Column(nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long commentCount;

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    private LocalDateTime deleteDate;

    public Board(String title, String content, String boardImage, String boardImageOriginName, User writer,
                 Long visitCount, Long likeCount, Long commentCount, LocalDateTime createDate, LocalDateTime updateDate,
                 LocalDateTime deleteDate) {
        this.title = title;
        this.content = content;
        this.boardImage = boardImage;
        this.boardImageOriginName = boardImageOriginName;
        this.writer = writer;
        this.visitCount = visitCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.deleteDate = deleteDate;
    }

    public void changeBoard(String title, String content, String boardImage, String boardImageOriginName) {
        this.title = title;
        this.content = content;
        this.boardImage = boardImage;
        this.boardImageOriginName = boardImageOriginName;
        this.updateDate = LocalDateTime.now();
    }

    public void softDelete() {
        this.deleteDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    public void plusVisitCount() {
        this.visitCount += 1;
        this.updateDate = LocalDateTime.now();
    }

    public void plusLikeCount() {
        this.likeCount += 1;
        this.updateDate = LocalDateTime.now();
    }

    public void plusCommentCount() {
        this.commentCount += 1;
        this.updateDate = LocalDateTime.now();
    }

    public void minusLikeCount() {
        this.likeCount -= 1;
        this.updateDate = LocalDateTime.now();
    }

    public void minusCommentCount() {
        this.commentCount -= 1;
        this.updateDate = LocalDateTime.now();
    }
}
