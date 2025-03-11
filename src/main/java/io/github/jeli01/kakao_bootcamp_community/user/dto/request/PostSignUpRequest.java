package io.github.jeli01.kakao_bootcamp_community.user.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostSignUpRequest {
    private String email;
    private String password;
    private String nickname;
    private MultipartFile profileImage;
}
