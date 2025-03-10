package com.ahmad.cameraapp;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.Constraints;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmad.cameraapp.config.Cache;
import com.ahmad.cameraapp.config.ConnectionCheck;
import com.ahmad.cameraapp.config.DataSource;
import com.ahmad.cameraapp.fragments.LoginFragment;
import com.ahmad.cameraapp.fragments.MainFragment;
import com.ahmad.cameraapp.fragments.Step1Fragment;
import com.ahmad.cameraapp.service.UploadService;
import com.ahmad.cameraapp.ui.CustomUploadingBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Cache cache;
    private CustomUploadingBar uploadingBar;
    private Window window;
    private Dialog dialog;
    private TextView textYes, textNo, textReview, textReportId;
    private ConnectionCheck conn;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private DataSource dataSource = new DataSource();
    private String reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("openDialog"));
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        cache = new Cache(this);
        conn = new ConnectionCheck(this);
        uploadingBar = new CustomUploadingBar(this);
        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.toolbar_color));
        verifyPermissions();
        redirectToLogin();
    }

    public void verifyPermissions(){
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS};
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 2000);
            }
        }
    }

    public void redirectToLogin(){
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, loginFragment);
        ft.commit();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("type");
            if (action == "openDialog1"){
                uploadingBar.show("Uploading Report Details...");
            }
            else if (action == "openDialog2"){
                int step = intent.getIntExtra("step", 0);
                int progress = intent.getIntExtra("progress", 0);
                int current = intent.getIntExtra("current", 0);
                int max = intent.getIntExtra("max", 0);
                switch (step){
                    case 1:
                        uploadingBar.update(0, "Uploading Images...");
                        break;

                    case 2:
                        uploadingBar.update(progress, "Uploading Image " + current + " of " + max);
                        break;

                    case 3:
                        uploadingBar.waiting();
                        break;
                }
            }
            else if (action == "openDialog3"){
                int status = intent.getIntExtra("status", 0);
                switch (status){
                    case 00:
                        uploadingBar.update(100,"Finishing Uploading...");
                        break;

                    case 01:
                        uploadingBar.dismiss();
                        successResult(intent.getStringExtra("report_id"));
                        break;
                }
            }
            else{
                uploadingBar.dismiss();
                System.out.println("Failed");
                failResult();
            }
        }
    };

    public void failResult(){
        System.out.println("Upload Unsuccessful!");
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_failed_dialog);
        dialog.getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        textYes = dialog.findViewById(R.id.text_yes);
        textNo = dialog.findViewById(R.id.text_no);
        textYes.setOnClickListener(reupload);
        textNo.setOnClickListener(noReupload);
        dialog.show();
    }

    public void successResult(String reportId){
        this.reportId = reportId;
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_success_dialog);
        dialog.getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        textReportId = dialog.findViewById(R.id.text_report_id);
        textReview = dialog.findViewById(R.id.text_review);
        textReportId.setText("Report #" + reportId);
        textReview.setOnClickListener(ok);
        dialog.show();

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager
                .PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Couldn't notify student. Permission not allowed." , Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                String phoneNum = "+6" + cache.getStudentInfoCache().getContactNo();;

                String message = "ATTENTION! This message was sent directly from IUKL " +
                        "Counselory Department. You are receiving this message because you are being" +
                        " suspected for committing examination misconduct in exam hall. Please contact " +
                        "The Counselor from IUKL Counselory Department immediately for more details about this message.";

                SmsManager smsManager = SmsManager.getDefault();

                if (smsManager != null){
                    ArrayList<String> messageParts = smsManager.divideMessage(message);
                    smsManager.sendMultipartTextMessage(phoneNum, null, messageParts, null, null);
                }

                Toast.makeText(this, "Student has been notified." , Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void upload(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, "http://" + dataSource.getHost() + ":8080",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Intent serviceIntent = new Intent(getApplicationContext(), UploadService.class);
                        ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Server offline. Try again later.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private View.OnClickListener reupload = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (conn.isOnline()){
                dialog.dismiss();
                if (conn.isMobileData()){
                    alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.alertDialog)).create();
                    alertDialog.setTitle("Confirm");
                    alertDialog.setMessage("Are you sure to upload using mobile data?\n(Data charges may apply.)");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    upload();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    upload();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener noReupload = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    private View.OnClickListener ok = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();
        }
    };

    @Override
    public void onBackPressed()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof Step1Fragment) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("Cancel report?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (cache.removeAllReportCache()){
                                MainFragment mainFragment = new MainFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                                        .replace(R.id.container, mainFragment).commit();
                            }
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (uploadingBar != null){
            if (uploadingBar.isShowing()){
                uploadingBar.dismiss();
            }
        }
        if (dialog != null){
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
