package com.edu.eduorganizer.home;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

import android.os.Bundle;

import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.R;

public class Wifi extends BaseMenu {

    private WifiManager wifiManager;
    private ListView listView;
    private Button scanButton;
    private List<ScanResult> results;
    private ArrayAdapter<String> adapter;
    private BroadcastReceiver wifiScanReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        listView = findViewById(R.id.listView);
        scanButton = findViewById(R.id.scanButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                connectToWifi(results.get(i).SSID);
            }
        });

        // Check for Wi-Fi permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        // Register WiFi scan receiver
        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                results = wifiManager.getScanResults();
                adapter.clear();
                for (ScanResult scanResult : results) {
                    adapter.add(scanResult.SSID);
                }
            }
        };
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


    }

    private void scanWifi() {
        wifiManager.startScan();
        Toast.makeText(this, "Scanning WiFi...", Toast.LENGTH_SHORT).show();
    }

    private void connectToWifi(String ssid) {
//        WifiConfiguration wifiConfig = new WifiConfiguration();
//        wifiConfig.SSID = String.format("\"%s\"", ssid);
//        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//
//        int netId = wifiManager.addNetwork(wifiConfig);
//        wifiManager.disconnect();
//        wifiManager.enableNetwork(netId, true);
//        wifiManager.reconnect();

        if (wifiManager == null) {
            Toast.makeText(getApplicationContext(), "Wi-Fi manager not available", Toast.LENGTH_SHORT).show();
            return;
        }

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int netId = wifiManager.addNetwork(wifiConfig);
        if (netId == -1) {
            Toast.makeText(getApplicationContext(), "Failed to add network configuration", Toast.LENGTH_SHORT).show();
            return;
        }

        wifiManager.disconnect();
        boolean enableNetwork = wifiManager.enableNetwork(netId, true);
        if (!enableNetwork) {
            Toast.makeText(getApplicationContext(), "Failed to enable network", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean reconnect = wifiManager.reconnect();
        if (!reconnect) {
            Toast.makeText(getApplicationContext(), "Failed to reconnect to network", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(this, "Connecting to " + ssid, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanReceiver);
    }

}