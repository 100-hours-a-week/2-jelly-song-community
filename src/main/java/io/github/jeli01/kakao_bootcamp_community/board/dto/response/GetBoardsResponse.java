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

    public GetBoardsResponse(String message) {
        isSuccess = true;
        this.message = message;
    }

    public GetBoardsResponse(String message, Integer count, List<BoardsInnerData> datas) {
        this.isSuccess = true;
        this.message = message;
        this.count = count;
        this.datas = datas;
    }
}
