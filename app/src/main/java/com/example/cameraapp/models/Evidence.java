package com.example.cameraapp.models;

import java.util.ArrayList;

public class Evidence {
    private ArrayList<String> imagePaths = new ArrayList<>();

    public Evidence() {
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
