let $header_back = document.querySelector(".header-back")

let $fileDOM = document.querySelector("#file");
let $preview = document.querySelector(".profile-upload-button");
let $plus_font = document.querySelector(".plus-font");
let $profile_validation_container = document.querySelector(".profile-validation-container");

let $layout_form = document.querySelector(".layout-form");
let $email_form = document.querySelector(".email-form");
let email_validation_container = document.querySelector(".email-validation-container");

let $password_form = document.querySelector(".password-form");
let $password_validation_container = document.querySelector(".password-validation-container");

let $password_confirm_form = document.querySelector(".password-confirm-form");
let $password_confirm_validation_container = document.querySelector(".password-confirm-validation-container");

let $nickname_form = document.querySelector(".nickname-form");
let $nickname_validation_container = document.querySelector(".nickname-validation-container");

let $button = document.querySelector(".button-disable");

activateBackLink();
uploadProfile();
validateWheneverTyped();
preventSubmitIfNotValidate();

function activateBackLink() {
    $header_back.addEventListener("click", (event) => {
        location.href = "./login.html";
    })
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

            $profile_validation_container.innerHTML = ""

            $layout_form.dispatchEvent(new Event("input", {bubbles: true}));
        };
    });
}

function validateWheneverTyped() {
    $layout_form.addEventListener("input", (event) => {
        let email_address = $email_form.value;
        let email_regex = /^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\.[a-zA-Z]{2,4}$/i;

        let password = $password_form.value;
        let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

        let validation = true;

        validateProfile();
        validateEmail();
        validatePassword();
        validatePasswordConfirm();
        validateNickname();
        validateButton();

        function validateProfile() {
            if ($preview.style.backgroundImage == "") {
                validation = false;
            }
        }
        function validateEmail() {
            if ($email_form.value == "") {
                email_validation_container.innerText = "*이메일을 입력해주세요";
                validation = false;
            } else if (!email_regex.test(email_address)) {
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
        function validateNickname() {
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

function preventSubmitIfNotValidate() {
    $layout_form.addEventListener("submit", (event) => {
        if ($button.classList.contains("button-disable")) {
            event.preventDefault();
        }
    })
}