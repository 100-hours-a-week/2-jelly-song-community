import {getValidAccessToken, parseJwt} from "./auth.js";
import { API_BASE_URL } from "./config.js"

let $headerProfile = document.querySelector(".header-profile");
let $postsContainer = document.querySelector(".layout-main-container");

(async function () {
    try {
        const token = await getValidAccessToken();
        if (!token) return;
        let jwtContent = parseJwt(token);

        const response = await fetch(`${API_BASE_URL}/users/${jwtContent.username}`, {
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

        const boardsRes = await fetch(`${API_BASE_URL}/boards`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
            },
        });

        const boardsResult = await boardsRes.json();

        if (boardsResult.isSuccess) {
            const posts = boardsResult.data;
            $postsContainer.innerHTML = "";

            posts.forEach(post => {
                const postHTML = `
                <div class="post-preview" onclick="location.href='./post.html?id=${post.id}'" style="cursor:pointer;">
                    <div class="post-preview-top">
                        <div class="post-preview-title-container">
                            <div class="post-preview-title">${post.title}</div>
                        </div>
                        <div class="post-preview-meta-container">
                            <div class="post-preview-meta">
                                <div class="post-preview-meta-element">좋아요 ${post.like}</div>
                                <div class="post-preview-meta-element">댓글 ${post.commentsCounts}</div>
                                <div class="post-preview-meta-element">조회수 ${post.visitCounts ?? 0}</div>
                            </div>
                            <div class="post-preview-time">${post.createDate.replace("T", " ").slice(0, 19)}</div>
                        </div>
                    </div>
                    <div class="post-preview-writer-container">
                        <div class="preview-profile"></div>
                        <div class="preview-writer">
                            <div class="preview-profile-image" style="background-image: url('${post.profileImage}');"></div>
                            <div class="align-center-container">
                                ${post.writer}
                            </div>
                        </div>
                    </div>
                </div>
                `;
                $postsContainer.insertAdjacentHTML("beforeend", postHTML);
            });
        }

    } catch (err) {
        console.error('회원정보 조회 에러:', err);
    }
})();