package io.github.jeli01.kakao_bootcamp_community.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUserResponse {
    private Boolean isSuccess;
    private String message;

    public DeleteUserResponse() {
        this.isSuccess = true;
        this.message = "user delete success";
    }
}
