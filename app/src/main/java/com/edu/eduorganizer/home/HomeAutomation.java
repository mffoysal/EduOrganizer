package com.edu.eduorganizer.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.R;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//import com.appizona.yehiahd.fastsave.FastSave;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
public class HomeAutomation extends BaseMenu implements View.OnClickListener {

    public static Activity start_activity;
    String BLUETOOTH_INFO = "bluetooth_info";
    String CLASSIC_SCAN = "classic_scan";
    String FIND_DEVICE = "find_device";
    String PAIRED_DEVICE = "paired_device";
    String activityName = "";
    ArrayList<Parcelable> arrayList = new ArrayList<>();
    public BluetoothAdapter bluetoothAdapter;
    ImageView img_ad_free;
    ImageView img_info;
    ImageView img_scan;
    public boolean isBtnDeviceVisible = false;
    boolean isChecked = false;
    /* access modifiers changed from: private */
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                if (intExtra == 10) {
                    HomeAutomation.this.isChecked = false;
                    HomeAutomation.this.on_off_switch.setBackgroundResource(R.drawable.off);
                } else if (intExtra == 12) {
                    HomeAutomation.this.isChecked = true;
                    HomeAutomation.this.on_off_switch.setBackgroundResource(R.drawable.on);
                }
            }
        }
    };
    ImageView on_off_switch;
    Pair_device pair_device;
    PermissionClass permissionClass = new PermissionClass(this);
    Animation push_animation;
    RelativeLayout rel_native_ad;
    RelativeLayout rl_find;
    RelativeLayout rl_info;
    RelativeLayout rl_pairedbtn;

    public void viewLocationDialog() {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Why location permission required?").setMessage((CharSequence) getResources().getString(R.string.location_help_text)).setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    private Button bluetoothButton, wifiButton, wirelessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_automation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        start_activity = this;

        bluetoothButton = findViewById(R.id.bluetoothButtonId);
        wifiButton = findViewById(R.id.wifiButtonId);
        wirelessButton = findViewById(R.id.wirelessButtonId);

        bluetoothButton.setOnClickListener(this);
        wifiButton.setOnClickListener(this);
        wirelessButton.setOnClickListener(this);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bluetoothButtonId){

//            v.startAnimation(HomeAutomation.this.push_animation);
            HomeAutomation startActivity = HomeAutomation.this;
            startActivity.activityName = startActivity.PAIRED_DEVICE;
            if (!HomeAutomation.this.bluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Please enable Bluetooth first!",Toast.LENGTH_SHORT).show();
            } else if (PermissionClass.HasPermission()) {
                HomeAutomation.this.NextScreen();
            } else {
                PermissionClass permissionClass = HomeAutomation.this.permissionClass;
                PermissionClass.RequestPermissions();
            }

//            startActivity(new Intent(getApplicationContext(), Bluetooth.class));

        } else if (v.getId()==R.id.wifiButtonId) {
            startActivity(new Intent(getApplicationContext(), Wifi.class));

        } else if (v.getId()==R.id.wirelessButtonId) {
            startActivity(new Intent(getApplicationContext(), Wireless.class));
        }
    }

    private void NextScreen() {
        if (this.activityName.equalsIgnoreCase(this.CLASSIC_SCAN)) {
        } else if (this.activityName.equalsIgnoreCase(this.PAIRED_DEVICE)) {
            Paired_Device_List();
        } else if (this.activityName.equalsIgnoreCase(this.BLUETOOTH_INFO)) {
            
        } else if (this.activityName.equalsIgnoreCase(this.FIND_DEVICE)) {
            
        } else if (!this.bluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 101);
        }
    }

    private void Paired_Device_List() {
        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice device : bondedDevices) {
                Pair_device pair_device2 = new Pair_device();
                this.pair_device = pair_device2;
                pair_device2.setDevice(device);
                this.arrayList.add(this.pair_device);
            }
            Intent intent = new Intent(this, Bluetooth.class);
//            Intent intent = new Intent(this, BluetoothControl.class);
            intent.putParcelableArrayListExtra("deviceName", this.arrayList);
            startActivity(intent);
            return;
        }
        Toast.makeText(this, R.string.empty_list_pair_device, Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(getApplicationContext(),BluetoothControl.class));
    }
}