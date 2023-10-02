package com.edu.eduorganizer.wifi;

import static android.content.Context.WIFI_SERVICE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;

public class HotspotManager {

    private Context context;
    private WifiManager wifiManager;

    public HotspotManager(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public boolean startHotspot(String ssid, String password) {
        if (isWifiApEnabled()) {
            Log.e("HotspotManager", "Hotspot is already enabled.");
            return false;
        }

        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = ssid;
        wifiConfiguration.preSharedKey = password;

        try {
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            return (boolean) method.invoke(wifiManager, wifiConfiguration, true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean stopHotspot() {
        if (!isWifiApEnabled()) {
            Log.e("HotspotManager", "Hotspot is already disabled.");
            return false;
        }

        try {
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            return (boolean) method.invoke(wifiManager, null, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isWifiApEnabled() {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            return (boolean) method.invoke(wifiManager);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isHotspotEnabled() {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            return (boolean) method.invoke(wifiManager);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getHotspotIpAddress() {
        if (!isHotspotEnabled()) {
            return null;
        }

        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfiguration = (WifiConfiguration) method.invoke(wifiManager);
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

            int ipAddress = dhcpInfo.ipAddress;
            return formatIpAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getWifiIpAddress() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return formatIpAddress(ipAddress);
        }
        return null;
    }


    private String formatIpAddress(int ipAddress) {
        return String.format(
                "%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff)
        );
    }


    public void enableHotspot() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.TetherSettings"));
        context.startActivity(intent);
    }

    public void disableHotspot() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }

}
