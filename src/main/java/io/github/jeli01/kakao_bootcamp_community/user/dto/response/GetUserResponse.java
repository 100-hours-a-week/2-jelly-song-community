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
        String profileImageUrl = user.getProfileImage();
        Data tempData = new Data();
        tempData.setUserProfileImageUrl(profileImageUrl);
        tempData.setNickname(user.getNickname());
        this.data = tempData;
    }

    @Getter
    @Setter
    class Data {
        private String userProfileImageUrl;
        private String nickname;
    }
}
