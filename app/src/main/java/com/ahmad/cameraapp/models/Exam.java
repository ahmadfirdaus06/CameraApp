package com.ahmad.cameraapp.models;

public class Exam {
    private String courseCode = "";
    private String courseName = "";
    private String examVenue = "";
    private String examDate = "";
    private String examTime = "";

    public Exam() {
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getExamVenue() {
        return examVenue;
    }

    public void setExamVenue(String examVenue) {
        this.examVenue = examVenue;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }
}
