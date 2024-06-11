// index.js (Login script)
console.log("script is connected");

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

            // Save token and email in localStorage
            localStorage.setItem('token', responseData.token);

            // Redirect to home.html
            window.location.href = './pages/home/home.html';
        } else {
            console.error('Login failed');
            alert('Invalid email or password. Please try again.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while logging in.');
    }
});
