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
        this.data = new Data(liked);
    }

    @Getter
    @Setter
    static class Data {
        private Boolean isLiked;
        public Data(Boolean isLiked) {
            this.isLiked = isLiked;
        }
    }
}
