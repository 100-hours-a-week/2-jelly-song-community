let $header_back = document.querySelector(".header-back");
let create_post_container_form = document.querySelector(".create-post-container-form");
let $button = document.querySelector(".button-disable");
let $title_form = document.querySelector(".title-form");
let $post_comment_form_textarea = document.querySelector(".text-area-form");
let $post_comment_form_button = document.querySelector(".button-disable");
let $content_validation_container = document.querySelector(".content-validation-container");

activateHeaderBack();
preventSubmitIfNotValidate();
validateTitle();
validateTextArea();
initializeButtonAttribute();

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