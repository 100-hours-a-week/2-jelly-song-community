import {getValidAccessToken, parseJwt} from "./auth.js";

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
let $headerProfile = document.querySelector(".header-profile");

activateHeaderBack();
activatePostDeleteModal();
changeLikeButtonColor();
activateCommentFormButtonWhenExistsInCommentBox();
preventSubmitIfNotValidate();
changeCommentButtonToUpdateButton();
activateCommentModal();
(async function () {
    await fetchAndRenderUserProfile();
})();

async function fetchAndRenderUserProfile() {
    try {
        let token = await getValidAccessToken();
        if (!token) return;
        let jwtContent = parseJwt(token);

        let response = await fetch(`http://localhost:8080/users/${jwtContent.username}`, {
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

        let {userProfileImageUrl} = result.data;

        if (userProfileImageUrl) {
            $headerProfile.style.backgroundImage = `url(${userProfileImageUrl})`;
        }

        token = await getValidAccessToken();
        if (!token) return;

        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get("id");

        response = await fetch(`http://localhost:8080/boards/${postId}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
            }
        });

        result = await response.json();
        if (!result.isSuccess) {
            console.error("게시물 정보를 가져오지 못했습니다.");
            return;
        }

        const post = result.data;

        document.querySelector(".post-header-title").innerText = post.title;
        document.querySelector(".post-header-meta-writer").innerText = post.writer;
        document.querySelector(".align-row-container .align-center-container").innerText = formatDate(post.createDate);
        document.querySelector(".post-body-main").innerText = post.contents;
        document.querySelector(".post-header-meta-profile").style.backgroundImage = `url(${post.profileImage})`;
        const profileDiv = document.querySelector(".post-header-meta-profile");
        if (post.boardImage != null) {
            profileDiv.style.backgroundImage = `url(${post.boardImage})`;
        }

        const bodyImage = document.querySelector(".post-body-image");
        if (post.boardImage) {
            bodyImage.src = post.boardImage;
        }

        $post_meta_likes_number.innerText = post.like;
        document.querySelectorAll(".post-meta-number")[1].innerText = post.visitCount;
        document.querySelectorAll(".post-meta-number")[2].innerText = post.commentsCount;

        const commentContainer = document.querySelector(".align-column-container");
        commentContainer.innerHTML = "";

        post.comments.forEach(comment => {
            const commentEl = document.createElement("div");
            commentEl.className = "post-comment";
            commentEl.innerHTML = `
                <div>
                    <div class="align-row-container">
                        <div class="post-comment-meta-profile" style="background-image: url('${comment.profileImage}')"></div>
                        <div class="post-comment-meta-writer">${comment.writer}</div>
                        <div class="post-comment-meta-time">${formatDate(comment.createDate)}</div>
                    </div>
                    <div class="post-comment-main">${comment.content}</div>
                </div>
                <div class="align-center-container">
                    <div>
                        <button class="post-comment-right-update-button post-comment-right-button">수정</button>
                        <button class="post-comment-right-delete-button post-comment-right-button">삭제</button>
                    </div>
                </div>
            `;
            commentContainer.appendChild(commentEl);
        });

        // ✅ 댓글 새로 렌더링 후 버튼들 다시 연결
        reconnectCommentButtons();

    } catch (err) {
        console.error('회원정보 조회 에러:', err);
    }
}

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
        modalOpen.addEventListener("click", () => {
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

function formatDate(dateStr) {
    const date = new Date(dateStr);
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}

function pad(n) {
    return n < 10 ? "0" + n : n;
}

// ✅ 새로 렌더링된 댓글에 이벤트 재연결 함수
function reconnectCommentButtons() {
    $post_comment_right_update_buttons = document.querySelectorAll(".post-comment-right-update-button");
    const newDeleteButtons = document.querySelectorAll('.post-comment-right-delete-button');

    $post_comment_right_update_buttons.forEach(button => {
        button.addEventListener("click", () => {
            $post_comment_form_button.value = "댓글 수정";
            $post_comment_form_textarea.value = "댓글 내용";

            if ($post_comment_form_textarea.value != "" && $post_comment_form_button.classList.contains("button-disable")) {
                $post_comment_form_button.classList.remove("button-disable")
                $post_comment_form_button.classList.add("button-enable")
            }
        });
    });

    newDeleteButtons.forEach(button => {
        button.addEventListener("click", () => {
            commentModal.style.display = 'block';
            document.body.style.overflow = 'hidden'
        });
    });
}