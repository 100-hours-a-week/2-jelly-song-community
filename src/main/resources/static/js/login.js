import { API_BASE_URL } from "./config.js"

let $layout_form = document.querySelector(".layout-form");

let $email_form = document.querySelector(".email-form");
let email_validation_container = document.querySelector(".email-validation-container");

let $password_form = document.querySelector(".password-form");
let $password_validation_container = document.querySelector(".password-validation-container");

let $login_button = document.querySelector(".button-disable");

let email_regex = /^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\.[a-zA-Z]{2,4}$/i;
let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

preventIfNotValidated();
validateWheneverTyped();

function preventIfNotValidated() {
    $layout_form.addEventListener("submit", async (event) => {
        event.preventDefault();

        let email = $email_form.value;
        let password = $password_form.value;

        try {
            const response = await fetch(`${API_BASE_URL}/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify({email, password}),
            });

            const data = await response.json();

            const accessToken = response.headers.get("Authorization");
            if (accessToken) {
                localStorage.setItem("accessToken", accessToken);
            }

            if (data.isSuccess === "true") {
                window.location.href = "./posts.html";
            } else {
                email_validation_container.innerText = "*아이디 또는 비밀번호를 확인해주세요";
                $password_validation_container.innerText = "*아이디 또는 비밀번호를 확인해주세요"
            }
        } catch (error) {
            alert("로그인 중 오류가 발생했습니다.");
        }
    })
}

function validateWheneverTyped() {
    $layout_form.addEventListener("input", (event) => {
        let email_address = $email_form.value;
        let password = $password_form.value;
        let validation = true;

        validateEmail();
        validatePassword();
        validateButton();

        function validateEmail() {
            if (!email_regex.test(email_address)) {
                email_validation_container.innerText = "*올바른 이메일 주소 형식을 입력해주세요";
                validation = false;
            } else {
                email_validation_container.innerText = "";
            }
        }

        function validatePassword() {
            if ($password_form.value == "") {
                $password_validation_container.innerText = "*비밀번호를 입력해주세요"
                validation = false;
            } else if (!passwordRegex.test(password)) {
                $password_validation_container.innerText = "*비밀번호는 8자 이상, 20자이하이며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다.";
                validation = false;
            } else {
                $password_validation_container.innerText = "";
            }
        }

        function validateButton() {
            if (validation == true) {
                $login_button.classList.remove("button-disable")
                $login_button.classList.add("button-enable")
            } else {
                $login_button.classList.remove("button-enable")
                $login_button.classList.add("button-disable")
            }
        }
    })
}