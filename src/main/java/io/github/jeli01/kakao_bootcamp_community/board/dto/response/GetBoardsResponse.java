package io.github.jeli01.kakao_bootcamp_community.board.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.jeli01.kakao_bootcamp_community.board.dto.BoardsInnerData;
import io.github.jeli01.kakao_bootcamp_community.util.contant.ErrorMessage;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBoardsResponse {
    private Boolean isSuccess;
    String message;
    Integer count;
    @JsonProperty("data")
    List<BoardsInnerData> datas;

    public GetBoardsResponse() {
        isSuccess = true;
        message = "boards get success";
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
