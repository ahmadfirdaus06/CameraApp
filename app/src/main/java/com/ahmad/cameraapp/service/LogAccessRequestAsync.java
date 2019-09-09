package com.ahmad.cameraapp.service;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.ahmad.cameraapp.config.Cache;
import com.ahmad.cameraapp.config.DataSource;
import com.ahmad.cameraapp.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class LogAccessRequestAsync extends AsyncTask<JSONObject, Void, Boolean> {

    private AsyncResponse response;
    private DataSource dataSource;
    private Context context;
    private Cache cache;
    static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager cookieManager;

    public LogAccessRequestAsync(Context context) {
        this.context = context;
        dataSource = new DataSource();
        cache = new Cache(this.context);
    }

    public interface AsyncResponse {
        public void processFinish(boolean response);
        public void start();
        public void finish();
    }

    public LogAccessRequestAsync setListener(AsyncResponse response) {
        this.response = response;
        return this;
    }

    @Override
    protected Boolean doInBackground(JSONObject... jsonObjects) {
        boolean granted = false;
        String responseMessage = "";
        cookieManager = new java.net.CookieManager();
        String requestUrl, requestType = "";
        final String LOGIN = "LOGIN";
        final String LOGOUT = "LOGOUT";
        if (jsonObjects.length != 0){
            requestUrl = dataSource.getLoginUrl();
            requestType = LOGIN;
        }
        else{
            requestUrl = dataSource.getLogoutUrl();
            requestType = LOGOUT;
        }


        URL url = null;
        try {
            url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if (requestType == LOGOUT){
                getCookie(conn);
            }

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            if (requestType == LOGIN){
                os.writeBytes(jsonObjects[0].toString());
            }
            if (requestType == LOGOUT){
                JSONObject objectRequest = new JSONObject();
                objectRequest.put("user_id", cache.getUserPrefCache().getUserID());
                os.writeBytes(objectRequest.toString());
            }
            os.flush();
            os.close();

            if (conn.getResponseCode() == 200){

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");
                responseStreamReader.close();
                responseMessage = stringBuilder.toString();
                if (!responseMessage.isEmpty()){
                    JSONObject response = new JSONObject(responseMessage);
                    if (requestType == LOGIN){
                        System.out.println(response.getString("message"));
                        if (response.getString("message").contains("Access Granted!")){
                            setCookie(conn);
                            JSONObject data = response.getJSONArray("data").getJSONObject(0);
                            if (data != null){
                                User user = new User();
                                user.setUserID(data.getString("user_id"));
                                user.setStaffId(data.getString("staff_id"));
                                user.setPassword(data.getString("password"));
                                user.setName(data.getString("name"));
                                user.setEmail(data.getString("email"));
                                user.setContactNo(data.getString("contact_no"));
                                user.setUserType(data.getString("user_type"));
                                user.setCreatedDate(data.getString("created_date"));
                                user.setModifiedDate(data.getString("modified_date"));
                                user.setLastLogin(data.getString("last_login"));
                                user.setGrantedAccess(data.getString("granted_access"));
                                if (cache.setUserPrefCache(user)){
                                    granted = true;
                                }
                            }
                        }
                        else{

                            granted = false;
                        }
                    }
                    else if (requestType == LOGOUT){
                        System.out.println(response.getString("message"));
                        if (response.getString("message").contains("Success")){
                            granted = true;
                        }
                        else{
                            granted = false;
                        }
                    }

                }
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
            Toast.makeText(context, "Connection error!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return granted;
    }

    public void setCookie(HttpURLConnection conn){
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
    }

    public void getCookie(HttpURLConnection conn){
        if (cookieManager.getCookieStore().getCookies().size() > 0) {
            conn.setRequestProperty("Cookie",
                    TextUtils.join(";",  cookieManager.getCookieStore().getCookies()));
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.response.start();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        this.response.finish();
        this.response.processFinish(result);
    }
}
