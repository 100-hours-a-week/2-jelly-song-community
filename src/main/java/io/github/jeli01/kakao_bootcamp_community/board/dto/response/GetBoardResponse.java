package io.github.jeli01.kakao_bootcamp_community.board.dto.response;

import io.github.jeli01.kakao_bootcamp_community.util.contant.ErrorMessage;
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

    public void changeClientError() {
        isSuccess = false;
        message = ErrorMessage.CLIENT_ERROR_MESSAGE;
    }

    public void changeServerError() {
        isSuccess = false;
        message = ErrorMessage.SERVER_ERROR_MESSAGE;
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
