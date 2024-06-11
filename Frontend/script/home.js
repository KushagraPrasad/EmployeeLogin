document.addEventListener('DOMContentLoaded', async function () {
    const token = localStorage.getItem('token');

    try {
        const response = await fetch('http://localhost:9999/user/details', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const userDetails = await response.json();
            displayUserDetails(userDetails);
        } else {
            console.error('Failed to fetch user details');
        }
    } catch (error) {
        console.error('Error fetching user details:', error);
    }
});

document.getElementById('markPresentBtn').addEventListener('click', async function () {
    const token = localStorage.getItem('token');

    try {
        // Disable the button to prevent multiple clicks
        this.disabled = true;

        const response = await fetch('http://localhost:9999/attendance/mark', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        const message = await response.text();

        if (response.ok) {
            alert(message);
        } else {
            alert(`Failed to mark attendance: ${message}`);
        }
    } catch (error) {
        console.error('Error marking attendance:', error);
        alert('An error occurred while marking attendance.');
    } finally {
        // Re-enable the button
        this.disabled = false;
    }
});

document.getElementById('logoutBtn').addEventListener('click', function () {
    localStorage.removeItem('token');
    window.location.href = '../../index.html';
});

function displayUserDetails(userDetails) {
    const userDetailsElement = document.getElementById('userDetails');
    userDetailsElement.innerHTML = `
        <p>Email: ${userDetails.email}</p>
        <p>Name: ${userDetails.firstName} ${userDetails.lastName}</p>
        <p>Mobile: ${userDetails.mobile}</p>
        <p>Role: ${userDetails.role.name}</p>
    `;
}
