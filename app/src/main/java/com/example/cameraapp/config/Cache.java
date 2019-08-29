package com.example.cameraapp.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cameraapp.models.Approval;
import com.example.cameraapp.models.ChiefInvigilator;
import com.example.cameraapp.models.Evidence;
import com.example.cameraapp.models.Exam;
import com.example.cameraapp.models.Student;
import com.example.cameraapp.models.SubReport;
import com.example.cameraapp.models.User;
import com.google.gson.Gson;

public class Cache {

    public User getUserPrefCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("user_info", "");
        Gson gson = new Gson();
        User user = gson.fromJson(currentUser, User.class);
        return user;
    }

    public boolean setUserPrefCache(Context context, User user){
        Gson gson = new Gson();
        String currentUser = gson.toJson(user);
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", currentUser);
        editor.commit();
        if (sharedPreferences.getString("user_info", "") != ""){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean removeUserPrefCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_info");
        editor.commit();
        if (sharedPreferences.getString("user_info", "") == ""){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setStudentInfoCache(Context context, Student student){
        Gson gson = new Gson();
        String currentStudent = gson.toJson(student);
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("student_info", currentStudent);
        editor.commit();
        if (sharedPreferences.getString("student_info", "") != ""){
            return true;
        }
        else{
            return false;
        }
    }

    public Student getStudentInfoCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentStudent = sharedPreferences.getString("student_info", "");
        Gson gson = new Gson();
        Student student = gson.fromJson(currentStudent, Student.class);
        return student;
    }

    public boolean removeStudentInfoCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("student_info");
        editor.commit();
        if (sharedPreferences.getString("student_info", "") == ""){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setExamInfoCache(Context context, Exam exam){
        Gson gson = new Gson();
        String currentExamInfo = gson.toJson(exam);
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("exam_info", currentExamInfo);
        editor.commit();
        if (sharedPreferences.getString("exam_info", "") != ""){
            return true;
        }
        else{
            return false;
        }
    }

    public Exam getExamInfoCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentStudent = sharedPreferences.getString("exam_info", "");
        Gson gson = new Gson();
        Exam exam= gson.fromJson(currentStudent, Exam.class);
        return exam;
    }

    public boolean removeExamInfoCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("exam_info");
        editor.commit();
        if (sharedPreferences.getString("exam_info", "") == ""){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setEvidenceDetailsCache(Context context, Evidence evidence){
        Gson gson = new Gson();
        String currentImagePaths = gson.toJson(evidence);
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("evidence_details", currentImagePaths);
        editor.commit();
        if (sharedPreferences.getString("evidence_details", "") != ""){
            return true;
        }
        else{
            return false;
        }
    }

    public Evidence getEvidenceDetailsCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentImagePaths = sharedPreferences.getString("evidence_details", "");
        Gson gson = new Gson();
        Evidence evidence = gson.fromJson(currentImagePaths, Evidence.class);
        return evidence;
    }

    public boolean removeEvidenceDetailsCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("evidence_details");
        editor.commit();
        if (sharedPreferences.getString("evidence_details", "") == ""){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setSubReportDetailsCache(Context context, SubReport subReport){
        Gson gson = new Gson();
        String currentSubReportDetails = gson.toJson(subReport);
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("subreport_details", currentSubReportDetails);
        editor.commit();
        if (sharedPreferences.getString("subreport_details", "") != ""){
            return true;
        }
        else{
            return false;
        }
    }

    public SubReport getSubReportDetailsCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentSubReportDetails = sharedPreferences.getString("subreport_details", "");
        Gson gson = new Gson();
        SubReport subReport = gson.fromJson(currentSubReportDetails, SubReport.class);
        return subReport;
    }

    public boolean removeSubReportDetailsCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("subreport_details");
        editor.commit();
        if (sharedPreferences.getString("subreport_details", "") == ""){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setApprovalDetailsCache(Context context, Approval approval){
        Gson gson = new Gson();
        String currentApprovalDetails = gson.toJson(approval);
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("approval_details", currentApprovalDetails);
        editor.commit();
        if (sharedPreferences.getString("approval_details", "") != ""){
            return true;
        }
        else{
            return false;
        }
    }

    public Approval getApprovalDetailsCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentApprovalDetails = sharedPreferences.getString("approval_details", "");
        Gson gson = new Gson();
        Approval approval = gson.fromJson(currentApprovalDetails, Approval.class);
        return approval;
    }

    public boolean removeApprovalDetailsCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("approval_details");
        editor.commit();
        if (sharedPreferences.getString("approval_details", "") == ""){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setChiefInvDetailsCache(Context context, ChiefInvigilator invigilator){
        Gson gson = new Gson();
        String chiefDetails = gson.toJson(invigilator);
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("chief_details", chiefDetails);
        editor.commit();
        if (sharedPreferences.getString("chief_details", "") != ""){
            return true;
        }
        else{
            return false;
        }
    }

    public ChiefInvigilator getChiefInvDetailsCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String chiefDetails = sharedPreferences.getString("chief_details", "");
        Gson gson = new Gson();
        ChiefInvigilator invigilator = gson.fromJson(chiefDetails, ChiefInvigilator.class);
        return invigilator;
    }

    public boolean removeChiefInvDetailsCache(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("chief_details");
        editor.commit();
        if (sharedPreferences.getString("chief_details", "") == ""){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean removeAllReportCache(Context context){

        if (removeStudentInfoCache(context) && removeExamInfoCache(context) && removeEvidenceDetailsCache(context) && removeSubReportDetailsCache(context) && removeApprovalDetailsCache(context)){

            return true;
        }
        else{
            return false;
        }
    }


}
