const registerForm = document.getElementById("registerForm");
const message = document.getElementById("message");

registerForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const registerData = {
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        role: document.getElementById("role").value
    };

    try {
        const response = await fetch("/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(registerData)
        });

        const result = await response.text();

        if (response.ok) {
            showMessage(result, "success");

            setTimeout(function () {
                window.location.href = "/login.html";
            }, 1200);
        } else {
            showMessage(result, "error");
        }

    } catch (error) {
        showMessage("Something went wrong while registering.", "error");
    }
});

function showMessage(text, type) {
    message.textContent = text;
    message.className = "message " + type;
}