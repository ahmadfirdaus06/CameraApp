package com.example.cameraapp.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cameraapp.miscellanous.GetDataAsync;
import com.example.cameraapp.models.Approval;
import com.example.cameraapp.models.ChiefInvigilator;
import com.example.cameraapp.models.Evidence;
import com.example.cameraapp.models.Exam;
import com.example.cameraapp.models.Report;
import com.example.cameraapp.models.Student;
import com.example.cameraapp.models.SubReport;
import com.example.cameraapp.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cache {


    private Context context;
    private ConnectionCheck conn;
    private DataSource dataSource;
    private RequestQueue requestQueue;
    private JsonObjectRequest objectRequest;
    SQLiteHelper db;
    GetDataAsync getDataAsync;

    public Cache(Context context) {
        this.context = context;
        conn = new ConnectionCheck(this.context);
        dataSource = new DataSource();
    }

    public User getUserPrefCache(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("user_info", "");
        Gson gson = new Gson();
        User user = gson.fromJson(currentUser, User.class);
        return user;
    }

    public boolean setUserPrefCache(User user){
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

    public boolean removeUserPrefCache(){
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

    public boolean setStudentInfoCache(Student student){
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

    public Student getStudentInfoCache(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentStudent = sharedPreferences.getString("student_info", "");
        Gson gson = new Gson();
        Student student = gson.fromJson(currentStudent, Student.class);
        return student;
    }

    public boolean removeStudentInfoCache(){
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

    public boolean setExamInfoCache(Exam exam){
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

    public Exam getExamInfoCache(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentStudent = sharedPreferences.getString("exam_info", "");
        Gson gson = new Gson();
        Exam exam= gson.fromJson(currentStudent, Exam.class);
        return exam;
    }

    public boolean removeExamInfoCache(){
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

    public boolean setEvidenceDetailsCache(Evidence evidence){
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

    public Evidence getEvidenceDetailsCache(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentImagePaths = sharedPreferences.getString("evidence_details", "");
        Gson gson = new Gson();
        Evidence evidence = gson.fromJson(currentImagePaths, Evidence.class);
        return evidence;
    }

    public boolean removeEvidenceDetailsCache(){
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

    public boolean setSubReportDetailsCache(SubReport subReport){
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

    public SubReport getSubReportDetailsCache(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentSubReportDetails = sharedPreferences.getString("subreport_details", "");
        Gson gson = new Gson();
        SubReport subReport = gson.fromJson(currentSubReportDetails, SubReport.class);
        return subReport;
    }

    public boolean removeSubReportDetailsCache(){
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

    public boolean setApprovalDetailsCache(Approval approval){
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

    public Approval getApprovalDetailsCache(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String currentApprovalDetails = sharedPreferences.getString("approval_details", "");
        Gson gson = new Gson();
        Approval approval = gson.fromJson(currentApprovalDetails, Approval.class);
        return approval;
    }

    public boolean removeApprovalDetailsCache(){
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

    public boolean setChiefInvDetailsCache(ChiefInvigilator invigilator){
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

    public ChiefInvigilator getChiefInvDetailsCache(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("REPORT_FORM_DETAILS", Context.MODE_PRIVATE);
        String chiefDetails = sharedPreferences.getString("chief_details", "");
        Gson gson = new Gson();
        ChiefInvigilator invigilator = gson.fromJson(chiefDetails, ChiefInvigilator.class);
        return invigilator;
    }

    public boolean removeChiefInvDetailsCache(){
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

    public void getData(){
        if (getUserPrefCache() != null){
            String userId = getUserPrefCache().getUserID();
            String url = dataSource.getGetAllReportsDataUrl();
            JSONObject object = new JSONObject();
            try {
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (conn.isOnline()){
                requestQueue = Volley.newRequestQueue(context);
                objectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String message = "";
                                try {
                                    message = response.getString("message");
                                    if (message.equals("Success")){
                                        JSONArray users = response.getJSONArray("userList");
                                        JSONArray students = response.getJSONArray("studentList");
                                        JSONArray reports = response.getJSONArray("reportList");
                                        JSONArray attachments = response.getJSONArray("attachmentList");
                                        JSONArray misconducts = response.getJSONArray("misconductList");
                                        GetDataAsync getDataAsync = new GetDataAsync(context);
                                        getDataAsync.execute(users, students, reports, attachments, misconducts);
                                    }
                                } catch (JSONException e) {
                                    System.out.println(e);
                                    Toast.makeText(context, "Couln't fetch data, Try again later",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Couln't fetch data, Try again later",Toast.LENGTH_SHORT).show();
                    }
                });

                requestQueue.add(objectRequest);
            }
            else{
                Toast.makeText(context, "Couln't fetch data, Try again later",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean clearTable(){
        db = new SQLiteHelper(context);
        db.emptyTables();
        return true;
    }

    public ArrayList<Report> getReport(){
        db = new SQLiteHelper(context);
        return db.getReport();
    }

    public boolean removeAllReportCache(){

        if (removeStudentInfoCache() && removeExamInfoCache() && removeEvidenceDetailsCache() && removeSubReportDetailsCache() && removeApprovalDetailsCache() && removeChiefInvDetailsCache()){

            return true;
        }
        else{
            return false;
        }
    }

    public boolean removeAllData(){
        if (removeAllReportCache() && removeUserPrefCache()){
//            clearTable();
            return true;
        }
        else{
            return false;
        }
    }
}
