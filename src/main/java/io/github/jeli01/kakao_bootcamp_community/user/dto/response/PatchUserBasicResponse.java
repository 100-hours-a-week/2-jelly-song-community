package io.github.jeli01.kakao_bootcamp_community.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchUserBasicResponse {
    private Boolean isSuccess;
    private String message;

    public PatchUserBasicResponse() {
        this.isSuccess = true;
        this.message = "update user-basic success";
    }
}
