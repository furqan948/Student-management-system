package com.sms.model;

public class Student {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String course;
    private String dob;
    private String gender;
    private String address;

    public Student() {}

    public Student(int id, String name, String email, String phone, String course, String dob, String gender, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", course='" + course + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String toJson() {
        return "{" +
                "\"id\":" + id + "," +
                "\"name\":\"" + escapeJson(name) + "\"," +
                "\"email\":\"" + escapeJson(email) + "\"," +
                "\"phone\":\"" + escapeJson(phone) + "\"," +
                "\"course\":\"" + escapeJson(course) + "\"," +
                "\"dob\":\"" + escapeJson(dob) + "\"," +
                "\"gender\":\"" + escapeJson(gender) + "\"," +
                "\"address\":\"" + escapeJson(address) + "\"" +
                "}";
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
