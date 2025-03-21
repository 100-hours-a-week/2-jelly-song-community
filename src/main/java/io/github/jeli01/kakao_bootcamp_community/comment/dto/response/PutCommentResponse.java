package io.github.jeli01.kakao_bootcamp_community.comment.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutCommentResponse {
    private Boolean isSuccess;
    private String message;

    public PutCommentResponse() {
        this.isSuccess = true;
        this.message = "comment post success";
    }
}
