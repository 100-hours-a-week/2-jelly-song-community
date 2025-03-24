package io.github.jeli01.kakao_bootcamp_community.auth.dto.request;

import lombok.Getter;

@Getter
public class PostLoginRequest {
    private String email;
    private String password;
}
