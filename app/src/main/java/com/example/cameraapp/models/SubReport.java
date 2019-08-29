package com.example.cameraapp.models;

import java.util.ArrayList;

public class SubReport {
    private ArrayList<String> typeOfMisconduct = new ArrayList<>();
    private String timeOfMisconduct = "";
    private String misconductDesc = "";
    private String actionTaken = "";

    public SubReport() {
    }

    public ArrayList<String> getTypeOfMisconduct() {
        return typeOfMisconduct;
    }

    public void setTypeOfMisconduct(ArrayList<String> typeOfMisconduct) {
        this.typeOfMisconduct = typeOfMisconduct;
    }

    public String getTimeOfMisconduct() {
        return timeOfMisconduct;
    }

    public void setTimeOfMisconduct(String timeOfMisconduct) {
        this.timeOfMisconduct = timeOfMisconduct;
    }

    public String getMisconductDesc() {
        return misconductDesc;
    }

    public void setMisconductDesc(String misconductDesc) {
        this.misconductDesc = misconductDesc;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }
}
