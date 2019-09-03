package com.example.cameraapp.models;

public class Misconduct {
    private String misconductId = "";
    private String type = "";
    private String reportId = "";

    public Misconduct() {
    }

    public String getMisconductId() {
        return misconductId;
    }

    public void setMisconductId(String misconductId) {
        this.misconductId = misconductId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
}
