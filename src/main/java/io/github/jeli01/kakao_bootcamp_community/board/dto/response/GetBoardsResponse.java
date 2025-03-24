package io.github.jeli01.kakao_bootcamp_community.board.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.jeli01.kakao_bootcamp_community.board.dto.BoardsInnerData;
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
        this.message = message;
    }
}
