let $update_password_form = document.querySelector(".update-password-form");
let $button = document.querySelector(".button-disable");
let $password_form = document.querySelector(".password-form")
let $password_validation_container = document.querySelector(".password-validation-container");
let $password_confirm_form = document.querySelector(".password-confirm-form");
let $password_confirm_validation_container = document.querySelector(".password-confirm-validation-container");
let tostMessage = document.getElementById('tost_message');

activateDropDownMenu();
validateWheneverInput();
activateTost();



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
    $update_password_form.addEventListener("submit", (event) => {
        event.preventDefault();
        if (!$button.classList.contains("button-enable")) {
            return false;
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