let $drop_down = document.querySelector(".drop-down");
let $header_profile = document.querySelector(".header-profile");
$header_profile.addEventListener("click", (e) => {
    if ($drop_down.style.display == "flex") {
        $drop_down.style.display = "none";
    } else {
        $drop_down.style.display = "flex";
    }
})

$update_password_form = document.querySelector(".update-password-form");
$button = document.querySelector(".button-disable");
$password_form = document.querySelector(".password-form")
$password_validation_container = document.querySelector(".password-validation-container");
let $password_confirm_form = document.querySelector(".password-confirm-form");
let $password_confirm_validation_container = document.querySelector(".password-confirm-validation-container");

$update_password_form.addEventListener("input", (event) => {
    let password = $password_form.value;
    let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

    let validation = true;

    if ($password_form.value == "") {
        $password_validation_container.innerText = "*비밀번호를 입력해주세요"
        validation = false;
    } else if (!passwordRegex.test(password)) {
        $password_validation_container.innerText = "*비밀번호는 8자 이상, 20자이하이며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다.";
        validation = false;
    } else if ($password_confirm_form.value != $password_form.value && $password_confirm_form.value != "") {
        $password_validation_container.innerText = "*비밀번호가 다릅니다";
        validation = false;
    }
    else {
        $password_validation_container.innerText = "";
    }

    if ($password_confirm_form.value == "") {
        $password_confirm_validation_container.innerText = "*비밀번호를 한번 더 입력해주세요"
        validation = false;
    } else if ($password_confirm_form.value != $password_form.value) {
        $password_confirm_validation_container.innerText = "*비밀번호가 다릅니다";
        validation = false;
    } else {
        $password_confirm_validation_container.innerText = "";
    }

    if (validation == true) {
        $button.classList.remove("button-disable")
        $button.classList.add("button-enable")
    } else {
        $button.classList.remove("button-enable")
        $button.classList.add("button-disable")
    }
})

$update_password_form.addEventListener("submit", (event) => {
    if (!$button.classList.contains("button-enable")) {
        event.preventDefault();
        return false;
    }

    event.preventDefault();
    tostOn()
})

//1. 토스트 메시지, 버튼요소를 변수에 대입
let tostMessage = document.getElementById('tost_message');
let tostBtn = document.getElementById('tost_btn');

//2. 토스트 메시지 노출-사라짐 함수 작성
function tostOn(){
    tostMessage.classList.add('active');
    setTimeout(function(){
        tostMessage.classList.remove('active');
    },1000);
}