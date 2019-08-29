package com.example.cameraapp.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionCheck {

    private ConnectivityManager cm;
    private NetworkCapabilities networkCapabilities;
    NetworkInfo netInfo;
    Network network;
    public ConnectionCheck() {
    }

    public boolean isOnline(Context context) {
        boolean internetPing = false;
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

    public boolean isMobileData(Context context){
        boolean mobileData = false;
        if (isOnline(context)){
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

    public boolean isWifi(Context context){
        boolean wifi = false;
        if (isOnline(context)){
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
