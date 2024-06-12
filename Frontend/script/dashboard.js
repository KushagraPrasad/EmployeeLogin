document.addEventListener('DOMContentLoaded', async function () {
    const token = localStorage.getItem('token');

    try {
        const response = await fetch('http://localhost:9999/tasks', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const tasks = await response.json();
            displayTasks(tasks);
        } else {
            console.error('Failed to fetch tasks');
        }
    } catch (error) {
        console.error('Error fetching tasks:', error);
    }
});

function displayTasks(tasks) {
    const tasksElement = document.getElementById('tasks');
    tasksElement.innerHTML = '';

    if (tasks.length === 0) {
        tasksElement.innerHTML = '<p>No tasks assigned.</p>';
    } else {
        const ul = document.createElement('ul');
        tasks.forEach(task => {
            const li = document.createElement('li');
            li.textContent = `Task: ${task.description}, Due Date: ${new Date(task.dueDate).toLocaleDateString()}`;
            ul.appendChild(li);
        });
        tasksElement.appendChild(ul);
    }
}
