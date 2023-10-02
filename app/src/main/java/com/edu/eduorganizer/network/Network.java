package com.edu.eduorganizer.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

public class Network extends BroadcastReceiver {

    public static final String ACTION_CONNECTIVITY_CHANGE = "com.edubox.admin.CONNECTIVITY_CHANGE";
    public static final String EXTRA_CONNECTIVITY_STATUS = "connectivityStatus";
    private ConnectivityManager.NetworkCallback networkCallback;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(android.net.Network network) {
                super.onAvailable(network);
                // Network is available
                sendBroadcast(context, true);
            }

            @Override
            public void onLost(android.net.Network network) {
                super.onLost(network);
                // Network is lost
                sendBroadcast(context, false);
            }
        };

        if (connectivityManager != null) {
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }

    private void sendBroadcast(Context context, boolean isConnected) {
        Intent broadcastIntent = new Intent(ACTION_CONNECTIVITY_CHANGE);
        broadcastIntent.putExtra(EXTRA_CONNECTIVITY_STATUS, isConnected);
        context.sendBroadcast(broadcastIntent);
    }
}