package io.github.jeli01.kakao_bootcamp_community.common.exception.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private Boolean isSuccess;
    private Integer stateCode;
    private String message;

    public ErrorResponse(Integer stateCode, String message) {
        this.isSuccess = false;
        this.stateCode = stateCode;
        this.message = message;
    }
}
