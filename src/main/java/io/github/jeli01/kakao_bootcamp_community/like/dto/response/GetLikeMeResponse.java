package io.github.jeli01.kakao_bootcamp_community.like.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetLikeMeResponse {
    private Boolean isSuccess;
    private String message;
    private Data data;

    public GetLikeMeResponse(Boolean liked) {
        this.isSuccess = true;
        this.message = "is liked get success";
        Data data1 = new Data();
        data1.setIsLiked(liked);
        data = data1;
    }

    @Getter
    @Setter
    static class Data {
        private Boolean isLiked;
    }
}
