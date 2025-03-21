import {getValidAccessToken, parseJwt} from "./auth.js";

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

uploadProfile();
validateWheneverTyped();
activateTost();
activateModal();

// 예: DOMContentLoaded 시점에 실행
(async function () {
    try {
        console.log("야아아아아아아아아 왜 안나오냐")
        const token = await getValidAccessToken();
        if (!token) return;
        let jwtContent = parseJwt(token);

        // 1. 유저 정보 조회 API 호출
        const response = await fetch(`http://localhost:8080/users/${jwtContent.username}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
            },
            credentials: "include",
        });

        const result = await response.json();
        console.log("result:" + result);

        if (!result.isSuccess) {
            // 조회 실패 시 처리
            console.error('유저 정보를 가져오지 못했습니다.');
            return;
        }

        // 2. API 응답에서 필요한 데이터 꺼내기
        const {nickname, userProfileImageUrl} = result.data;

        // 3. 닉네임, 프로필 이미지 적용
        if (nickname) {
            $nickname_form.value = nickname;
        }

        console.log(`url(${userProfileImageUrl})`)
        if (userProfileImageUrl) {
            $preview.style.backgroundImage = `url(${userProfileImageUrl})`;
            $preview.style.backgroundRepeat = "no-repeat";
            $preview.style.backgroundSize = "cover";
            $preview.style.backgroundPosition = "center";
            $plus_font.innerHTML = "";
        }

        // 4. 폼에 "input" 이벤트를 인위적으로 발생시켜서
        //    기존의 유효성 검사 로직과 버튼 활성화 로직이 동작하도록 함
        $layout_form.dispatchEvent(new Event("input", {bubbles: true}));

    } catch (err) {
        console.error('회원정보 조회 에러:', err);
    }
})();

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

            const response = await fetch(`http://localhost:8080/users/${jwtContent.username}`, {
                method: "PATCH",
                headers: {
                    "Authorization": `Bearer ${token}`,
                },
                body: formData
            });

            const result = await response.json();

            if (result.isSuccess) {
                tostOn();
            } else {
                console.error("업로드 실패:", result.message);
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