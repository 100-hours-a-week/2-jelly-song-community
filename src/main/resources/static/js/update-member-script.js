let $header_profile = document.querySelector(".header-profile");
let $drop_down = document.querySelector(".drop-down");

$header_profile.addEventListener("click", (e) => {
    if ($drop_down.style.display == "flex") {
        $drop_down.style.display = "none";
    } else {
        $drop_down.style.display = "flex";
    }
})

$update_member_form = document.querySelector(".update-member-form");
$nickname_form = document.querySelector(".nickname-form");
$nickname_validation_container = document.querySelector(".nickname-validation-container");
$button = document.querySelector(".button-disable");
$update_member_form.addEventListener("input", (event) => {
    let validation = true;

    if ($nickname_form.value == "") {
        $nickname_validation_container.innerText = "*닉네임을 입력해주세요"
        validation = false;
    } else if ($nickname_form.value.includes(" ")) {
        $nickname_validation_container.innerText = "*띄어쓰기를 없애주세요"
        validation = false;
    } else if ($nickname_form.value.length >= 11){
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

$update_member_form.addEventListener("submit", (event) => {
    event.preventDefault();
    if ($button.classList.contains("button-disable")) {
        return false;
    }
    tostOn()
})

let tostMessage = document.getElementById('tost_message');

function tostOn(){
    tostMessage.classList.add('active');
    setTimeout(function(){
        tostMessage.classList.remove('active');
    },1000);
}

let $drop_member_button = document.querySelector(".drop-member-button");

$drop_member_button.addEventListener("click", (e) => {
    e.preventDefault();
})

const modal = document.querySelector('.modal');
const modalOpen = document.querySelector('.drop-member-button');
const modalClose = document.querySelector('.close_btn');
const confirm_btn = document.querySelector(".confirm_btn");

modalOpen.addEventListener('click',function(){
    modal.style.display = 'block';
});
modalClose.addEventListener('click',function(){
    modal.style.display = 'none';
});

confirm_btn.addEventListener('click', function() {
    location.href="./login.html";
})