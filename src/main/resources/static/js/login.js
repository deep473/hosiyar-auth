const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");

loginForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const loginData = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };

    try {
        const response = await fetch("/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(loginData)
        });

        if (response.ok) {
            const data = await response.json();

            localStorage.setItem("jwtToken", data.token);

            showMessage("Login successful!", "success");

            setTimeout(function () {
                window.location.href = "/dashboard.html";
            }, 800);

        } else {
            const errorMessage = await response.text();
            showMessage(errorMessage, "error");
        }

    } catch (error) {
        showMessage("Something went wrong while logging in.", "error");
    }
});

function showMessage(text, type) {
    message.textContent = text;
    message.className = "message " + type;
}