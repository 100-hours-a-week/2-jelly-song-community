import {getValidAccessToken, parseJwt} from "./auth.js";
import { API_BASE_URL } from "./config.js"

let $update_member_form = document.querySelector(".update-member-form");
let $nickname_form = document.querySelector(".nickname-form");
let $nickname_validation_container = document.querySelector(".nickname-validation-container");
let $button = document.querySelector(".button-disable");

let tostMessage = document.getElementById('tost_message');
let $drop_member_button = document.querySelector(".drop-member-button");

const modal = document.querySelector('.modal');
const modalOpen = document.querySelector('.drop-member-button');
const modalClose = document.querySelector('.close_btn');
const confirm_btn = document.querySelector(".confirm_btn");
let $fileDOM = document.querySelector("#file");
let $preview = document.querySelector(".profile-upload-button");
let $plus_font = document.querySelector(".plus-font");
let $layout_form = document.querySelector(".layout-form");
let $headerProfile = document.querySelector(".header-profile");

uploadProfile();
validateWheneverTyped();
activateTost();
activateModal();

(async function () {
    await loadUserInfo();
})();

async function loadUserInfo() {
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
        console.log("result:", result);

        if (!result.isSuccess) {
            console.error('유저 정보를 가져오지 못했습니다.');
            return;
        }

        const {nickname, userProfileImageUrl} = result.data;

        if (nickname) {
            $nickname_form.value = nickname;
        }

        console.log(`url(${userProfileImageUrl})`);
        if (userProfileImageUrl) {
            $preview.style.backgroundImage = `url(${userProfileImageUrl})`;
            $preview.style.backgroundRepeat = "no-repeat";
            $preview.style.backgroundSize = "cover";
            $preview.style.backgroundPosition = "center";
            $plus_font.innerHTML = "";
            $headerProfile.style.backgroundImage = `url(${userProfileImageUrl})`;
        }

        $layout_form.dispatchEvent(new Event("input", {bubbles: true}));

    } catch (err) {
        console.error('회원정보 조회 에러:', err);
    }
}

function uploadProfile() {
    $fileDOM.addEventListener('change', (event) => {
        const reader = new FileReader();
        reader.readAsDataURL($fileDOM.files[0]);
        reader.onload = ({target}) => {
            $preview.style.backgroundImage = `url(${target.result})`
            $preview.style.backgroundRepeat = "no-repeat"
            $preview.style.backgroundSize = "cover";
            $preview.style.backgroundPosition = "center";
            $plus_font.innerHTML = "";

            $layout_form.dispatchEvent(new Event("input", {bubbles: true}));
        };
    });
}

function validateWheneverTyped() {
    $update_member_form.addEventListener("input", (event) => {
        let validation = true;

        if ($preview.style.backgroundImage == "") {
            validation = false;
        }

        if ($nickname_form.value == "") {
            $nickname_validation_container.innerText = "*닉네임을 입력해주세요"
            validation = false;
        } else if ($nickname_form.value.includes(" ")) {
            $nickname_validation_container.innerText = "*띄어쓰기를 없애주세요"
            validation = false;
        } else if ($nickname_form.value.length >= 11) {
            $nickname_validation_container.innerText = "*닉네임은 최대 10자 까지 작성 가능합니다."
            validation = false;
        } else {
            $nickname_validation_container.innerText = "";
        }

        if (validation == true) {
            $button.classList.remove("button-disable")
            $button.classList.add("button-enable")
        } else {
            $button.classList.remove("button-enable")
            $button.classList.add("button-disable")
        }
    })
}

function activateTost() {
    $update_member_form.addEventListener("submit", async (event) => {
        event.preventDefault();

        if ($button.classList.contains("button-disable")) {
            return false;
        }

        try {
            const token = await getValidAccessToken();
            if (!token) {
                console.error("토큰이 없습니다.");
                return;
            }

            let jwtContent = parseJwt(token);
            let formData = new FormData();
            formData.append("nickname", $nickname_form.value);

            if ($fileDOM.files.length > 0) {
                formData.append("profile_image", $fileDOM.files[0]); // 파일 추가
            }

            const response = await fetch(`${API_BASE_URL}/users/${jwtContent.username}`, {
                method: "PATCH",
                headers: {
                    "Authorization": `Bearer ${token}`,
                },
                body: formData
            });

            const result = await response.json();

            if (result.isSuccess) {
                tostOn();
                await loadUserInfo();
            } else {
                console.log("업로드 실패:", result.message);
                if (result.message == "닉네임이 중복됩니다.") {
                    $nickname_validation_container.innerText = "닉네임이 중복됩니다"
                }
            }
        } catch (error) {
            console.error("업로드 중 오류 발생:", error);
        }
    });

    function tostOn() {
        tostMessage.classList.add('active');
        setTimeout(function () {
            tostMessage.classList.remove('active');
        }, 1000);
    }

}

function activateModal() {
    modalOpen.addEventListener('click', function () {
        modal.style.display = 'block';
    });
    modalClose.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    confirm_btn.addEventListener('click', async function () {
        try {
            const token = await getValidAccessToken();
            if (!token) {
                console.error("토큰이 없습니다.");
                return;
            }

            let jwtContent = parseJwt(token);

            const response = await fetch(`${API_BASE_URL}/users/${jwtContent.username}`, {
                method: "DELETE",
                headers: {
                    "Authorization": `Bearer ${token}`,
                },
            });

            const result = await response.json();

            if (result.isSuccess) {
                location.href = "./login.html";
            } else {
                console.error("회원 탈퇴 실패:", result.message);
            }
        } catch (error) {
            console.error("회원 탈퇴 오류 발생:", error);
        }
    })

    preventHrefDropMemberButton();

    function preventHrefDropMemberButton() {
        $drop_member_button.addEventListener("click", (e) => {
            e.preventDefault();
        })
    }
}