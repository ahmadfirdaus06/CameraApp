package com.ahmad.cameraapp.miscellanous;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.ahmad.cameraapp.R;
import com.ahmad.cameraapp.config.DataSource;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UploadFileAsync extends AsyncTask<String, Integer, ArrayList<String>> {

    private Context context;
    private int total = 0;
    private AsyncResponse response;
    NotificationCompat.Builder builder;
    NotificationManager manager;
    NotificationManagerCompat managerCompat;
    RemoteViews notificationLayout;

    public interface AsyncResponse {
        public void processFinish(ArrayList<String> paths);
        public void setDialog(int step, int progress, int current, int max);
    }

    public UploadFileAsync (Context context, NotificationManager manager, RemoteViews notificationLayout, NotificationCompat.Builder builder){
        this.context = context;
        this.builder = builder;
        this.manager = manager;
        this.notificationLayout = notificationLayout;
    }

    public UploadFileAsync (Context context, NotificationManagerCompat managerCompat, RemoteViews notificationLayout, NotificationCompat.Builder builder){
        this.context = context;
        this.builder = builder;
        this.managerCompat = managerCompat;
        this.notificationLayout = notificationLayout;
    }

    public UploadFileAsync setListener(AsyncResponse response) {
        this.response = response;
        return this;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        int serverResponseCode = 0;
        DataSource dataSource = new DataSource();
        JSONObject response = null;
        total = strings.length;
        ArrayList <String> storedPath = new ArrayList<>();
        for (int i = 0; i < strings.length; i++){
            try {
                int progress = (int)((double)(i + 1) * 100 / (double)total);
                publishProgress(progress, i + 1, total);
                String sourceFileUri = strings[i];
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);

                if (sourceFile.isFile()) {

                    try {
                        String upLoadServerUri = dataSource.getUploadImageUrl();

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("file", sourceFileUri);
                        conn.setChunkedStreamingMode(4096);
                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                                + sourceFileUri + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {
                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);

                        }

                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);

                        serverResponseCode = conn.getResponseCode();

                        String someResponse = "";

                        if (serverResponseCode == 200) {
                            InputStream in = new BufferedInputStream(conn.getInputStream());
                            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(in));
                            String line = "";
                            StringBuilder stringBuilder = new StringBuilder();
                            while ((line = responseStreamReader.readLine()) != null)
                                stringBuilder.append(line).append("\n");
                            responseStreamReader.close();
                            someResponse = stringBuilder.toString();
                            if (!someResponse.isEmpty()){
                                response = new JSONObject(someResponse);
                                if (response.has("image_path")){
                                    storedPath.add(response.getString("image_path"));
                                }
                                else{
                                    storedPath = null;
                                }
                            }
                            else{
                                storedPath = null;
                            }


                        } else {
                            throw new Exception("response code: " + serverResponseCode);
                        }

                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return storedPath;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        if (response != null){
            this.response.processFinish(strings);
            this.response.setDialog(3, 0, 0, 0);
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (response != null){
            this.response.setDialog(1, 0, 0, 0);
            notificationLayout.setTextViewText(R.id.text_status, "Uploading Images");
            builder.setProgress(0,0, true);
            rebuildNotification();
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (response != null){
            this.response.setDialog(2, values[0], values[1], values[2]);
            notificationLayout.setTextViewText(R.id.text_status, "Uploading Image " + values[1] + " of " + values[2]);
            builder.setProgress(values[2],values[1], false);
            rebuildNotification();
        }

    }

    public void rebuildNotification(){
        if (manager != null){
            manager.notify(1, builder.build());
        }
        else{
            managerCompat.notify(1, builder.build());
        }
    }
}
