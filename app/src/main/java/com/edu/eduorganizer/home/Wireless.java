package com.edu.eduorganizer.home;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;

import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.R;

public class Wireless extends BaseMenu {

    private static final String TAG = "MainActivity";
    private ListView deviceListView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wireless);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        deviceListView = findViewById(R.id.deviceListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            } else {
                new FetchConnectedDevicesTask().execute();
            }
        }

    }

    private class FetchConnectedDevicesTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> connectedDevices = new ArrayList<>();
            try {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String macAddress = wifiInfo.getMacAddress();
                String ipAddress = intToIp(wifiInfo.getIpAddress());
                String subnet = ipAddress.substring(0, ipAddress.lastIndexOf("."));

                Process process = Runtime.getRuntime().exec("/system/bin/arp -n");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d(TAG, "ARP Line: " + line);
                    if (line.contains("ether")) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 4) {
                            String deviceIpAddress = parts[0];
                            String deviceMacAddress = parts[2];
                            if (deviceIpAddress.startsWith(subnet) && !deviceMacAddress.equalsIgnoreCase(macAddress)) {
                                connectedDevices.add(deviceIpAddress + " - " + deviceMacAddress);
                            }
                        }
                    }
                }
                process.waitFor();
            } catch (Exception e) {
                Log.e(TAG, "Error fetching connected devices", e);
            }
            return connectedDevices;
        }

        @Override
        protected void onPostExecute(ArrayList<String> connectedDevices) {
            adapter.clear();
            adapter.addAll(connectedDevices);
        }
    }

    private String intToIp(int ip) {
        return ((ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 24) & 0xFF));
    }

}