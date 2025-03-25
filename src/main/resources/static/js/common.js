import { API_BASE_URL } from "./config.js"

let $header_profile = document.querySelector(".header-profile");
let $drop_down = document.querySelector(".drop-down");
const $logoutButton = document.querySelector(".drop-down-logout");

activateDropDownMenu();
if ($logoutButton != null) {
    activateLogoutDropDownMenu();
}


function activateDropDownMenu() {
    if ($header_profile != null) {
        $header_profile.addEventListener("click", (e) => {
            if ($drop_down.style.display == "flex") {
                $drop_down.style.display = "none";
            } else {
                $drop_down.style.display = "flex";
            }
        })
    }
}

function activateLogoutDropDownMenu() {
    $logoutButton.addEventListener("click", (e) => {
        e.preventDefault();

        const token = localStorage.getItem("accessToken");
        if (!token) {
            alert("이미 로그아웃 상태입니다.");
            window.location.href = "./login.html";
            return;
        }

        console.log("여기까진 옴1")
        fetch(`${API_BASE_URL}/logout`, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            credentials: "include",
        })
            .then((res) => res.json())
            .then((data) => {
                if (data.isSuccess === "true") {
                    alert(data.message || "로그아웃 성공!");
                    localStorage.removeItem("accessToken");
                    window.location.href = "./login.html";
                } else {
                    alert("로그아웃 실패: " + (data.message || ""));
                }
            })
            .catch((err) => {
                console.error("로그아웃 오류:", err);
                alert("로그아웃 중 오류가 발생했습니다.");
            });
    })
}