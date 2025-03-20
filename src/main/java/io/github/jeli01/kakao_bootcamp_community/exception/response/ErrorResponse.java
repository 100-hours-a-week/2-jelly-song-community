package io.github.jeli01.kakao_bootcamp_community.exception.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private Boolean isSuccess;
    private Integer statecode;
    private String message;

    public ErrorResponse(Integer statecode, String message) {
        this.isSuccess = false;
        this.statecode = statecode;
        this.message = message;
    }
}
