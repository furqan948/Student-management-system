# Student Management System

A full-stack web application built using Java (core) and Vanilla JavaScript.

## 🚀 Features
- **Full CRUD**: Create, Read, Update, and Delete students.
- **Search & Sort**: Filter by name and sort by ID or Name.
- **Validation**: Both frontend (JS) and backend (Java) validation.
- **Responsive UI**: Professional blue/white theme that works on different screen sizes.
- **No Dependencies**: Uses built-in Java `HttpServer` (no Tomcat/Maven required).

## 🛠 Project Structure
- `backend/src/com/sms`: Java sources (Model, Service, Controller, Main).
- `frontend/`: HTML, CSS, and JS files.
- `bin/`: Compiled class files.

## 🏃 Ready-to-Run Instructions

### Prerequisites
- JDK 11 or higher installed and added to your PATH.

### 1. Compile the Backend
Open a terminal in the project root (`d:\StudentProject`) and run:
```powershell
javac -d bin backend/src/com/sms/model/Student.java backend/src/com/sms/service/StudentService.java backend/src/com/sms/controller/StudentController.java backend/src/com/sms/Main.java
```

### 2. Run the Server
```powershell
java -cp bin com.sms.Main
```

### 3. Access the Application
Open your browser and navigate to:
[http://localhost:8080](http://localhost:8080)

## 📝 Usage
1. **Dashboard**: View total student count and quick links.
2. **Add Student**: Click "Add Student" in the navbar or dashboard. Fill the form (validation included).
3. **View Students**: See all records in a table. Use the search bar to filter by name.
4. **Edit/Delete**: Use the action buttons in the student table to modify or remove records.
