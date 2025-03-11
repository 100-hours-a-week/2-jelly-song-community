package io.github.jeli01.kakao_bootcamp_community.user.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PatchUserBasicRequest {
    private String nickname;
    private MultipartFile profileImage;
}
