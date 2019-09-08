package com.example.cameraapp.config;

import android.content.Context;

public class DataSource {
//    private String host = "192.168.43.75";
//    private String host = "192.168.1.215";
//    private String host = "localhost";
    private String host = "semms.ddns.net";
    private String loginUrl = "http://" + host + ":8080/iukl-semms/semms/api/user/login-mobile.php";
    private String logoutUrl = "http://" + host + ":8080/iukl-semms/semms/api/user/logout-user.php";
    private String getStudentUrl = "http://" + host + ":8080/iukl-semms/semms/api/student/read.php";
    private String getInvigilatorUrl = "http://" + host + ":8080/iukl-semms/semms/api/user/read-by-staff_id.php";
    private String uploadImageUrl = "http://" + host + ":8080/iukl-semms/semms/api/report/upload-images.php";
    private String uploadReportDetailsUrl = "http://" + host + ":8080/iukl-semms/semms/api/report/upload-report-details.php";
    private String uploadImageDetailsUrl = "http://" + host + ":8080/iukl-semms/semms/api/report/upload-image-details.php";
    private String getAllReportsDataUrl = "http://" + host + ":8080/iukl-semms/semms/api/report/fetch-all-reports-details.php";
    private String imagePath = "http://" + host + ":8080/semms-uploads/";
    private String updateUserData = "http://" + host + ":8080/iukl-semms/semms/api/user/update-user-data-mobile.php";

    public DataSource() {
    }

    public String getHost() {
        return host;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public String getGetStudentUrl() {
        return getStudentUrl;
    }

    public String getGetInvigilatorUrl() {
        return getInvigilatorUrl;
    }

    public String getUploadImageUrl() {
        return uploadImageUrl;
    }

    public String getUploadReportDetailsUrl() {
        return uploadReportDetailsUrl;
    }

    public String getUploadImageDetailsUrl() {
        return uploadImageDetailsUrl;
    }

    public String getGetAllReportsDataUrl() {
        return getAllReportsDataUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getUpdateUserData() {
        return updateUserData;
    }
}
