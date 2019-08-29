package com.example.cameraapp.models;

import java.util.ArrayList;

public class Report {
    private String studentId = "";
    private String courseCode = "";
    private String courseName = "";
    private String examVenue = "";
    private String examDate = "";
    private String examTime = "";
    private ArrayList<String> typeOfMisconduct = new ArrayList<>();
    private String misconductTime = "";
    private String misconductDescription = "";
    private String actionTaken = "";
    private String reporterUserId = "";
    private String superiorUserId = "";
    private String witness1Name = "";
    private String witness1ContactNo = "";
    private String witness1Email = "";
    private String witness2Name = "";
    private String witness2ContactNo = "";
    private String witness2Email = "";

    public Report() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public ArrayList<String> getTypeOfMisconduct() {
        return typeOfMisconduct;
    }

    public void setTypeOfMisconduct(ArrayList<String> typeOfMisconduct) {
        this.typeOfMisconduct = typeOfMisconduct;
    }

    public String getMisconductTime() {
        return misconductTime;
    }

    public void setMisconductTime(String misconductTime) {
        this.misconductTime = misconductTime;
    }

    public String getMisconductDescription() {
        return misconductDescription;
    }

    public void setMisconductDescription(String misconductDescription) {
        this.misconductDescription = misconductDescription;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getReporterUserId() {
        return reporterUserId;
    }

    public void setReporterUserId(String reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public String getSuperiorUserId() {
        return superiorUserId;
    }

    public void setSuperiorUserId(String superiorUserId) {
        this.superiorUserId = superiorUserId;
    }

    public String getWitness1Name() {
        return witness1Name;
    }

    public void setWitness1Name(String witness1Name) {
        this.witness1Name = witness1Name;
    }

    public String getWitness1ContactNo() {
        return witness1ContactNo;
    }

    public void setWitness1ContactNo(String witness1ContactNo) {
        this.witness1ContactNo = witness1ContactNo;
    }

    public String getWitness1Email() {
        return witness1Email;
    }

    public void setWitness1Email(String witness1Email) {
        this.witness1Email = witness1Email;
    }

    public String getWitness2Name() {
        return witness2Name;
    }

    public void setWitness2Name(String witness2Name) {
        this.witness2Name = witness2Name;
    }

    public String getWitness2ContactNo() {
        return witness2ContactNo;
    }

    public void setWitness2ContactNo(String witness2ContactNo) {
        this.witness2ContactNo = witness2ContactNo;
    }

    public String getWitness2Email() {
        return witness2Email;
    }

    public void setWitness2Email(String witness2Email) {
        this.witness2Email = witness2Email;
    }
}
