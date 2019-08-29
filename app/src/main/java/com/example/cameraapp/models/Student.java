package com.example.cameraapp.models;

public class Student {
    private String studentId = "";
    private String matricId = "";
    private String name = "";
    private String email = "";
    private String programme = "";
    private String contactNo = "";
    private String icOrPassport = "";

    public Student() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMatricId() {
        return matricId;
    }

    public void setMatricId(String matricId) {
        this.matricId = matricId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getIcOrPassport() {
        return icOrPassport;
    }

    public void setIcOrPassport(String icOrPassport) {
        this.icOrPassport = icOrPassport;
    }
}
