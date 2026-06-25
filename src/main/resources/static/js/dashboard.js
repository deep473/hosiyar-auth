const token = localStorage.getItem("jwtToken");

const userInfo = document.getElementById("userInfo");
const apiResponse = document.getElementById("apiResponse");
const resultBtn = document.getElementById("resultBtn");
const createBtn = document.getElementById("createBtn");
const logoutBtn = document.getElementById("logoutBtn");

if (!token) {
    window.location.href = "/login.html";
}

displayUserInfo();

resultBtn.addEventListener("click", function () {
    callProtectedApi("/assessments/result");
});

createBtn.addEventListener("click", function () {
    callProtectedApi("/assessments/create");
});

logoutBtn.addEventListener("click", async function () {
    try {
        const response = await fetch("/auth/logout", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        const result = await response.text();

        localStorage.removeItem("jwtToken");

        alert(result);

        window.location.href = "/login.html";

    } catch (error) {
        localStorage.removeItem("jwtToken");
        window.location.href = "/login.html";
    }
});

async function callProtectedApi(url) {
    try {
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        const result = await response.text();

        if (response.ok) {
            apiResponse.textContent = result;
        } else {
            apiResponse.textContent =
                "Status: " + response.status + "\n" + result;
        }

    } catch (error) {
        apiResponse.textContent = "Something went wrong while calling API.";
    }
}

function displayUserInfo() {
    try {
        const payload = parseJwt(token);

        let username = payload.sub;
        let roles = payload.roles;

        userInfo.textContent = "Logged in as: " + username + " | Role: " + roles;

    } catch (error) {
        userInfo.textContent = "Logged in";
    }
}

function parseJwt(token) {
    const base64Payload = token.split(".")[1];

    const base64 = base64Payload.replace(/-/g, "+").replace(/_/g, "/");

    const jsonPayload = decodeURIComponent(
        atob(base64)
            .split("")
            .map(function (character) {
                return "%" + ("00" + character.charCodeAt(0).toString(16)).slice(-2);
            })
            .join("")
    );

    return JSON.parse(jsonPayload);
}