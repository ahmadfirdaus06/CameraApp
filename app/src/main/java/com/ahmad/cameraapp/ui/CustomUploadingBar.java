package com.ahmad.cameraapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahmad.cameraapp.R;

public class CustomUploadingBar extends Activity{

    private Dialog dialog;
    private ProgressBar progressBar;
    private TextView textProgress, textHide;
    Context context;

    public CustomUploadingBar (Context context){
        this.context = context;
    }

    public boolean isShowing(){
        if (dialog != null){
            if(dialog.isShowing()){
                return true;
            }
            else{
                return false;
            }
        }else{
            return false;
        }

    }

    public void show(String message) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_upload_bar);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar = dialog.findViewById(R.id.progress_bar);
        textProgress = dialog.findViewById(R.id.text_progress);
        textHide = dialog.findViewById(R.id.text_hide);
        textProgress.setText(message);
        textProgress.setVisibility(View.VISIBLE);
        textHide.setVisibility(View.VISIBLE);
        textHide.setOnClickListener(hide);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    private View.OnClickListener hide = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = (Activity) context;
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(startMain);
        }
    };

    public void update(int progress, String message){
        textProgress.setText(message + " " + "(" + progress + "%)");
        progressBar.setProgress(progress);
        dialog.show();
    }

    public void waiting(){
        progressBar.setIndeterminate(true);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
