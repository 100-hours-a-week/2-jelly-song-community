let $header_back = document.querySelector(".header-back");
const postDeleteModal = document.querySelector('.post-modal');
const postDeleteModalOpen = document.querySelector('.delete-button');
const postDeleteModalClose = document.querySelector('.post-close_btn');
const postDeleteConfirmBtn = document.querySelector(".post-confirm_btn");
let $like_button = document.querySelector(".post-meta-likes-disable");
let $post_meta_likes_number = document.querySelector(".post-meta-likes-number");
let $post_comment_form_textarea = document.querySelector(".post-comment-form-textarea");
let $post_comment_form_button = document.querySelector(".post-comment-form-button");
let $post_comment_right_update_buttons = document.querySelectorAll(".post-comment-right-update-button")
const commentModal = document.querySelector('.comment-modal');
const commentModalOpens = document.querySelectorAll('.post-comment-right-delete-button');
const commentModalClose = document.querySelector('.comment-close_btn');
const commentConfirmBtn = document.querySelector(".comment-confirm_btn");
let post_comment_form = document.querySelector(".post-comment-form");

activateHeaderBack();
activatePostDeleteModal();
changeLikeButtonColor();
activateCommentFormButtonWhenExistsInCommentBox();
preventSubmitIfNotValidate();
changeCommentButtonToUpdateButton();
activateCommentModal();

function activateHeaderBack() {
    $header_back.addEventListener("click", (event) => {
        window.location.href = "./posts.html";
    })
}
function activatePostDeleteModal() {
    postDeleteModalOpen.addEventListener('click', function () {
        postDeleteModal.style.display = 'block';
        document.body.style.overflow = 'hidden'
    });
    postDeleteModalClose.addEventListener('click', function () {
        postDeleteModal.style.display = 'none';
        document.body.style.overflow = ''
    });

    postDeleteConfirmBtn.addEventListener('click', function () {
        location.href = "./posts.html";
        document.body.style.overflow = ''
    })
}

function changeLikeButtonColor() {
    $like_button.addEventListener("click", () => {
        if ($like_button.classList.contains("post-meta-likes-disable")) {
            $like_button.classList.remove("post-meta-likes-disable")
            $like_button.classList.add("post-meta-likes-enable")
            $post_meta_likes_number.innerHTML = parseInt($post_meta_likes_number.innerHTML) + 1
        } else {
            $like_button.classList.remove("post-meta-likes-enable")
            $like_button.classList.add("post-meta-likes-disable")
            $post_meta_likes_number.innerHTML = parseInt($post_meta_likes_number.innerHTML) - 1
        }
    })
}

function activateCommentFormButtonWhenExistsInCommentBox() {
    $post_comment_form_textarea.addEventListener("input", () => {
        if ($post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
            $post_comment_form_button.classList.remove("button-disable")
            $post_comment_form_button.classList.add("button-enable")
        } else if ($post_comment_form_textarea.value == "" && $post_comment_form_button.classList.contains("button-enable")) {
            $post_comment_form_button.classList.remove("button-enable")
            $post_comment_form_button.classList.add("button-disable")
        }
    })
}

function preventSubmitIfNotValidate() {
    post_comment_form.addEventListener("submit", (event) => {
        if ($post_comment_form_button.classList.contains("button-disable")) {
            event.preventDefault();
        }
    })
}

function changeCommentButtonToUpdateButton() {
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
}

function activateCommentModal() {
    commentModalOpens.forEach(modalOpen => {
        modalOpen.addEventListener("click",() => {
            commentModal.style.display = 'block';
            document.body.style.overflow = 'hidden'
        })
    })
    commentModalClose.addEventListener('click', function () {
        commentModal.style.display = 'none';
        document.body.style.overflow = ''
    });

    commentConfirmBtn.addEventListener('click', function () {
        location.href = "./post.html";
        document.body.style.overflow = ''
    })
}