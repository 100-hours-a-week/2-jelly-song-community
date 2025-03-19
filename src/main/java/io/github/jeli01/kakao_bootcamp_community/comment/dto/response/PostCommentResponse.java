package io.github.jeli01.kakao_bootcamp_community.comment.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentResponse {
    private Boolean isSuccess;
    private String message;

    public PostCommentResponse() {
        this.isSuccess = true;
        this.message = "comment post success";
    }
}
