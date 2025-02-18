let $header_profile = document.querySelector(".header-profile");
let $drop_down = document.querySelector(".drop-down");

activateDropDownMenu();

function activateDropDownMenu() {
    $header_profile.addEventListener("click", (e) => {
        if ($drop_down.style.display == "flex") {
            $drop_down.style.display = "none";
        } else {
            $drop_down.style.display = "flex";
        }
    })
}

let $header_back = document.querySelector(".header-back");

$header_back.addEventListener("click", (event) => {
    window.location.href = "./post.html";
})

let update_post_container_form = document.querySelector(".update-post-container-form");
let $button = document.querySelector(".button-disable");

preventSubmitIfNotValidate();
function preventSubmitIfNotValidate() {
    update_post_container_form.addEventListener("submit", (event) => {
        if ($button.classList.contains("button-disable")) {
            event.preventDefault();
        }
    })
}

$email_form = document.querySelector(".email-form");
$post_comment_form_textarea = document.querySelector(".text-area-form");
$post_comment_form_button = document.querySelector(".button-disable");
$password_validation_container = document.querySelector(".password-validation-container");

$email_form.addEventListener("input", () => {
    if ($email_form.value != "" && $post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
        $post_comment_form_button.classList.remove("button-disable")
        $post_comment_form_button.classList.add("button-enable")
        $password_validation_container.innerHTML = ""
    } else if (($email_form.value == "" || $post_comment_form_textarea.value == "") && $post_comment_form_button.classList.contains("button-enable")) {
        $post_comment_form_button.classList.remove("button-enable")
        $post_comment_form_button.classList.add("button-disable")
        $password_validation_container.innerHTML = "제목, 내용을 모두 작성해주세요"
    }
})

$post_comment_form_textarea.addEventListener("input", () => {
    if ($email_form.value != "" && $post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
        $post_comment_form_button.classList.remove("button-disable")
        $post_comment_form_button.classList.add("button-enable")
        $password_validation_container.innerHTML = ""
    } else if (($email_form.value == "" || $post_comment_form_textarea.value == "") && $post_comment_form_button.classList.contains("button-enable")) {
        $post_comment_form_button.classList.remove("button-enable")
        $post_comment_form_button.classList.add("button-disable")
        $password_validation_container.innerHTML = "제목, 내용을 모두 작성해주세요"
    }
})

haha();
function haha() {
    if ($email_form.value != "" && $post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
        $post_comment_form_button.classList.remove("button-disable")
        $post_comment_form_button.classList.remove("button-enable")
        $post_comment_form_button.classList.add("button-enable")
    } else if (($email_form.value == "" || $post_comment_form_textarea.value == "") && $post_comment_form_button.classList.contains("button-enable")) {
        $post_comment_form_button.classList.remove("button-disable")
        $post_comment_form_button.classList.remove("button-enable")
        $post_comment_form_button.classList.add("button-disable")
    }
}