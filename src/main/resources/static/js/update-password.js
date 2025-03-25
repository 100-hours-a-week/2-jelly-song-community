import {getValidAccessToken, parseJwt} from "./auth.js";
import { API_BASE_URL } from "./config.js"

let $update_password_form = document.querySelector(".update-password-form");
let $button = document.querySelector(".button-disable");
let $password_form = document.querySelector(".password-form")
let $password_validation_container = document.querySelector(".password-validation-container");
let $password_confirm_form = document.querySelector(".password-confirm-form");
let $password_confirm_validation_container = document.querySelector(".password-confirm-validation-container");
let tostMessage = document.getElementById('tost_message');
let $headerProfile = document.querySelector(".header-profile");

validateWheneverInput();
activateTost();

(async function () {
    try {
        const token = await getValidAccessToken();
        if (!token) return;
        let jwtContent = parseJwt(token);

        // 1. 유저 정보 조회 API 호출
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

function validateWheneverInput() {
    $update_password_form.addEventListener("input", (event) => {
        let password = $password_form.value;
        let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
        let validation = true;

        validatePassword();
        validatePasswordConfirm();
        validateButton();

        function validatePassword() {
            if ($password_form.value == "") {
                $password_validation_container.innerText = "*비밀번호를 입력해주세요"
                validation = false;
            } else if (!passwordRegex.test(password)) {
                $password_validation_container.innerText = "*비밀번호는 8자 이상, 20자이하이며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다.";
                validation = false;
            } else if ($password_confirm_form.value != $password_form.value && $password_confirm_form.value != "") {
                $password_validation_container.innerText = "*비밀번호가 다릅니다";
                validation = false;
            } else {
                $password_validation_container.innerText = "";
            }
        }

        function validatePasswordConfirm() {
            if ($password_confirm_form.value == "") {
                $password_confirm_validation_container.innerText = "*비밀번호를 한번 더 입력해주세요"
                validation = false;
            } else if ($password_confirm_form.value != $password_form.value) {
                $password_confirm_validation_container.innerText = "*비밀번호가 다릅니다";
                validation = false;
            } else {
                $password_confirm_validation_container.innerText = "";
            }
        }

        function validateButton() {
            if (validation == true) {
                $button.classList.remove("button-disable")
                $button.classList.add("button-enable")
            } else {
                $button.classList.remove("button-enable")
                $button.classList.add("button-disable")
            }
        }
    })
}

function activateTost() {
    $update_password_form.addEventListener("submit", async (event) => {
        event.preventDefault();
        if (!$button.classList.contains("button-enable")) {
            return false;
        }
        try {
            const token = await getValidAccessToken();
            if (!token) return;
            let jwtContent = parseJwt(token);

            const response = await fetch(`${API_BASE_URL}/users/${jwtContent.username}/password`, {
                method: "PATCH",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify({
                    password: $password_form.value
                })
            });

            const data = await response.json();
            if (!data.isSuccess) {
                console.error("패스워드 변경 실패:");
                return;
            }

        } catch (err) {
            alert("패스워드변경 에러")
            console.error("게시물 조회 오류:", err);
        }

        tostOn()

        function tostOn() {
            tostMessage.classList.add('active');
            setTimeout(function () {
                tostMessage.classList.remove('active');
            }, 1000);
        }
    })
}