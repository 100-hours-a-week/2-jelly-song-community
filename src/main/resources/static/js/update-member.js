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

validateWheneverTyped();
activateTost();
activateModal();


function validateWheneverTyped() {
    $update_member_form.addEventListener("input", (event) => {
        let validation = true;

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
    $update_member_form.addEventListener("submit", (event) => {
        event.preventDefault();
        if ($button.classList.contains("button-disable")) {
            return false;
        }
        tostOn()
    })
    function tostOn(){
        tostMessage.classList.add('active');
        setTimeout(function(){
            tostMessage.classList.remove('active');
        },1000);
    }

}

function activateModal() {
    modalOpen.addEventListener('click', function () {
        modal.style.display = 'block';
    });
    modalClose.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    confirm_btn.addEventListener('click', function () {
        location.href = "./login.html";
    })

    preventHrefDropMemberButton();
    function preventHrefDropMemberButton() {
        $drop_member_button.addEventListener("click", (e) => {
            e.preventDefault();
        })
    }
}