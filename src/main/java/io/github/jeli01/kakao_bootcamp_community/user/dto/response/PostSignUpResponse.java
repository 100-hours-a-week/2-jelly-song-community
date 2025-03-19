package io.github.jeli01.kakao_bootcamp_community.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSignUpResponse {
    private Boolean isSuccess;
    private String message;

    public PostSignUpResponse() {
        this.isSuccess = true;
        this.message = "user post success";
    }
}
