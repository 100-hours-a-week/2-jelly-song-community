package io.github.jeli01.kakao_bootcamp_community.board.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostBoardRequest {
    private String title;
    private String content;
    private MultipartFile image;
}
