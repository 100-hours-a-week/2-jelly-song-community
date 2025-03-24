package io.github.jeli01.kakao_bootcamp_community.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessfulResponse {
    private Boolean isSuccess;
    private String message;

    public SuccessfulResponse(String message) {
        this.isSuccess = true;
        this.message = message;
    }
}
