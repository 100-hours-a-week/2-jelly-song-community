let $header_profile = document.querySelector(".header-profile");
let $drop_down = document.querySelector(".drop-down");

activateDropDownMenu();

function activateDropDownMenu() {
    $header_profile.addEventListener("click", (e) => {
        if ($drop_down.style.display == "flex") {
            $drop_down.style.display = "none";
        } else {
            $drop_down.style.display = "flex";
        }
    })
}