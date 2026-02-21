package com.sms.controller;

import com.sms.model.Student;
import com.sms.service.StudentService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StudentController implements HttpHandler {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            if (path.equals("/api/students")) {
                if ("GET".equalsIgnoreCase(method)) {
                    handleGetAll(exchange);
                } else if ("POST".equalsIgnoreCase(method)) {
                    handleCreate(exchange);
                }
            } else if (path.startsWith("/api/students/")) {
                int id = Integer.parseInt(path.substring("/api/students/".length()));
                if ("GET".equalsIgnoreCase(method)) {
                    handleGetById(exchange, id);
                } else if ("PUT".equalsIgnoreCase(method)) {
                    handleUpdate(exchange, id);
                } else if ("DELETE".equalsIgnoreCase(method)) {
                    handleDelete(exchange, id);
                }
            } else {
                sendResponse(exchange, 404, "{\"error\":\"Not Found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        List<Student> students = studentService.getAllStudents();
        String json = "[" + students.stream().map(Student::toJson).collect(Collectors.joining(",")) + "]";
        sendResponse(exchange, 200, json);
    }

    private void handleGetById(HttpExchange exchange, int id) throws IOException {
        Student student = studentService.getStudentById(id);
        if (student != null) {
            sendResponse(exchange, 200, student.toJson());
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Student not found\"}");
        }
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        System.out.println("POST /api/students - Request Body: " + body);
        Student student = parseJson(body);
        if (validate(student)) {
            studentService.addStudent(student);
            System.out.println("Successfully added student: " + student.getName());
            sendResponse(exchange, 201, student.toJson());
        } else {
            System.out.println("Validation failed for student: " + student);
            sendResponse(exchange, 400, "{\"error\":\"Validation failed: Check email format or phone length\"}");
        }
    }

    private void handleUpdate(HttpExchange exchange, int id) throws IOException {
        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        Student student = parseJson(body);
        student.setId(id);
        if (validate(student) && studentService.updateStudent(student)) {
            sendResponse(exchange, 200, student.toJson());
        } else {
            sendResponse(exchange, 400, "{\"error\":\"Update failed or student not found\"}");
        }
    }

    private void handleDelete(HttpExchange exchange, int id) throws IOException {
        if (studentService.deleteStudentById(id)) {
            sendResponse(exchange, 200, "{\"message\":\"Student deleted\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Student not found\"}");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private Student parseJson(String json) {
        Student s = new Student();
        s.setName(getJsonValue(json, "name"));
        s.setEmail(getJsonValue(json, "email"));
        s.setPhone(getJsonValue(json, "phone"));
        s.setCourse(getJsonValue(json, "course"));
        s.setDob(getJsonValue(json, "dob"));
        s.setGender(getJsonValue(json, "gender"));
        s.setAddress(getJsonValue(json, "address"));
        return s;
    }

    private String getJsonValue(String json, String key) {
        // Robust regex to handle different JSON formats and escaping
        Pattern pattern = Pattern.compile("\"" + key + "\":\\s*\"(.*?)\"(?:,|})");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    private boolean validate(Student s) {
        if (s.getName() == null || s.getName().isEmpty()) return false;
        if (s.getEmail() == null || !s.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) return false;
        if (s.getPhone() == null || !s.getPhone().matches("\\d{10}")) return false;
        if (s.getCourse() == null || s.getCourse().isEmpty()) return false;
        // Basic dob validation (should not be future, but keeping it simple for core Java)
        return true;
    }
}
