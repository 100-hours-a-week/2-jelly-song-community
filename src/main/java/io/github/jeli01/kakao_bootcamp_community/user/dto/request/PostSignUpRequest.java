package io.github.jeli01.kakao_bootcamp_community.user.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostSignUpRequest {
    private MultipartFile profileImage;
    private String email;
    private String password;
    private String nickname;

    public PostSignUpRequest(MultipartFile profileImage, String email, String password, String nickname) {
        this.profileImage = profileImage;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
