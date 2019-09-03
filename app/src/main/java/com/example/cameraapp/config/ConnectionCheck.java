package com.example.cameraapp.config;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cameraapp.miscellanous.UploadService;

public class ConnectionCheck {

    private ConnectivityManager cm;
    private NetworkCapabilities networkCapabilities;
    private NetworkInfo netInfo;
    private Network network;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private DataSource dataSource = new DataSource();
    private boolean isOnline = false;
    private Context context;

    public ConnectionCheck(Context context) {
        this.context = context;
    }

    public boolean serverCheck(){

        requestQueue = Volley.newRequestQueue(context);
        stringRequest = new StringRequest(Request.Method.GET, "http://" + dataSource.getHost() + ":8080",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        isOnline =  true;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isOnline = false;
            }
        });

        requestQueue.add(stringRequest);

        return isOnline;
    }

    public boolean isOnline() {
        boolean connection = false;
        boolean connectionOk = false;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()){
            connection = true;
        }

        if (connection){
            connectionOk = true;
        }

        return  connectionOk;
    }

    public boolean isMobileData(){
        boolean mobileData = false;
        if (isOnline()){
            network = cm.getActiveNetwork();
            networkCapabilities = cm.getNetworkCapabilities(network);
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                mobileData = true;
            }
        }
        else{
            Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
        return mobileData;
    }

    public boolean isWifi(){
        boolean wifi = false;
        if (isOnline()){
            network = cm.getActiveNetwork();
            networkCapabilities = cm.getNetworkCapabilities(network);
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                wifi = true;
            }
        }
        else{
            Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
        return wifi;
    }
}
