package io.github.jeli01.kakao_bootcamp_community.user.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PatchPasswordRequest {
    private String password;
}
