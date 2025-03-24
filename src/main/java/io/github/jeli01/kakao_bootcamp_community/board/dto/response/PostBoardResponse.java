package io.github.jeli01.kakao_bootcamp_community.board.dto.response;

import io.github.jeli01.kakao_bootcamp_community.common.contant.ErrorMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostBoardResponse {
    private Boolean isSuccess = true;
    private String message = "board create success";

    public void changeClientError() {
        isSuccess = false;
        message = ErrorMessage.CLIENT_ERROR_MESSAGE;
    }

    public void changeServerError() {
        isSuccess = false;
        message = ErrorMessage.SERVER_ERROR_MESSAGE;
    }
}
