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
    window.location.href = "./posts.html";
})

const modal = document.querySelector('.post-modal');
const modalOpen = document.querySelector('.post-delete-button');
const modalClose = document.querySelector('.post-close_btn');
const confirm_btn = document.querySelector(".post-confirm_btn");

activateModal();

function activateModal() {
    modalOpen.addEventListener('click', function () {
        modal.style.display = 'block';
        document.body.style.overflow = 'hidden'
    });
    modalClose.addEventListener('click', function () {
        modal.style.display = 'none';
        document.body.style.overflow = ''
    });

    confirm_btn.addEventListener('click', function () {
        location.href = "./posts.html";
        document.body.style.overflow = ''
    })
}


$like_button = document.querySelector(".post-meta-likes-disable");
$post_meta_likes_number = document.querySelector(".post-meta-likes-number");

changeLikeButtonColor();
function changeLikeButtonColor() {
    $like_button.addEventListener("click", () => {
        if ($like_button.classList.contains("post-meta-likes-disable")) {
            $like_button.classList.remove("post-meta-likes-disable")
            $like_button.classList.add("post-meta-likes-enable")
            $post_meta_likes_number.innerHTML = parseInt($post_meta_likes_number.innerHTML) + 1
        } else {
            $like_button.classList.remove("post-meta-likes-enable")
            $like_button.classList.add("post-meta-likes-disable")
            $post_meta_likes_number.innerHTML = Number($post_meta_likes_number.innerHTML) - 1
        }
    })
}

$post_comment_form_textarea = document.querySelector(".post-comment-form-textarea");
$post_comment_form_button = document.querySelector(".post-comment-form-button");
$post_comment_form_textarea.addEventListener("input", () => {
    if ($post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
        $post_comment_form_button.classList.remove("button-disable")
        $post_comment_form_button.classList.add("button-enable")
    } else if ($post_comment_form_textarea.value == "" && $post_comment_form_button.classList.contains("button-enable")) {
        $post_comment_form_button.classList.remove("button-enable")
        $post_comment_form_button.classList.add("button-disable")
    }
})

$post_comment_form = document.querySelector(".post-comment-form");


$post_comment_right_update_buttons = document.querySelectorAll(".post-comment-right-update-button")

$post_comment_right_update_buttons.forEach(button => {
    button.addEventListener("click", () => {
        $post_comment_form_button.value = "댓글 수정";
        $post_comment_form_textarea.value = "댓글 내용";

        if ($post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
            $post_comment_form_button.classList.remove("button-disable")
            $post_comment_form_button.classList.add("button-enable")
        }
    })
})

let post_comment_form = document.querySelector(".post-comment-form");
preventSubmitIfNotValidate();
function preventSubmitIfNotValidate() {
    post_comment_form.addEventListener("submit", (event) => {
        if ($post_comment_form_button.classList.contains("button-disable")) {
            event.preventDefault();
        }
    })
}

const modal2 = document.querySelector('.comment-modal');
const modalOpen2s = document.querySelectorAll('.post-comment-right-delete-button');
const modalClose2 = document.querySelector('.comment-close_btn');
const confirm_btn2 = document.querySelector(".comment-confirm_btn");

activateModal2();

function activateModal2() {
    modalOpen2s.forEach( modalopen2 => {
        modalopen2.addEventListener("click",() => {
            modal2.style.display = 'block';
            document.body.style.overflow = 'hidden'
        })
    })
    modalClose2.addEventListener('click', function () {
        modal2.style.display = 'none';
        document.body.style.overflow = ''
    });

    confirm_btn2.addEventListener('click', function () {
        location.href = "./post.html";
        document.body.style.overflow = ''
    })
}