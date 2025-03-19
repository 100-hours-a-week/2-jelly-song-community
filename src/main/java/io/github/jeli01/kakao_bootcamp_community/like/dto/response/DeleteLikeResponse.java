package io.github.jeli01.kakao_bootcamp_community.like.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteLikeResponse {
    private Boolean isSuccess;
    private String message;
}
