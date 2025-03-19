package io.github.jeli01.kakao_bootcamp_community.board.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardsInnerData {
    private Long id;
    private String title;
    private Long like;
    private Long commentsCounts;
    private Long visitCounts;
    private LocalDateTime createDate;
    private String profileImage;
    private String writer;
}
