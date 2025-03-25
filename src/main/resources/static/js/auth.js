import { API_BASE_URL } from "./config.js"
export function parseJwt(token) {
    const base64Url = token.split(".")[1];
    if (!base64Url) return null;

    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    try {
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split("")
                .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
                .join("")
        );
        return JSON.parse(jsonPayload);
    } catch (err) {
        console.error("parseJwt 에러:", err);
        return null;
    }
}

function isTokenExpired(token) {
    const payload = parseJwt(token);
    if (!payload || !payload.exp) return true;
    const now = Math.floor(Date.now() / 1000);
    return now > payload.exp;
}

async function reissueAccessToken() {
    try {
        const response = await fetch(`${API_BASE_URL}/reissue`, {
            method: "POST",
            credentials: "include",
        });
        const data = await response.json();
        if (data.isSuccess && response.headers.get("Authorization")) {
            const newToken = response.headers.get("Authorization");
            localStorage.setItem("accessToken", newToken);
            return newToken;
        } else {
            alert("세션이 만료되었습니다. 다시 로그인해주세요.");
            localStorage.removeItem("accessToken");
            window.location.href = "./login.html";
            return null;
        }
    } catch (err) {
        console.error("토큰 재발급 에러:", err);
        alert("오류가 발생했습니다. 다시 로그인해주세요.");
        localStorage.removeItem("accessToken");
        window.location.href = "./login.html";
        return null;
    }
}

export async function getValidAccessToken() {
    let token = localStorage.getItem("accessToken");

    if (!token) {
        alert("로그인이 필요합니다.");
        window.location.href = "./login.html";
        return null;
    }

    if (isTokenExpired(token)) {
        console.log("access 토큰 만료됨. reissueAccessToken() 호출");
        token = await reissueAccessToken();
    }

    return token;
}
