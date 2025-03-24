package io.github.jeli01.kakao_bootcamp_community.user.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PatchUserBasicRequest {
    private MultipartFile profileImage;
    private String nickname;

    public PatchUserBasicRequest(MultipartFile profileImage, String nickname) {
        this.profileImage = profileImage;
        this.nickname = nickname;
    }
}
