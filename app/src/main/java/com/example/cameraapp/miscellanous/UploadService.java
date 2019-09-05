package com.example.cameraapp.miscellanous;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cameraapp.MainActivity;
import com.example.cameraapp.R;
import com.example.cameraapp.config.Cache;
import com.example.cameraapp.config.DataSource;
import com.example.cameraapp.models.Approval;
import com.example.cameraapp.models.Exam;
import com.example.cameraapp.models.Report;
import com.example.cameraapp.models.Student;
import com.example.cameraapp.models.SubReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UploadService extends Service implements UploadFileAsync.AsyncResponse {
    ArrayList <String> imagePaths;
    ArrayList<String> uploadedImagePaths;
    String [] paths = {};
    UploadFileAsync uploadFileAsync;
    Cache cache;
    Approval approval;
    Exam exam;
    SubReport subReport;
    Student student;
    Report report;
    DataSource dataSource = new DataSource();
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    Intent notificationIntent,openDialog1, openDialog2, openDialog3, failUpload, stopService;
    private String reportId;


    public static final String CHANNEL_ID = "UploadServiceChannel";
    PendingIntent pendingIntentStartService, pendingIntentStopService;
    NotificationCompat.Builder builder;
    Notification notification;
    NotificationManager manager;
    NotificationManagerCompat managerCompat;
    RemoteViews notificationLayout;

    @Override
    public void onCreate() {
        super.onCreate();
        cache = new Cache(this);
        createNotificationChannel();
        notificationIntent = new Intent(this, MainActivity.class);
        pendingIntentStartService = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        notificationLayout = new RemoteViews(getPackageName(), R.layout.layout_upload_notification);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.user_icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setContentIntent(pendingIntentStartService)
                .setOngoing(true)
                .build();
        notification = builder.build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.getInt("stop") == 00){
            stopSelf();
        }
        else{
            startForeground(1, notification);
            notificationLayout.setTextViewText(R.id.text_status, "Please wait...");
            builder.setProgress(0,0, true);

            rebuildNotification();

            uploadReportDetails();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        System.out.println("Stopped");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChanne = new NotificationChannel(
                    CHANNEL_ID,
                    "Upload Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );

            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChanne);
        }
        else{
            managerCompat = NotificationManagerCompat.from(this);
        }
    }

    public void uploadImages(){
        imagePaths = cache.getEvidenceDetailsCache().getImagePaths();
        uploadedImagePaths = new ArrayList<>();
        if (manager != null){
            uploadFileAsync = new UploadFileAsync(this, manager, notificationLayout, builder);
        }
        else{
            uploadFileAsync = new UploadFileAsync(this, managerCompat, notificationLayout, builder);
        }
        paths = imagePaths.toArray(new String[imagePaths.size()]);
        uploadFileAsync.setListener(this).execute(paths);
    }

    public void finishUpload(ArrayList<String> paths){
        JSONArray requestArray = new JSONArray(paths);
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("paths", requestArray);
            requestObject.put("report_id", reportId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(this);
        notificationLayout.setTextViewText(R.id.text_status, "Finishing Uploading");
        builder.setProgress(0,0, true);
        rebuildNotification();
        openDialog3 = new Intent("openDialog");
        openDialog3.putExtra("type", "openDialog3");
        openDialog3.putExtra("status", 00);
        LocalBroadcastManager.getInstance(this).sendBroadcast(openDialog3);
        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, dataSource.getUploadImageDetailsUrl(), requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            System.out.println(message);
                            if (message.equals("Success")){
                                openDialog3 = new Intent("openDialog");
                                openDialog3.putExtra("type", "openDialog3");
                                openDialog3.putExtra("status", 01);
                                openDialog3.putExtra("report_id", reportId);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(openDialog3);
                                notificationLayout.setTextViewText(R.id.text_status, "Report #" + response.getString("report_id") + " Submitted");
                                builder.setProgress(0,0, false);
                                builder.setOngoing(false).build();
                                rebuildNotification();
                                stopForeground(false);
                                stopCurrentService();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            failUploadNotify();
                            Toast.makeText(getApplicationContext(),
                                    "Upload Failed. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        failUploadNotify();
                        Toast.makeText(getApplicationContext(), "Upload Failed. Try again later."
                                , Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public void uploadReportDetails(){
        student = cache.getStudentInfoCache();
        exam = cache.getExamInfoCache();
        approval = cache.getApprovalDetailsCache();
        subReport = cache.getSubReportDetailsCache();

        report = new Report();
        report.setStudentId(student.getStudentId());
        report.setReporterUserId(approval.getReporterUserId());
        report.setSuperiorUserId(approval.getSuperiorUserId());
        report.setCourseCode(exam.getCourseCode());
        report.setCourseName(exam.getCourseName());
        report.setExamVenue(exam.getExamVenue());
        report.setExamDate(exam.getExamDate());
        report.setExamTime(exam.getExamTime());
        report.setTypeOfMisconduct(subReport.getTypeOfMisconduct());
        report.setMisconductTime(subReport.getTimeOfMisconduct());
        report.setMisconductDescription(subReport.getMisconductDesc());
        report.setActionTaken(subReport.getActionTaken());
        report.setWitness1Name(approval.getWitness1Name());
        report.setWitness1ContactNo(approval.getWitness1ContactNo());
        report.setWitness1Email(approval.getWitness1Email());
        report.setWitness2Name(approval.getWitness2Name());
        report.setWitness2ContactNo(approval.getWitness2ContactNo());
        report.setWitness2Email(approval.getWitness2Email());
        JSONArray requestArray = new JSONArray(report.getTypeOfMisconduct());


        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("student_id", report.getStudentId());
            requestObject.put("reporter_id", report.getReporterUserId());
            requestObject.put("superior_id", report.getSuperiorUserId());
            requestObject.put("course_code", report.getCourseCode());
            requestObject.put("course_name", report.getCourseName());
            requestObject.put("exam_venue", report.getExamVenue());
            requestObject.put("exam_date", report.getExamDate());
            requestObject.put("exam_time", report.getExamTime());
            requestObject.put("misconduct_description", report.getMisconductDescription());
            requestObject.put("misconduct_time", report.getMisconductTime());
            requestObject.put("action_taken", report.getActionTaken());
            requestObject.put("witness1_name", report.getWitness1Name());
            requestObject.put("witness1_contact_no", report.getWitness1ContactNo());
            requestObject.put("witness1_email", report.getWitness1Email());
            requestObject.put("witness2_name", report.getWitness2Name());
            requestObject.put("witness2_contact_no", report.getWitness2ContactNo());
            requestObject.put("witness2_email", report.getWitness2Email());
            requestObject.put("misconduct_type", requestArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(this);
        notificationLayout.setTextViewText(R.id.text_status, "Uploading Report Details");
        builder.setProgress(0,0, true);
        rebuildNotification();
        openDialog1 = new Intent("openDialog");
        openDialog1.putExtra("type", "openDialog1");
        LocalBroadcastManager.getInstance(this).sendBroadcast(openDialog1);
        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, dataSource.getUploadReportDetailsUrl(), requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            System.out.println(message);
                            if (message.equals("Success")){
                                reportId = response.getString("report_id");
                                if (!reportId.isEmpty()){
                                    uploadImages();
                                }
                            }
                        } catch (JSONException e) {
                            failUploadNotify();
                            Toast.makeText(getApplicationContext(),
                                    "Upload Failed. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        failUploadNotify();
                        Toast.makeText(getApplicationContext(), "Upload Failed. Try again later."
                                , Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);

    }

    public void failUploadNotify(){
        failUpload = new Intent("openDialog");
        failUpload.putExtra("type", "fail");
        LocalBroadcastManager.getInstance(this).sendBroadcast(failUpload);
        notificationLayout.setTextViewText(R.id.text_status, "Upload Failed");
        builder.setProgress(0,0, false);
        builder.setOngoing(false).build();
        rebuildNotification();
        stopForeground(false);
        stopCurrentService();
    }

    public void stopCurrentService(){
        stopService = new Intent(this, UploadService.class);
        stopService.putExtra("stop", 00);
        pendingIntentStopService = PendingIntent.getService(this,
                0,
                stopService,
                PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setDeleteIntent(pendingIntentStopService);
        rebuildNotification();
    }

    public void rebuildNotification(){
        if (manager != null){
            manager.notify(1, builder.build());
        }
        else{
            managerCompat.notify(1, builder.build());
        }
    }

    @Override
    public void processFinish(ArrayList<String> paths) {
        finishUpload(paths);
    }

    @Override
    public void setDialog(int step, int progress, int current, int max) {
        openDialog2 = new Intent("openDialog");
        openDialog2.putExtra("type", "openDialog2");
        openDialog2.putExtra("step", step);
        openDialog2.putExtra("progress", progress);
        openDialog2.putExtra("current", current);
        openDialog2.putExtra("max", max);
        LocalBroadcastManager.getInstance(this).sendBroadcast(openDialog2);
    }
}
