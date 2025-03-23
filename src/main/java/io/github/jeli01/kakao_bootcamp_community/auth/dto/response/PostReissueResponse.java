package io.github.jeli01.kakao_bootcamp_community.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReissueResponse {
    private Boolean isSuccess = true;
    private String message = "reissue post success";
}
