const API_BASE_URL = "https://localhost:8443/api"; // Pas aan naar jouw backend URL

async function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const response = await fetch(`${API_BASE_URL}/auth/authenticate`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });

    const data = await response.json();
    if (response.ok) {
        document.getElementById("jwtToken").value = data.token;
        document.getElementById("subscriptions").style.display = "block";
        document.getElementById("shareSubscription").style.display = "block";
    } else {
        alert("Login mislukt: " + data.message);
    }
}

async function getSubscriptions() {
    const token = document.getElementById("jwtToken").value;
    const response = await fetch(`${API_BASE_URL}/categories`, {
        method: "GET",
        headers: { "Authorization": "Bearer " + token }
    });

    const data = await response.json();
    if (response.ok) {
        const subscriptionList = document.getElementById("subscriptionList");
        subscriptionList.innerHTML = "";
        data.subscribedCategories.forEach(sub => {
            const li = document.createElement("li");
            li.textContent = `${sub.name} - Resterende inhoud: ${sub.remainingContent}`;
            subscriptionList.appendChild(li);
        });
    } else {
        alert("Kan abonnementen niet ophalen");
    }
}

async function shareSubscription() {
    const token = document.getElementById("jwtToken").value;
    const customerEmail = document.getElementById("customerEmail").value;
    const sharedCategory = document.getElementById("sharedCategory").value;

    const response = await fetch(`${API_BASE_URL}/subscriptions/share`, {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ customer: customerEmail, subscribedCategory: sharedCategory })
    });

    const data = await response.json();
    alert(data.message);
}
