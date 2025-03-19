package io.github.jeli01.kakao_bootcamp_community.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchPasswordResponse {
    private Boolean isSuccess;
    private String message;

    public PatchPasswordResponse() {
        this.isSuccess = true;
        this.message = "password update success";
    }
}
