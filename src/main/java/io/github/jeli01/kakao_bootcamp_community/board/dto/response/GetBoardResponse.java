package io.github.jeli01.kakao_bootcamp_community.board.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBoardResponse {
    private Boolean isSuccess;
    private String message;
    private DataInBoard data;

    public GetBoardResponse() {
        this.isSuccess = true;
        this.message = "board get success";
    }

    @Getter
    @Setter
    public static class DataInBoard {
        private Long id;
        private String title;
        private String boardImage;
        private String profileImage;
        private String writer;
        private Long writerId;
        private LocalDateTime createDate;
        private String contents;
        private String originImageName;
        private Long like;
        private Long visitCount;
        private Long commentsCount;
        private List<CommentInnerDataInBoard> comments;

        @Getter
        @Setter
        public static class CommentInnerDataInBoard {
            private Long id;
            private String profileImage;
            private String writer;
            private Long writerId;
            private LocalDateTime createDate;
            private String content;
        }
    }
}
