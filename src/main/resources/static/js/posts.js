import {getValidAccessToken, parseJwt} from "./auth.js";

let $headerProfile = document.querySelector(".header-profile");


(async function () {
    try {
        const token = await getValidAccessToken();
        if (!token) return;
        let jwtContent = parseJwt(token);

        // 1. 유저 정보 조회 API 호출
        const response = await fetch(`http://localhost:8080/users/${jwtContent.username}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
            },
            credentials: "include",
        });

        const result = await response.json();

        if (!result.isSuccess) {
            console.error('유저 정보를 가져오지 못했습니다.');
            return;
        }

        const {userProfileImageUrl} = result.data;

        if (userProfileImageUrl) {
            $headerProfile.style.backgroundImage = `url(${userProfileImageUrl})`;
        }

    } catch (err) {
        console.error('회원정보 조회 에러:', err);
    }
})();