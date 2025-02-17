let $layout_form = document.querySelector(".layout-form");

let $email_form = document.querySelector(".email-form");
let email_validation_container = document.querySelector(".email-validation-container");

let $password_form = document.querySelector(".password-form");
let $password_validation_container = document.querySelector(".password-validation-container");

let $login_button = document.querySelector(".button-disable");

let email_regex = /^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\.[a-zA-Z]{2,4}$/i;
let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

$layout_form.addEventListener("submit", (event) => {
    let email_address = $email_form.value;
    let password = $password_form.value;

    if (email_regex.test(email_address) && passwordRegex.test(password)) {
        return;
    }
    event.preventDefault();
})

$layout_form.addEventListener("input", (event) => {
    let email_address = $email_form.value;
    let password = $password_form.value;

    let validation = true;

    if (!email_regex.test(email_address)) {
        email_validation_container.innerText = "*올바른 이메일 주소 형식을 입력해주세요";
        validation = false;
    } else {
        email_validation_container.innerText = "";
    }

    if ($password_form.value == "") {
        $password_validation_container.innerText = "*비밀번호를 입력해주세요"
        validation = false;
    } else if (!passwordRegex.test(password)) {
        $password_validation_container.innerText = "*비밀번호는 8자 이상, 20자이하이며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다.";
        validation = false;
    } else {
        $password_validation_container.innerText = "";
    }

    if (validation == true) {
        $login_button.classList.remove("button-disable")
        $login_button.classList.add("button-enable")
    } else {
        $login_button.classList.remove("button-enable")
        $login_button.classList.add("button-disable")
    }
})