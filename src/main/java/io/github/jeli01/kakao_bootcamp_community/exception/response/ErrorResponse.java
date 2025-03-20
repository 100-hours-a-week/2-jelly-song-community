package io.github.jeli01.kakao_bootcamp_community.exception.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private Boolean isSuccess;
    private Integer statecode;
    private String message;
}
