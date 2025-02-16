let $drop_down = document.querySelector(".drop-down");
let $header_profile = document.querySelector(".header-profile");

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
$update_member_button = document.querySelector(".update-member-button-disable");
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
        $update_member_button.classList.remove("update-member-button-disable")
        $update_member_button.classList.add("update-member-button-enable")
    } else {
        $update_member_button.classList.remove("update-member-button-enable")
        $update_member_button.classList.add("update-member-button-disable")
    }
})

$update_member_form.addEventListener("submit", (event) => {
    if (!$update_member_button.classList.contains("update-member-button-enable")) {
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

let $drop_member_button = document.querySelector(".drop-member-button");

$drop_member_button.addEventListener("click", (e) => {
    e.preventDefault();
})

const modal = document.querySelector('.modal');
const modalOpen = document.querySelector('.drop-member-button');
const modalClose = document.querySelector('.close_btn');
const confirm_btn = document.querySelector(".confirm_btn");

//열기 버튼을 눌렀을 때 모달팝업이 열림
modalOpen.addEventListener('click',function(){
    //display 속성을 block로 변경
    modal.style.display = 'block';
});
//닫기 버튼을 눌렀을 때 모달팝업이 닫힘
modalClose.addEventListener('click',function(){
    //display 속성을 none으로 변경
    modal.style.display = 'none';
});

confirm_btn.addEventListener('click', function() {
    location.href="./login.html";
})