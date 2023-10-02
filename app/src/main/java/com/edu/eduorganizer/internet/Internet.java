package com.edu.eduorganizer.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Internet extends BroadcastReceiver {
    private Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public Internet(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction()!=null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager!=null){
                networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()){
                    Toast.makeText(context,"Internet Connected",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public Internet(Context context){
        this.context = context;
    }

    public static boolean isInternetConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
    public boolean isInternetConnection(){
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}
