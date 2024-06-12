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
            await displayUserDetails(userDetails, token); // Pass token to displayUserDetails
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
            console.log(message);
            alert(message);
            document.getElementById('dashboardBtn').classList.remove('hidden'); // Show the dashboard button
            this.classList.add('hidden'); // Hide the "Mark Present" button
        } else if (response.status === 400 && message.includes("Attendance already marked")) {
            alert(message);
            document.getElementById('dashboardBtn').classList.remove('hidden'); // Show the dashboard button
        } else {
            alert(`Failed to mark attendance: ${message}`);
            console.log(message);
        }
    } catch (error) {
        console.error('Error marking attendance:', error);
        alert('An error occurred while marking attendance.');
    } finally {
        this.disabled = false;
    }
});

document.getElementById('logoutBtn').addEventListener('click', function () {
    localStorage.removeItem('token');
    window.location.href = '../../index.html';
});

document.getElementById('addTaskBtn').addEventListener('click', function () {
    window.location.href = './add_task.html'; // Redirect to add task page
});

async function displayUserDetails(userDetails, token) { // Accept token as a parameter
    const userDetailsElement = document.getElementById('userDetails');
    userDetailsElement.innerHTML = `
        <p>Email: ${userDetails.email}</p>
        <p>Name: ${userDetails.firstName} ${userDetails.lastName}</p>
        <p>Mobile: ${userDetails.mobile}</p>
        <p>Role: ${userDetails.role.name}</p>
    `;

    if (userDetails.role.name === 'ADMIN') {
        document.getElementById('markPresentBtn').classList.add('hidden'); // Hide the "Mark Present" button for admins
        document.getElementById('dashboardBtn').classList.remove('hidden'); // Show the "Go to Dashboard" button for admins
        document.getElementById('addTaskBtn').classList.remove('hidden'); // Show the "Add Task" button for admins
    } else {
        const attendanceStatus = await checkAttendanceStatus(token);
        if (attendanceStatus === 'Attendance already marked for today') {
            document.getElementById('markPresentBtn').classList.add('hidden'); // Hide the "Mark Present" button for employees if attendance already marked
            document.getElementById('dashboardBtn').classList.remove('hidden'); // Show the "Go to Dashboard" button for employees
        } else {
            document.getElementById('dashboardBtn').classList.add('hidden'); // Hide the "Go to Dashboard" button for employees
            document.getElementById('markPresentBtn').classList.remove('hidden'); // Show the "Mark Present" button for employees
            document.getElementById('addTaskBtn').classList.add('hidden'); // Hide the "Add Task" button for employees
        }
    }
}

async function checkAttendanceStatus(token) {
    try {
        const response = await fetch('http://localhost:9999/attendance/status', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        const statusMessage = await response.text();

        if (response.ok) {
            return statusMessage;
        } else {
            console.error('Error checking attendance status:', statusMessage);
            return 'Error';
        }
    } catch (error) {
        console.error('Error checking attendance status:', error);
        return 'Error';
    }
}
