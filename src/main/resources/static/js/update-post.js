import {getValidAccessToken, parseJwt} from "./auth.js";
import { API_BASE_URL } from "./config.js"

let $header_back = document.querySelector(".header-back");
let update_post_container_form = document.querySelector(".update-post-container-form");
let $button = document.querySelector(".button-disable");
let $title_form = document.querySelector(".title-form");
let $post_comment_form_textarea = document.querySelector(".textarea-form");
let $post_comment_form_button = document.querySelector(".button-disable");
let $content_validation_container = document.querySelector(".content-validation-container");
let $headerProfile = document.querySelector(".header-profile");

activateHeaderBack();
preventSubmitIfNotValidate();
validateEmail();
validateTextArea();
initializeButtonAttribute();

(async function () {
    try {
        const token = await getValidAccessToken();
        if (!token) return;
        let jwtContent = parseJwt(token);

        let response = await fetch(`${API_BASE_URL}/users/${jwtContent.username}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
            },
            credentials: "include",
        });

        let result = await response.json();

        if (!result.isSuccess) {
            console.error('유저 정보를 가져오지 못했습니다.');
            return;
        }

        const {userProfileImageUrl} = result.data;

        if (userProfileImageUrl) {
            $headerProfile.style.backgroundImage = `url(${userProfileImageUrl})`;
        }

        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get("id");

        response = await fetch(`${API_BASE_URL}/boards/${postId}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
            }
        });

        result = await response.json();

        if (!result.isSuccess) {
            console.error("게시글을 불러오지 못했습니다.");
            return;
        }

        const post = result.data;

        document.querySelector(".title-form").value = post.title;
        document.querySelector(".textarea-form").value = post.contents;

        const fileInput = document.querySelector("#update-post-image");
        const fileNameDisplay = document.createElement("div");
        fileNameDisplay.className = "origin-file-name";
        fileNameDisplay.innerText = `기존 첨부된 파일: ${post.originImageName ?? "없음"}`;
        fileInput.parentNode.insertBefore(fileNameDisplay, fileInput.nextSibling);

    } catch (err) {
        console.error('회원정보 조회 에러:', err);
    }
})();

function activateHeaderBack() {
    $header_back.addEventListener("click", (event) => {
        window.location.href = "./post.html";
    })
}

function preventSubmitIfNotValidate() {
    update_post_container_form.addEventListener("submit", async (event) => {
        event.preventDefault();
        if ($button.classList.contains("button-disable")) {
            return
        }

        const token = await getValidAccessToken();
        if (!token) {
            alert("로그인이 필요합니다.");
            return;
        }

        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get("id");

        const title = $title_form.value;
        const contents = $post_comment_form_textarea.value;
        const imageFile = document.querySelector("#update-post-image").files[0];

        const formData = new FormData();
        formData.append("title", title);
        formData.append("content", contents);
        if (imageFile) {
            formData.append("image", imageFile);
        }

        try {
            const response = await fetch(`${API_BASE_URL}/boards/${postId}`, {
                method: "PUT", // 또는 PATCH
                headers: {
                    "Authorization": `Bearer ${token}`,
                },
                body: formData,
            });

            const result = await response.json();

            if (result.isSuccess) {
                alert("게시글이 수정되었습니다.");
                window.location.href = `./post.html?id=${postId}`;
            } else {
                alert("게시글 수정에 실패했습니다: " + result.message);
            }

        } catch (err) {
            console.error("게시글 수정 중 에러:", err);
            alert("게시글 수정 중 오류가 발생했습니다.");
        }
    })
}

function validateEmail() {
    $title_form.addEventListener("input", () => {
        if ($title_form.value != "" && $post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
            $post_comment_form_button.classList.remove("button-disable")
            $post_comment_form_button.classList.add("button-enable")
            $content_validation_container.innerHTML = ""
        } else if (($title_form.value == "" || $post_comment_form_textarea.value == "") && $post_comment_form_button.classList.contains("button-enable")) {
            $post_comment_form_button.classList.remove("button-enable")
            $post_comment_form_button.classList.add("button-disable")
            $content_validation_container.innerHTML = "제목, 내용을 모두 작성해주세요"
        }
    })
}

function validateTextArea() {
    $post_comment_form_textarea.addEventListener("input", () => {
        if ($title_form.value != "" && $post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
            $post_comment_form_button.classList.remove("button-disable")
            $post_comment_form_button.classList.add("button-enable")
            $content_validation_container.innerHTML = ""
        } else if (($title_form.value == "" || $post_comment_form_textarea.value == "") && $post_comment_form_button.classList.contains("button-enable")) {
            $post_comment_form_button.classList.remove("button-enable")
            $post_comment_form_button.classList.add("button-disable")
            $content_validation_container.innerHTML = "제목, 내용을 모두 작성해주세요"
        }
    })
}

function initializeButtonAttribute() {
    if ($title_form.value != "" && $post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
        $post_comment_form_button.classList.remove("button-disable")
        $post_comment_form_button.classList.remove("button-enable")
        $post_comment_form_button.classList.add("button-enable")
    } else if (($title_form.value == "" || $post_comment_form_textarea.value == "") && $post_comment_form_button.classList.contains("button-enable")) {
        $post_comment_form_button.classList.remove("button-disable")
        $post_comment_form_button.classList.remove("button-enable")
        $post_comment_form_button.classList.add("button-disable")
    }
}