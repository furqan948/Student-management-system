package com.sms.service;

import com.sms.model.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StudentService {
    private final List<Student> students = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public StudentService() {
        // Initial sample data
        addStudent(new Student(0, "John Doe", "john@example.com", "1234567890", "Computer Science", "2000-01-01", "Male", "123 Street, NY"));
        addStudent(new Student(0, "Jane Smith", "jane@example.com", "0987654321", "Information Technology", "2001-02-02", "Female", "456 Avenue, CA"));
    }

    public synchronized Student addStudent(Student student) {
        student.setId(idGenerator.getAndIncrement());
        students.add(student);
        return student;
    }

    public synchronized List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public synchronized Student getStudentById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized boolean updateStudent(Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == updatedStudent.getId()) {
                students.set(i, updatedStudent);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteStudentById(int id) {
        return students.removeIf(s -> s.getId() == id);
    }

    public List<Student> searchStudentByName(String name) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Student> sortStudents(String criteria, boolean ascending) {
        return students.stream()
                .sorted((s1, s2) -> {
                    int result = 0;
                    if ("name".equalsIgnoreCase(criteria)) {
                        result = s1.getName().compareToIgnoreCase(s2.getName());
                    } else if ("id".equalsIgnoreCase(criteria)) {
                        result = Integer.compare(s1.getId(), s2.getId());
                    }
                    return ascending ? result : -result;
                })
                .collect(Collectors.toList());
    }
}
