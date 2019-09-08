package com.example.cameraapp.miscellanous;

import android.content.Context;
import android.os.AsyncTask;

import com.example.cameraapp.config.SQLiteHelper;

import org.json.JSONObject;

import java.util.ArrayList;

public class GetReportSummaryAsync extends AsyncTask<String, Void, JSONObject> {

    private AsyncResponse response;
    private Context context;
    private SQLiteHelper db;

    public interface AsyncResponse {
        public void start();
        public void finish(JSONObject results);
    }

    public GetReportSummaryAsync(Context context) {
        this.context = context;
        db = new SQLiteHelper(this.context);
    }

    public GetReportSummaryAsync setListener(AsyncResponse response) {
        this.response = response;
        return this;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        String reportId = strings[0];
        return db.getReportAssoc(reportId);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.response.start();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        this.response.finish(jsonObject);
    }
}
