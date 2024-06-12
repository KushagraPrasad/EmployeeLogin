console.log("script is connected");

document.addEventListener('DOMContentLoaded', async function () {
    const token = localStorage.getItem('token');

    if (token) {
        try {
            const response = await fetch('http://localhost:9999/auth/validate', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                console.log('Token is valid');
                window.location.href = './pages/home/home.html'; // Redirect to home page
            } else {
                console.log('Token is invalid');
            }
        } catch (error) {
            console.error('Error validating token:', error);
        }
    }
});

document.getElementById('loginForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('http://localhost:9999/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const responseData = await response.json();
            console.log('Login successful');
            console.log('Token:', responseData.token);

            localStorage.setItem('token', responseData.token);

            window.location.href = './pages/home/home.html'; // Redirect to home page after successful login
        } else {
            console.error('Login failed');
            alert('Invalid email or password. Please try again.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while logging in.');
    }
});
