package com.edu.eduorganizer.home;


import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.edu.eduorganizer.R;

public class PermissionClass {
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    public static String[] all_permissions = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.BLUETOOTH"};
    public static String[] all_permissions_31 = {"android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    public static String[] all_permissions_33 = {"android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    public static Activity mActivity;

    public static String[] permissions() {
        if (Build.VERSION.SDK_INT >= 33) {
            return all_permissions_33;
        }
        if (Build.VERSION.SDK_INT >= 31) {
            return all_permissions_31;
        }
        return all_permissions;
    }

    public PermissionClass(Activity activity) {
        mActivity = activity;
    }

    public static void RequestPermissions() {
        try {
            ActivityCompat.requestPermissions(mActivity, permissions(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean HasPermission() {
        return Build.VERSION.SDK_INT >= 33 ? ContextCompat.checkSelfPermission(mActivity, "android.permission.BLUETOOTH_CONNECT") == 0 && ContextCompat.checkSelfPermission(mActivity, "android.permission.BLUETOOTH_SCAN") == 0 && ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_COARSE_LOCATION") == 0 && ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_FINE_LOCATION") == 0 : Build.VERSION.SDK_INT >= 31 ? ContextCompat.checkSelfPermission(mActivity, "android.permission.BLUETOOTH_CONNECT") == 0 && ContextCompat.checkSelfPermission(mActivity, "android.permission.BLUETOOTH") == 0 && ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_COARSE_LOCATION") == 0 && ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_FINE_LOCATION") == 0 : ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_COARSE_LOCATION") == 0 && ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_FINE_LOCATION") == 0 && ContextCompat.checkSelfPermission(mActivity, "android.permission.BLUETOOTH") == 0;
    }

    public static boolean checkRequestPermissionRationale() {
        return Build.VERSION.SDK_INT >= 33 ? ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_COARSE_LOCATION") || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_FINE_LOCATION") || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.BLUETOOTH_CONNECT") || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.BLUETOOTH_SCAN") : Build.VERSION.SDK_INT >= 31 ? ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_COARSE_LOCATION") || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_FINE_LOCATION") || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.BLUETOOTH_CONNECT") || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.BLUETOOTH") : ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_COARSE_LOCATION") || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_FINE_LOCATION") || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.BLUETOOTH");
    }

    public static void openSettingDialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_permission);
        ((Button) dialog.findViewById(R.id.dialog_conform_btn_yes)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.addFlags(268435456);
                    intent.setData(Uri.fromParts("package", PermissionClass.mActivity.getPackageName(), (String) null));
                    PermissionClass.mActivity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.dialog_conform_btn_no)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

