import {getValidAccessToken, parseJwt} from "./auth.js";
import { API_BASE_URL } from "./config.js"

let $header_back = document.querySelector(".header-back");
let create_post_container_form = document.querySelector(".create-post-container-form");
let $button = document.querySelector(".button-disable");
let $title_form = document.querySelector(".title-form");
let $post_comment_form_textarea = document.querySelector(".text-area-form");
let $post_comment_form_button = document.querySelector(".button-disable");
let $content_validation_container = document.querySelector(".content-validation-container");
let $headerProfile = document.querySelector(".header-profile");
let $create_post_container_form = document.querySelector(".create-post-container-form");

activateHeaderBack();
preventSubmitIfNotValidate();
validateTitle();
validateTextArea();
initializeButtonAttribute();

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

    } catch (err) {
        console.error('회원정보 조회 에러:', err);
    }
})();

function activateHeaderBack() {
    $header_back.addEventListener("click", (event) => {
        window.location.href = "./posts.html";
    })
}

function preventSubmitIfNotValidate() {
    create_post_container_form.addEventListener("submit", (event) => {
        if ($button.classList.contains("button-disable")) {
            event.preventDefault();
        }
    })
}

function validateTitle() {
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

$create_post_container_form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const token = await getValidAccessToken();
    if (!token) {
        alert("로그인이 필요합니다.");
        return;
    }

    const title = $title_form.value;
    const content = $post_comment_form_textarea.value;
    const imageFile = document.getElementById("create-post-file").files[0];

    if (!title || !content) {
        alert("제목과 내용을 모두 입력해주세요.");
        return;
    }

    const formData = new FormData();
    formData.append("title", title);
    formData.append("content", content);
    if (imageFile) {
        formData.append("image", imageFile);
    }

    try {
        const response = await fetch(`${API_BASE_URL}/boards`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
            },
            body: formData,
        });

        const result = await response.json();

        if (result.isSuccess) {
            alert("게시물이 성공적으로 등록되었습니다!");
            window.location.href = "/posts.html";
        } else {
            alert("게시글 등록 실패: " + result.message);
        }
    } catch (error) {
        console.error("게시글 작성 오류:", error);
        alert("게시글 작성 중 오류가 발생했습니다.");
    }
});