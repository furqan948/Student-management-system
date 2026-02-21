const API_URL = 'http://localhost:8080/api';

async function fetchStudents() {
    const response = await fetch(`${API_URL}/students`);
    return await response.json();
}

async function fetchStudentById(id) {
    const response = await fetch(`${API_URL}/students/${id}`);
    return await response.json();
}

async function createStudent(student) {
    const response = await fetch(`${API_URL}/students`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(student)
    });
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.error || 'Failed to create student');
    }
    return data;
}

async function updateStudent(id, student) {
    const response = await fetch(`${API_URL}/students/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(student)
    });
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.error || 'Failed to update student');
    }
    return data;
}

async function deleteStudent(id) {
    const response = await fetch(`${API_URL}/students/${id}`, {
        method: 'DELETE'
    });
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.error || 'Failed to delete student');
    }
    return data;
}

function showToast(message, type = 'success') {
    alert(message); // Simple alert for now, can be improved to a toast UI
}
