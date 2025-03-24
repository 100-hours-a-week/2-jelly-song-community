package io.github.jeli01.kakao_bootcamp_community.board.dto.response;

import io.github.jeli01.kakao_bootcamp_community.common.contant.ErrorMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteBoardResponse {
    private Boolean isSuccess;
    private String message;

    public DeleteBoardResponse() {
        this.isSuccess = true;
        this.message = "board delete success";
    }

    public void changeClientError() {
        isSuccess = false;
        message = ErrorMessage.CLIENT_ERROR_MESSAGE;
    }

    public void changeServerError() {
        isSuccess = false;
        message = ErrorMessage.SERVER_ERROR_MESSAGE;
    }
}
