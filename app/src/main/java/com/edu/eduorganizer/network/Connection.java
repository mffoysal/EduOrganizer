package com.edu.eduorganizer.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection extends BroadcastReceiver {
    public static final String ACTION_CONNECTIVITY_CHANGE = "com.edubox.admin.CONNECTIVITY_CHANGE";
    public static final String EXTRA_CONNECTIVITY_STATUS = "connectivityStatus";
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null && networkInfo.isConnected();

            Intent broadcastIntent = new Intent(ACTION_CONNECTIVITY_CHANGE);
            broadcastIntent.putExtra(EXTRA_CONNECTIVITY_STATUS, isConnected);
            context.sendBroadcast(broadcastIntent);
        }
    }
}
