document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('taskForm').addEventListener('submit', async function(event) {
        event.preventDefault();

        const description = document.getElementById('description').value;
        const dueDate = document.getElementById('dueDate').value;
        const assigneeEmail = document.getElementById('assigneeEmail').value;
        const token = localStorage.getItem('token');

        const task = {
            description: description,
            dueDate: dueDate,
            assigneeEmail: assigneeEmail
        };

        try {
            const response = await fetch('http://localhost:9999/tasks/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(task)
            });

            if (response.ok) {
                alert('Task added successfully');
            } else {
                const error = await response.json();
                alert(`Error adding task: ${error.message}`);
            }
        } catch (error) {
            console.error('Error adding task:', error);
            alert('An error occurred while adding the task.');
        }
    });

    // Add event listener for "Go Back to Home" button
    document.getElementById('goBackBtn').addEventListener('click', function() {
        window.location.href = '/pages/home/home.html';
    });
});
