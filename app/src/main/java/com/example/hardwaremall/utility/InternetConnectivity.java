package com.example.hardwaremall.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnectivity {
    public boolean isConnectedToInternet(Context context){
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(manager!=null){
            NetworkInfo[] info= manager.getAllNetworkInfo();
            if(info!=null){
                for (int i =0;i<info.length;i++){
                    if(info[i].getState()== NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    /*public void checkConnection(){
        //ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
         ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo info = manager.getActiveNetworkInfo();
         if(info != null){
             if(info.getType() == ConnectivityManager.TYPE_WIFI)
                 Toast.makeText(this, "WiFi enabled", Toast.LENGTH_SHORT).show();
             else if(info.getType() == ConnectivityManager.TYPE_MOBILE)
                 Toast.makeText(this, "Mobile Data enabled", Toast.LENGTH_SHORT).show();
         } else
             Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
    }*/

}
