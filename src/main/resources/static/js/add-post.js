import {getValidAccessToken, parseJwt} from "./auth.js";

let $header_back = document.querySelector(".header-back");
let create_post_container_form = document.querySelector(".create-post-container-form");
let $button = document.querySelector(".button-disable");
let $title_form = document.querySelector(".title-form");
let $post_comment_form_textarea = document.querySelector(".text-area-form");
let $post_comment_form_button = document.querySelector(".button-disable");
let $content_validation_container = document.querySelector(".content-validation-container");
let $headerProfile = document.querySelector(".header-profile");

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