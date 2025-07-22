package io.github.jeli01.kakao_bootcamp_community.user.dto.response;

import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserResponse {
    private Boolean isSuccess;
    private String message;
    private Data data;

    public GetUserResponse(User user) {
        this.isSuccess = true;
        this.message = "user get success";
        this.data = new Data(user.getEmail(), user.getProfileImage(), user.getNickname());
    }

    @Getter
    @Setter
    class Data {
        private String email;
        private String userProfileImageUrl;
        private String nickname;

        public Data(String email, String userProfileImageUrl, String nickname) {
            this.email = email;
            this.userProfileImageUrl = userProfileImageUrl;
            this.nickname = nickname;
        }
    }
}
