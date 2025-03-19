package io.github.jeli01.kakao_bootcamp_community.like.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikeResponse {
    private Boolean isSuccess;
    private String message;

    public PostLikeResponse() {
        this.isSuccess = true;
        this.message = "like post success";
    }
}
