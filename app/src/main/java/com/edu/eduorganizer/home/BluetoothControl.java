package com.edu.eduorganizer.home;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.edu.eduorganizer.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class BluetoothControl extends Activity implements View.OnClickListener {
    private ConnectThread connectThread;
    private final UUID uuid = UUID.fromString("0000111F-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter<String> devicesArrayAdapter;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private OutputStream outputStream;
    public static final String DEVICE_NAME = "device_name";
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_WRITE = 3;
    private static final String CHANNEL_ID = "my_notification_channel";
    private static int MOOD_NOTIFICATIONS = R.layout.main;
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 1;
    public static final String TOAST = "toast";
    private final int GROUP = 0;
    private final int MENU_ABOUT = 2;
    private final int MENU_EXIT = 3;
    private final int MENU_SETNAME = 4;
    private final int MENU_TIMER = 1;
    /* access modifiers changed from: private */
    public ToggleButton btn1;
    /* access modifiers changed from: private */
    public ToggleButton btn2;
    /* access modifiers changed from: private */
    public ToggleButton btn3;
    /* access modifiers changed from: private */
    public ToggleButton btn4;
    /* access modifiers changed from: private */
    public ToggleButton btn5;
    /* access modifiers changed from: private */
    public ToggleButton btn6;
    /* access modifiers changed from: private */
    public ToggleButton btn7;
    /* access modifiers changed from: private */
    public ToggleButton btn8;
    /* access modifiers changed from: private */
    public ToggleButton btnT1;
    /* access modifiers changed from: private */
    public ToggleButton btnT2;
    /* access modifiers changed from: private */
    public ToggleButton btnT3;
    /* access modifiers changed from: private */
    public ToggleButton btnT4;
    /* access modifiers changed from: private */
    public ToggleButton btnT5;
    /* access modifiers changed from: private */
    public ToggleButton btnT6;
    /* access modifiers changed from: private */
    public ToggleButton btnT7;
    /* access modifiers changed from: private */
    public ToggleButton btnT8;
    String deviceName1_off;
    String deviceName1_on;
    String deviceName2_off;
    String deviceName2_on;
    String deviceName3_off;
    String deviceName3_on;
    String deviceName4_off;
    String deviceName4_on;
    String deviceName5_off;
    String deviceName5_on;
    String deviceName6_off;
    String deviceName6_on;
    String deviceName7_off;
    String deviceName7_on;
    String deviceName8_off;
    String deviceName8_on;
    private Button disConnectButton;
    SharedPreferences.Editor editor;

    /* access modifiers changed from: private */
    public TextView mBTStatus;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Button mConnectButton;
    /* access modifiers changed from: private */
    public String mConnectedDeviceName = null;
    private final Handler mHandler = new Handler() {
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    switch (msg.arg1) {
                        case 0:
                            BluetoothControl.this.mBTStatus.setText("Bluetooth not connected");
                            return;
                        case 2:
                            BluetoothControl.this.mBTStatus.setText(R.string.title_connecting);
                            return;
                        case 3:
                            BluetoothControl.this.mBTStatus.setText(R.string.title_connected_to);
                            BluetoothControl.this.mBTStatus.append("\n" + BluetoothControl.this.mConnectedDeviceName);
                            return;
                        default:
                            return;
                    }
                case 2:
                    new String((byte[]) msg.obj, 0, msg.arg1);
                    return;
                case 4:
                    BluetoothControl.this.mConnectedDeviceName = msg.getData().getString(BluetoothControl.DEVICE_NAME);
                    Toast.makeText(BluetoothControl.this.getApplicationContext(), "Connected to " + BluetoothControl.this.mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    return;
                case 5:
                    Toast.makeText(BluetoothControl.this.getApplicationContext(), msg.getData().getString(BluetoothControl.TOAST), Toast.LENGTH_SHORT).show();
                    return;
                default:
                    return;
            }
        }
    };
    private NotificationManager mNotificationManager;
    /* access modifiers changed from: private */
    public BluetoothService mRfcommClient = null;
    /* access modifiers changed from: private */
    public long mStartTime1 = 0;
    /* access modifiers changed from: private */
    public long mStartTime2 = 0;
    /* access modifiers changed from: private */
    public long mStartTime3 = 0;
    /* access modifiers changed from: private */
    public long mStartTime4 = 0;
    /* access modifiers changed from: private */
    public long mStartTime5 = 0;
    /* access modifiers changed from: private */
    public long mStartTime6 = 0;
    /* access modifiers changed from: private */
    public long mStartTime7 = 0;
    /* access modifiers changed from: private */
    public long mStartTime8 = 0;
    /* access modifiers changed from: private */
    public TextView mTime1;
    /* access modifiers changed from: private */
    public TextView mTime2;
    /* access modifiers changed from: private */
    public TextView mTime3;
    /* access modifiers changed from: private */
    public TextView mTime4;
    /* access modifiers changed from: private */
    public TextView mTime5;
    /* access modifiers changed from: private */
    public TextView mTime6;
    /* access modifiers changed from: private */
    public TextView mTime7;
    /* access modifiers changed from: private */
    public TextView mTime8;
    /* access modifiers changed from: private */
    public Runnable mUpdateTimeTask1 = new Runnable() {
        public void run() {
            int seconds = (int) ((SystemClock.uptimeMillis() - BluetoothControl.this.mStartTime1) / 1000);
            int minutes = seconds / 60;
            int hr = minutes / 60;
            int minutes2 = minutes % 60;
            int seconds2 = seconds % 60;
            BluetoothControl.this.tv1.setTextColor(-16711936);
            BluetoothControl.this.tv1.setText("Time " + String.format("%02d", new Object[]{Integer.valueOf(hr)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)}));
            BluetoothControl.this.timerStop1 = String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(hr)})) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)});
            if (minutes2 == 59) {
                BluetoothControl.this.tv1.setText("1 Hour Alarm");
                Toast.makeText(BluetoothControl.this.getApplicationContext(), "1 Hour Alarm", Toast.LENGTH_SHORT).show();
            }
            BluetoothControl.this.timer1Handler.postDelayed(this, 200);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateTimeTask2 = new Runnable() {
        public void run() {
            int seconds = (int) ((SystemClock.uptimeMillis() - BluetoothControl.this.mStartTime2) / 1000);
            int minutes = seconds / 60;
            int hr = minutes / 60;
            int minutes2 = minutes % 60;
            int seconds2 = seconds % 60;
            BluetoothControl.this.tv2.setTextColor(-16711936);
            BluetoothControl.this.tv2.setText("Time " + String.format("%02d", new Object[]{Integer.valueOf(hr)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)}));
            BluetoothControl.this.timerStop2 = String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(hr)})) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)});
            if (minutes2 == 59) {
                BluetoothControl.this.tv2.setText("1 Hour Alarm");
                Toast.makeText(BluetoothControl.this.getApplicationContext(), "1 Hour Alarm", Toast.LENGTH_SHORT).show();
            }
            BluetoothControl.this.timer2Handler.postDelayed(this, 200);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateTimeTask3 = new Runnable() {
        public void run() {
            int seconds = (int) ((SystemClock.uptimeMillis() - BluetoothControl.this.mStartTime3) / 1000);
            int minutes = seconds / 60;
            int hr = minutes / 60;
            int minutes2 = minutes % 60;
            int seconds2 = seconds % 60;
            BluetoothControl.this.tv3.setTextColor(-16711936);
            BluetoothControl.this.tv3.setText("Time " + String.format("%02d", new Object[]{Integer.valueOf(hr)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)}));
            BluetoothControl.this.timerStop3 = String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(hr)})) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)});
            if (minutes2 == 59) {
                BluetoothControl.this.tv3.setText("1 Hour Alarm");
                Toast.makeText(BluetoothControl.this.getApplicationContext(), "1 Hour Alarm", Toast.LENGTH_SHORT).show();
            }
            BluetoothControl.this.timer3Handler.postDelayed(this, 200);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateTimeTask4 = new Runnable() {
        public void run() {
            int seconds = (int) ((SystemClock.uptimeMillis() - BluetoothControl.this.mStartTime4) / 1000);
            int minutes = seconds / 60;
            int hr = minutes / 60;
            int minutes2 = minutes % 60;
            int seconds2 = seconds % 60;
            BluetoothControl.this.tv4.setTextColor(-16711936);
            BluetoothControl.this.tv4.setText("Time " + String.format("%02d", new Object[]{Integer.valueOf(hr)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)}));
            BluetoothControl.this.timerStop4 = String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(hr)})) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)});
            if (minutes2 == 59) {
                BluetoothControl.this.tv4.setText("1 Hour Alarm");
                Toast.makeText(BluetoothControl.this.getApplicationContext(), "1 Hour Alarm", Toast.LENGTH_SHORT).show();
            }
            BluetoothControl.this.timer4Handler.postDelayed(this, 200);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateTimeTask5 = new Runnable() {
        public void run() {
            int seconds = (int) ((SystemClock.uptimeMillis() - BluetoothControl.this.mStartTime5) / 1000);
            int minutes = seconds / 60;
            int hr = minutes / 60;
            int minutes2 = minutes % 60;
            int seconds2 = seconds % 60;
            BluetoothControl.this.tv5.setTextColor(-16711936);
            BluetoothControl.this.tv5.setText("Time " + String.format("%02d", new Object[]{Integer.valueOf(hr)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)}));
            BluetoothControl.this.timerStop5 = String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(hr)})) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)});
            if (minutes2 == 59) {
                BluetoothControl.this.tv5.setText("1 Hour Alarm");
                Toast.makeText(BluetoothControl.this.getApplicationContext(), "1 Hour Alarm", Toast.LENGTH_SHORT).show();
            }
            BluetoothControl.this.timer5Handler.postDelayed(this, 200);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateTimeTask6 = new Runnable() {
        public void run() {
            int seconds = (int) ((SystemClock.uptimeMillis() - BluetoothControl.this.mStartTime6) / 1000);
            int minutes = seconds / 60;
            int hr = minutes / 60;
            int minutes2 = minutes % 60;
            int seconds2 = seconds % 60;
            BluetoothControl.this.tv6.setTextColor(-16711936);
            BluetoothControl.this.tv6.setText("Time " + String.format("%02d", new Object[]{Integer.valueOf(hr)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)}));
            BluetoothControl.this.timerStop6 = String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(hr)})) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)});
            if (minutes2 == 59) {
                BluetoothControl.this.tv6.setText("1 Hour Alarm");
                Toast.makeText(BluetoothControl.this.getApplicationContext(), "1 Hour Alarm", Toast.LENGTH_SHORT).show();
            }
            BluetoothControl.this.timer6Handler.postDelayed(this, 200);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateTimeTask7 = new Runnable() {
        public void run() {
            int seconds = (int) ((SystemClock.uptimeMillis() - BluetoothControl.this.mStartTime7) / 1000);
            int minutes = seconds / 60;
            int hr = minutes / 60;
            int minutes2 = minutes % 60;
            int seconds2 = seconds % 60;
            BluetoothControl.this.tv7.setTextColor(-16711936);
            BluetoothControl.this.tv7.setText("Time " + String.format("%02d", new Object[]{Integer.valueOf(hr)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)}));
            BluetoothControl.this.timerStop7 = String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(hr)})) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)});
            if (minutes2 == 59) {
                BluetoothControl.this.tv7.setText("1 Hour Alarm");
                Toast.makeText(BluetoothControl.this.getApplicationContext(), "1 Hour Alarm", Toast.LENGTH_SHORT).show();
            }
            BluetoothControl.this.timer7Handler.postDelayed(this, 200);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateTimeTask8 = new Runnable() {
        public void run() {
            int seconds = (int) ((SystemClock.uptimeMillis() - BluetoothControl.this.mStartTime8) / 1000);
            int minutes = seconds / 60;
            int hr = minutes / 60;
            int minutes2 = minutes % 60;
            int seconds2 = seconds % 60;
            BluetoothControl.this.tv8.setTextColor(-16711936);
            BluetoothControl.this.tv8.setText("Time " + String.format("%02d", new Object[]{Integer.valueOf(hr)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)}));
            BluetoothControl.this.timerStop8 = String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(hr)})) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2)});
            if (minutes2 == 59) {
                BluetoothControl.this.tv8.setText("1 Hour Alarm");
                Toast.makeText(BluetoothControl.this.getApplicationContext(), "1 Hour Alarm", Toast.LENGTH_SHORT).show();
            }
            BluetoothControl.this.timer8Handler.postDelayed(this, 200);
        }
    };
    SharedPreferences myprefs;
    public long t1;
    public long t2;
    public long t3;
    public long t4;
    public long t5;
    public long t6;
    public long t7;
    public long t8;
    CountDownTimer timer1;
    /* access modifiers changed from: private */
    public Handler timer1Handler = new Handler();
    CountDownTimer timer2;
    /* access modifiers changed from: private */
    public Handler timer2Handler = new Handler();
    CountDownTimer timer3;
    /* access modifiers changed from: private */
    public Handler timer3Handler = new Handler();
    CountDownTimer timer4;
    /* access modifiers changed from: private */
    public Handler timer4Handler = new Handler();
    CountDownTimer timer5;
    /* access modifiers changed from: private */
    public Handler timer5Handler = new Handler();
    CountDownTimer timer6;
    /* access modifiers changed from: private */
    public Handler timer6Handler = new Handler();
    CountDownTimer timer7;
    /* access modifiers changed from: private */
    public Handler timer7Handler = new Handler();
    CountDownTimer timer8;
    /* access modifiers changed from: private */
    public Handler timer8Handler = new Handler();
    String timerStop1;
    String timerStop2;
    String timerStop3;
    String timerStop4;
    String timerStop5;
    String timerStop6;
    String timerStop7;
    String timerStop8;
    boolean toggle = false;
    String tt1;
    String tt2;
    String tt3;
    String tt4;
    String tt5;
    String tt6;
    String tt7;
    String tt8;
    /* access modifiers changed from: private */
    public TextView tv1;
    /* access modifiers changed from: private */
    public TextView tv2;
    /* access modifiers changed from: private */
    public TextView tv3;
    /* access modifiers changed from: private */
    public TextView tv4;
    /* access modifiers changed from: private */
    public TextView tv5;
    /* access modifiers changed from: private */
    public TextView tv6;
    /* access modifiers changed from: private */
    public TextView tv7;
    /* access modifiers changed from: private */
    public TextView tv8;
    private TextView tvName1;
    private TextView tvName2;
    private TextView tvName3;
    private TextView tvName4;
    private TextView tvName5;
    private TextView tvName6;
    private TextView tvName7;
    private TextView tvName8;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (this.mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        this.myprefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.deviceName1_on = this.myprefs.getString("device_name1_on", (String) null);
        this.deviceName2_on = this.myprefs.getString("device_name2_on", (String) null);
        this.deviceName3_on = this.myprefs.getString("device_name3_on", (String) null);
        this.deviceName4_on = this.myprefs.getString("device_name4_on", (String) null);
        this.deviceName5_on = this.myprefs.getString("device_name5_on", (String) null);
        this.deviceName6_on = this.myprefs.getString("device_name6_on", (String) null);
        this.deviceName7_on = this.myprefs.getString("device_name7_on", (String) null);
        this.deviceName8_on = this.myprefs.getString("device_name8_on", (String) null);
    }

    public void onStart() {
        super.onStart();
        if (!this.mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        } else if (this.mRfcommClient == null) {
            setup();
        }
    }

    public synchronized void onResume() {
        super.onResume();
        if (this.mRfcommClient != null && this.mRfcommClient.getState() == 0) {
            this.mRfcommClient.start();
        }
    }

    public void onClick(View v) {
        int buttonID = v.getId();
        this.myprefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.tt1 = this.myprefs.getString(Preferences.KEY_TIMER1, (String) null);
        this.tt2 = this.myprefs.getString(Preferences.KEY_TIMER2, (String) null);
        this.tt3 = this.myprefs.getString(Preferences.KEY_TIMER3, (String) null);
        this.tt4 = this.myprefs.getString(Preferences.KEY_TIMER4, (String) null);
        this.tt5 = this.myprefs.getString(Preferences.KEY_TIMER5, (String) null);
        this.tt6 = this.myprefs.getString(Preferences.KEY_TIMER6, (String) null);
        this.tt7 = this.myprefs.getString(Preferences.KEY_TIMER7, (String) null);
        this.tt8 = this.myprefs.getString(Preferences.KEY_TIMER8, (String) null);
        if (this.tt1 != null) {
            this.t1 = Long.parseLong(this.tt1);
            this.t2 = Long.parseLong(this.tt2);
            this.t3 = Long.parseLong(this.tt3);
            this.t4 = Long.parseLong(this.tt4);
            this.t5 = Long.parseLong(this.tt5);
            this.t6 = Long.parseLong(this.tt6);
            this.t7 = Long.parseLong(this.tt7);
            this.t8 = Long.parseLong(this.tt8);
        } else {
            startActivity(new Intent(this, Preferences.class));
        }

            if (buttonID == R.id.togglebtn1){
                if (this.btn1.isChecked()) {
                    sendMessage("A");
                    if (this.mStartTime1 == 0) {
                        this.mStartTime1 = SystemClock.uptimeMillis();
                        this.timer1Handler.removeCallbacks(this.mUpdateTimeTask1);
                        this.timer1Handler.postDelayed(this.mUpdateTimeTask1, 100);
                        return;
                    }
                    return;
                }
                sendMessage("a");
                this.timer1Handler.removeCallbacks(this.mUpdateTimeTask1);
                this.tv1.setText("Time = " + this.timerStop1);
                this.tv1.setTextColor(-65536);
                this.mStartTime1 = 0;
                return;
            }
            else if (buttonID == R.id.toggletimer1){
                if (this.btnT1.isChecked()) {
                    this.timer1 = new CountDownTimer(this.t1, 1000) {
                        public void onFinish() {
                            BluetoothControl.this.mTime1.setText("done!");
                            BluetoothControl.this.setDefault(1);
                            if (BluetoothControl.this.btn1.isChecked()) {
                                BluetoothControl.this.sendMessage("a");
                                BluetoothControl.this.timer1Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask1);
                                BluetoothControl.this.tv1.setText("Time = " + BluetoothControl.this.timerStop1);
                                BluetoothControl.this.tv1.setTextColor(-65536);
                                BluetoothControl.this.mStartTime1 = 0;
                                BluetoothControl.this.btn1.setChecked(false);
                            } else {
                                BluetoothControl.this.sendMessage("A");
                                if (BluetoothControl.this.mStartTime1 == 0) {
                                    BluetoothControl.this.mStartTime1 = SystemClock.uptimeMillis();
                                    BluetoothControl.this.timer1Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask1);
                                    BluetoothControl.this.timer1Handler.postDelayed(BluetoothControl.this.mUpdateTimeTask1, 100);
                                }
                                BluetoothControl.this.btn1.setChecked(true);
                            }
                            BluetoothControl.this.btnT1.setChecked(false);
                        }

                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            int minutes = seconds / 60;
                            BluetoothControl.this.mTime1.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds % 60)}));
                        }
                    }.start();
                    return;
                }
                this.timer1.cancel();
                int seconds = (int) (this.t1 / 1000);
                int minutes = seconds / 60;
                this.mTime1.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds % 60)}));
                return;
            }

            else if (buttonID == R.id.togglebtn2){
                if (this.btn2.isChecked()) {
                    sendMessage("B");
                    if (this.mStartTime2 == 0) {
                        this.mStartTime2 = SystemClock.uptimeMillis();
                        this.timer2Handler.removeCallbacks(this.mUpdateTimeTask2);
                        this.timer2Handler.postDelayed(this.mUpdateTimeTask2, 100);
                        return;
                    }
                    return;
                }
                sendMessage("b");
                this.timer2Handler.removeCallbacks(this.mUpdateTimeTask2);
                this.tv2.setText("Time = " + this.timerStop2);
                this.tv2.setTextColor(-65536);
                this.mStartTime2 = 0;
                return;
            }
            else if (buttonID == R.id.toggletimer2){
                if (this.btnT2.isChecked()) {
                    this.timer2 = new CountDownTimer(this.t2, 1000) {
                        public void onFinish() {
                            BluetoothControl.this.mTime2.setText("done!");
                            BluetoothControl.this.setDefault(1);
                            if (BluetoothControl.this.btn2.isChecked()) {
                                BluetoothControl.this.sendMessage("b");
                                BluetoothControl.this.timer2Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask2);
                                BluetoothControl.this.tv2.setText("Time = " + BluetoothControl.this.timerStop2);
                                BluetoothControl.this.tv2.setTextColor(-65536);
                                BluetoothControl.this.mStartTime2 = 0;
                                BluetoothControl.this.btn2.setChecked(false);
                            } else {
                                BluetoothControl.this.sendMessage("B");
                                if (BluetoothControl.this.mStartTime2 == 0) {
                                    BluetoothControl.this.mStartTime2 = SystemClock.uptimeMillis();
                                    BluetoothControl.this.timer2Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask2);
                                    BluetoothControl.this.timer2Handler.postDelayed(BluetoothControl.this.mUpdateTimeTask2, 100);
                                }
                                BluetoothControl.this.btn2.setChecked(true);
                            }
                            BluetoothControl.this.btnT2.setChecked(false);
                        }

                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            int minutes = seconds / 60;
                            BluetoothControl.this.mTime2.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds % 60)}));
                        }
                    }.start();
                    return;
                }
                this.timer2.cancel();
                int seconds2 = (int) (this.t2 / 1000);
                int minutes2 = seconds2 / 60;
                this.mTime2.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes2 / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes2 % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds2 % 60)}));
                return;
            }

            else if (buttonID == R.id.togglebtn3){
                if (this.btn3.isChecked()) {
                    sendMessage("C");
                    if (this.mStartTime3 == 0) {
                        this.mStartTime3 = SystemClock.uptimeMillis();
                        this.timer3Handler.removeCallbacks(this.mUpdateTimeTask3);
                        this.timer3Handler.postDelayed(this.mUpdateTimeTask3, 100);
                        return;
                    }
                    return;
                }
                sendMessage("c");
                this.timer3Handler.removeCallbacks(this.mUpdateTimeTask3);
                this.tv3.setText("Time = " + this.timerStop3);
                this.tv3.setTextColor(-65536);
                this.mStartTime3 = 0;
                return;
            }

            else if (buttonID == R.id.toggletimer3){
                if (this.btnT3.isChecked()) {
                    this.timer3 = new CountDownTimer(this.t3, 1000) {
                        public void onFinish() {
                            BluetoothControl.this.mTime3.setText("done!");
                            BluetoothControl.this.setDefault(1);
                            if (BluetoothControl.this.btn3.isChecked()) {
                                BluetoothControl.this.sendMessage("c");
                                BluetoothControl.this.timer3Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask3);
                                BluetoothControl.this.tv3.setText("Time = " + BluetoothControl.this.timerStop3);
                                BluetoothControl.this.tv3.setTextColor(-65536);
                                BluetoothControl.this.mStartTime3 = 0;
                                BluetoothControl.this.btn3.setChecked(false);
                            } else {
                                BluetoothControl.this.sendMessage("C");
                                if (BluetoothControl.this.mStartTime3 == 0) {
                                    BluetoothControl.this.mStartTime3 = SystemClock.uptimeMillis();
                                    BluetoothControl.this.timer3Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask3);
                                    BluetoothControl.this.timer3Handler.postDelayed(BluetoothControl.this.mUpdateTimeTask3, 100);
                                }
                                BluetoothControl.this.btn3.setChecked(true);
                            }
                            BluetoothControl.this.btnT3.setChecked(false);
                        }

                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            int minutes = seconds / 60;
                            BluetoothControl.this.mTime3.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds % 60)}));
                        }
                    }.start();
                    return;
                }
                this.timer3.cancel();
                int seconds3 = (int) (this.t3 / 1000);
                int minutes3 = seconds3 / 60;
                this.mTime3.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes3 / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes3 % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds3 % 60)}));
                return;
            }

            else if (buttonID == R.id.togglebtn4){
                if (this.btn4.isChecked()) {
                    sendMessage("D");
                    if (this.mStartTime4 == 0) {
                        this.mStartTime4 = SystemClock.uptimeMillis();
                        this.timer4Handler.removeCallbacks(this.mUpdateTimeTask4);
                        this.timer4Handler.postDelayed(this.mUpdateTimeTask4, 100);
                        return;
                    }
                    return;
                }
                sendMessage("d");
                this.timer4Handler.removeCallbacks(this.mUpdateTimeTask4);
                this.tv4.setText("Time = " + this.timerStop4);
                this.tv4.setTextColor(-65536);
                this.mStartTime4 = 0;
                return;
            }

            else if (buttonID == R.id.toggletimer4){
                if (this.btnT4.isChecked()) {
                    this.timer4 = new CountDownTimer(this.t4, 1000) {
                        public void onFinish() {
                            BluetoothControl.this.mTime4.setText("done!");
                            BluetoothControl.this.setDefault(1);
                            if (BluetoothControl.this.btn4.isChecked()) {
                                BluetoothControl.this.sendMessage("d");
                                BluetoothControl.this.timer4Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask4);
                                BluetoothControl.this.tv4.setText("Time = " + BluetoothControl.this.timerStop4);
                                BluetoothControl.this.tv4.setTextColor(-65536);
                                BluetoothControl.this.mStartTime4 = 0;
                                BluetoothControl.this.btn4.setChecked(false);
                            } else {
                                BluetoothControl.this.sendMessage("D");
                                if (BluetoothControl.this.mStartTime4 == 0) {
                                    BluetoothControl.this.mStartTime4 = SystemClock.uptimeMillis();
                                    BluetoothControl.this.timer4Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask4);
                                    BluetoothControl.this.timer4Handler.postDelayed(BluetoothControl.this.mUpdateTimeTask4, 100);
                                }
                                BluetoothControl.this.btn4.setChecked(true);
                            }
                            BluetoothControl.this.btnT4.setChecked(false);
                        }

                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            int minutes = seconds / 60;
                            BluetoothControl.this.mTime4.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds % 60)}));
                        }
                    }.start();
                    return;
                }
                this.timer4.cancel();
                int seconds4 = (int) (this.t4 / 1000);
                int minutes4 = seconds4 / 60;
                this.mTime4.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes4 / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes4 % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds4 % 60)}));
                return;
            }

            else if (buttonID == R.id.togglebtn5){
                if (this.btn5.isChecked()) {
                    sendMessage("E");
                    if (this.mStartTime5 == 0) {
                        this.mStartTime5 = SystemClock.uptimeMillis();
                        this.timer5Handler.removeCallbacks(this.mUpdateTimeTask5);
                        this.timer5Handler.postDelayed(this.mUpdateTimeTask5, 100);
                        return;
                    }
                    return;
                }
                sendMessage("e");
                this.timer5Handler.removeCallbacks(this.mUpdateTimeTask5);
                this.tv5.setText("Time = " + this.timerStop5);
                this.tv5.setTextColor(-65536);
                this.mStartTime5 = 0;
                return;
            }

            else if (buttonID == R.id.toggletimer5){
                if (this.btnT5.isChecked()) {
                    this.timer5 = new CountDownTimer(this.t5, 1000) {
                        public void onFinish() {
                            BluetoothControl.this.mTime5.setText("done!");
                            BluetoothControl.this.setDefault(1);
                            if (BluetoothControl.this.btn5.isChecked()) {
                                BluetoothControl.this.sendMessage("e");
                                BluetoothControl.this.timer2Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask5);
                                BluetoothControl.this.tv5.setText("Time = " + BluetoothControl.this.timerStop5);
                                BluetoothControl.this.tv5.setTextColor(-65536);
                                BluetoothControl.this.mStartTime5 = 0;
                                BluetoothControl.this.btn5.setChecked(false);
                            } else {
                                BluetoothControl.this.sendMessage("E");
                                if (BluetoothControl.this.mStartTime5 == 0) {
                                    BluetoothControl.this.mStartTime5 = SystemClock.uptimeMillis();
                                    BluetoothControl.this.timer5Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask5);
                                    BluetoothControl.this.timer5Handler.postDelayed(BluetoothControl.this.mUpdateTimeTask5, 100);
                                }
                                BluetoothControl.this.btn5.setChecked(true);
                            }
                            BluetoothControl.this.btnT5.setChecked(false);
                        }

                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            int minutes = seconds / 60;
                            BluetoothControl.this.mTime5.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds % 60)}));
                        }
                    }.start();
                    return;
                }
                this.timer5.cancel();
                int seconds5 = (int) (this.t5 / 1000);
                int minutes5 = seconds5 / 60;
                this.mTime5.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes5 / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes5 % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds5 % 60)}));
                return;
            }

            else if (buttonID == R.id.togglebtn6){
                if (this.btn6.isChecked()) {
                    sendMessage("F");
                    if (this.mStartTime6 == 0) {
                        this.mStartTime6 = SystemClock.uptimeMillis();
                        this.timer6Handler.removeCallbacks(this.mUpdateTimeTask6);
                        this.timer6Handler.postDelayed(this.mUpdateTimeTask6, 100);
                        return;
                    }
                    return;
                }
                sendMessage("f");
                this.timer6Handler.removeCallbacks(this.mUpdateTimeTask6);
                this.tv6.setText("Time = " + this.timerStop6);
                this.tv6.setTextColor(-65536);
                this.mStartTime6 = 0;
                return;
            }

            else if (buttonID == R.id.toggletimer6){
                if (this.btnT6.isChecked()) {
                    this.timer6 = new CountDownTimer(this.t6, 1000) {
                        public void onFinish() {
                            BluetoothControl.this.mTime6.setText("done!");
                            BluetoothControl.this.setDefault(1);
                            if (BluetoothControl.this.btn6.isChecked()) {
                                BluetoothControl.this.sendMessage("f");
                                BluetoothControl.this.timer6Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask4);
                                BluetoothControl.this.tv6.setText("Time = " + BluetoothControl.this.timerStop6);
                                BluetoothControl.this.tv6.setTextColor(-65536);
                                BluetoothControl.this.mStartTime6 = 0;
                                BluetoothControl.this.btn6.setChecked(false);
                            } else {
                                BluetoothControl.this.sendMessage("F");
                                if (BluetoothControl.this.mStartTime6 == 0) {
                                    BluetoothControl.this.mStartTime6 = SystemClock.uptimeMillis();
                                    BluetoothControl.this.timer6Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask6);
                                    BluetoothControl.this.timer6Handler.postDelayed(BluetoothControl.this.mUpdateTimeTask6, 100);
                                }
                                BluetoothControl.this.btn6.setChecked(true);
                            }
                            BluetoothControl.this.btnT6.setChecked(false);
                        }

                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            int minutes = seconds / 60;
                            BluetoothControl.this.mTime6.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds % 60)}));
                        }
                    }.start();
                    return;
                }
                this.timer6.cancel();
                int seconds6 = (int) (this.t6 / 1000);
                int minutes6 = seconds6 / 60;
                this.mTime6.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes6 / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes6 % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds6 % 60)}));
                return;
            }

            else if (buttonID == R.id.togglebtn7){
                if (this.btn7.isChecked()) {
                    sendMessage("G");
                    if (this.mStartTime7 == 0) {
                        this.mStartTime7 = SystemClock.uptimeMillis();
                        this.timer7Handler.removeCallbacks(this.mUpdateTimeTask7);
                        this.timer7Handler.postDelayed(this.mUpdateTimeTask7, 100);
                        return;
                    }
                    return;
                }
                sendMessage("g");
                this.timer7Handler.removeCallbacks(this.mUpdateTimeTask7);
                this.tv7.setText("Time = " + this.timerStop7);
                this.tv7.setTextColor(-65536);
                this.mStartTime7 = 0;
                return;
            }

            else if (buttonID == R.id.toggletimer7){
                if (this.btnT7.isChecked()) {
                    this.timer7 = new CountDownTimer(this.t7, 1000) {
                        public void onFinish() {
                            BluetoothControl.this.mTime7.setText("done!");
                            BluetoothControl.this.setDefault(1);
                            if (BluetoothControl.this.btn7.isChecked()) {
                                BluetoothControl.this.sendMessage("g");
                                BluetoothControl.this.timer7Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask7);
                                BluetoothControl.this.tv7.setText("Time = " + BluetoothControl.this.timerStop7);
                                BluetoothControl.this.tv7.setTextColor(-65536);
                                BluetoothControl.this.mStartTime7 = 0;
                                BluetoothControl.this.btn7.setChecked(false);
                            } else {
                                BluetoothControl.this.sendMessage("G");
                                if (BluetoothControl.this.mStartTime7 == 0) {
                                    BluetoothControl.this.mStartTime7 = SystemClock.uptimeMillis();
                                    BluetoothControl.this.timer7Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask7);
                                    BluetoothControl.this.timer7Handler.postDelayed(BluetoothControl.this.mUpdateTimeTask7, 100);
                                }
                                BluetoothControl.this.btn7.setChecked(true);
                            }
                            BluetoothControl.this.btnT7.setChecked(false);
                        }

                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            int minutes = seconds / 60;
                            BluetoothControl.this.mTime7.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds % 60)}));
                        }
                    }.start();
                    return;
                }
                this.timer7.cancel();
                int seconds7 = (int) (this.t7 / 1000);
                int minutes7 = seconds7 / 60;
                this.mTime7.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes7 / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes7 % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds7 % 60)}));
                return;
            }

            else if (buttonID == R.id.togglebtn8){
                if (this.btn8.isChecked()) {
                    sendMessage("H");
                    if (this.mStartTime8 == 0) {
                        this.mStartTime8 = SystemClock.uptimeMillis();
                        this.timer8Handler.removeCallbacks(this.mUpdateTimeTask8);
                        this.timer8Handler.postDelayed(this.mUpdateTimeTask8, 100);
                        return;
                    }
                    return;
                }
                sendMessage("h");
                this.timer8Handler.removeCallbacks(this.mUpdateTimeTask8);
                this.tv8.setText("Time = " + this.timerStop8);
                this.tv8.setTextColor(-65536);
                this.mStartTime8 = 0;
                return;
            }
            else if (buttonID == R.id.toggletimer8){
                if (this.btnT8.isChecked()) {
                    this.timer8 = new CountDownTimer(this.t8, 1000) {
                        public void onFinish() {
                            BluetoothControl.this.mTime8.setText("done!");
                            BluetoothControl.this.setDefault(1);
                            if (BluetoothControl.this.btn8.isChecked()) {
                                BluetoothControl.this.sendMessage("h");
                                BluetoothControl.this.timer2Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask8);
                                BluetoothControl.this.tv8.setText("Time = " + BluetoothControl.this.timerStop8);
                                BluetoothControl.this.tv8.setTextColor(-65536);
                                BluetoothControl.this.mStartTime8 = 0;
                                BluetoothControl.this.btn8.setChecked(false);
                            } else {
                                BluetoothControl.this.sendMessage("H");
                                if (BluetoothControl.this.mStartTime8 == 0) {
                                    BluetoothControl.this.mStartTime8 = SystemClock.uptimeMillis();
                                    BluetoothControl.this.timer8Handler.removeCallbacks(BluetoothControl.this.mUpdateTimeTask8);
                                    BluetoothControl.this.timer8Handler.postDelayed(BluetoothControl.this.mUpdateTimeTask8, 100);
                                }
                                BluetoothControl.this.btn8.setChecked(true);
                            }
                            BluetoothControl.this.btnT8.setChecked(false);
                        }

                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            int minutes = seconds / 60;
                            BluetoothControl.this.mTime8.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds % 60)}));
                        }
                    }.start();
                    return;
                }
                this.timer8.cancel();
                int seconds8 = (int) (this.t8 / 1000);
                int minutes8 = seconds8 / 60;
                this.mTime8.setText(String.format("%02d", new Object[]{Integer.valueOf(minutes8 / 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minutes8 % 60)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds8 % 60)}));
                return;
            }else {
                return;
            }


    }

    public void onStop() {
        super.onStop();
        if (this.toggle ) {
        }
        this.toggle = !this.toggle;
    }

    public void onDestroy() {
        super.onDestroy();

        if (this.mRfcommClient != null) {
            this.mRfcommClient.stop();
        }
    }

    /* access modifiers changed from: private */
    public void sendMessage(String message) {
//        if (this.mRfcommClient.getState() != 3) {
//            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
//        } else if (message.length() > 0) {
//            this.mRfcommClient.write(message.getBytes());
//        }

        sendData(message);

    }
    private void sendData(String message) {
        if (outputStream == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth connection not established", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            outputStream.write(message.getBytes());
            outputStream.flush();
            Toast.makeText(getApplicationContext(), "Data sent: " + message, Toast.LENGTH_SHORT).show();
            resetOutputStream();
        } catch (IOException e) {
            resetOutputStream();
            Toast.makeText(getApplicationContext(), "Failed to send data"+e.getMessage(), Toast.LENGTH_LONG).show();
            reconnect(mmDevice.getName());
            e.printStackTrace();

        }
    }
    private void reconnect(String deviceName) {
        // Close the existing socket and output stream
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (mmSocket != null) {
                mmSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream = null;
            mmSocket = null;

            connectAgainDevice(deviceName);
        }
    }

    private void cancelConnection() {
        if (connectThread != null) {
            connectThread.cancel();
        }
    }

    private void closeOutputStream() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void resetOutputStream() {
//        closeOutputStream();
        outputStream = null;
        if (mmSocket != null && mmSocket.isConnected()) {
            try {
                outputStream = mmSocket.getOutputStream();
            } catch (IOException e) {
                reconnect(mmDevice.getName());
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported or not enabled", Toast.LENGTH_SHORT).show();
            reconnect(mmDevice.getName());
        }
    }

    private void setup() {
        this.tvName1 = (TextView) findViewById(R.id.textViewName1);
        this.tvName2 = (TextView) findViewById(R.id.textViewName2);
        this.tvName3 = (TextView) findViewById(R.id.textViewName3);
        this.tvName4 = (TextView) findViewById(R.id.textViewName4);
        this.tvName5 = (TextView) findViewById(R.id.textViewName5);
        this.tvName6 = (TextView) findViewById(R.id.textViewName6);
        this.tvName7 = (TextView) findViewById(R.id.textViewName7);
        this.tvName8 = (TextView) findViewById(R.id.textViewName8);
        this.mTime1 = (TextView) findViewById(R.id.textView1);
        this.mTime2 = (TextView) findViewById(R.id.textView2);
        this.mTime3 = (TextView) findViewById(R.id.textView3);
        this.mTime4 = (TextView) findViewById(R.id.textView4);
        this.mTime5 = (TextView) findViewById(R.id.textView5);
        this.mTime6 = (TextView) findViewById(R.id.textView6);
        this.mTime7 = (TextView) findViewById(R.id.textView7);
        this.mTime8 = (TextView) findViewById(R.id.textView8);
        this.tv1 = (TextView) findViewById(R.id.textViewOn1);
        this.tv2 = (TextView) findViewById(R.id.textViewOn2);
        this.tv3 = (TextView) findViewById(R.id.TextViewOn3);
        this.tv4 = (TextView) findViewById(R.id.TextViewOn4);
        this.tv5 = (TextView) findViewById(R.id.TextViewOn5);
        this.tv6 = (TextView) findViewById(R.id.TextViewOn6);
        this.tv7 = (TextView) findViewById(R.id.TextViewOn7);
        this.tv8 = (TextView) findViewById(R.id.TextViewOn8);
        this.mBTStatus = (TextView) findViewById(R.id.txt_btstatus);
        this.mConnectButton = (Button) findViewById(R.id.button_connect);
        this.mConnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                BluetoothControl.this.BTConnect();
            }
        });
        this.disConnectButton = (Button) findViewById(R.id.button_disconnect);
        this.disConnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (BluetoothControl.this.mRfcommClient != null) {
                    BluetoothControl.this.mRfcommClient.stop();
                }
                BluetoothControl.this.exitDialog();
            }
        });
        this.btn1 = (ToggleButton) findViewById(R.id.togglebtn1);
        this.btn1.setOnClickListener(this);
        this.btn2 = (ToggleButton) findViewById(R.id.togglebtn2);
        this.btn2.setOnClickListener(this);
        this.btn3 = (ToggleButton) findViewById(R.id.togglebtn3);
        this.btn3.setOnClickListener(this);
        this.btn4 = (ToggleButton) findViewById(R.id.togglebtn4);
        this.btn4.setOnClickListener(this);
        this.btn5 = (ToggleButton) findViewById(R.id.togglebtn5);
        this.btn5.setOnClickListener(this);
        this.btn6 = (ToggleButton) findViewById(R.id.togglebtn6);
        this.btn6.setOnClickListener(this);
        this.btn7 = (ToggleButton) findViewById(R.id.togglebtn7);
        this.btn7.setOnClickListener(this);
        this.btn8 = (ToggleButton) findViewById(R.id.togglebtn8);
        this.btn8.setOnClickListener(this);
        this.btnT1 = (ToggleButton) findViewById(R.id.toggletimer1);
        this.btnT1.setOnClickListener(this);
        this.btnT2 = (ToggleButton) findViewById(R.id.toggletimer2);
        this.btnT2.setOnClickListener(this);
        this.btnT3 = (ToggleButton) findViewById(R.id.toggletimer3);
        this.btnT3.setOnClickListener(this);
        this.btnT4 = (ToggleButton) findViewById(R.id.toggletimer4);
        this.btnT4.setOnClickListener(this);
        this.btnT5 = (ToggleButton) findViewById(R.id.toggletimer5);
        this.btnT5.setOnClickListener(this);
        this.btnT6 = (ToggleButton) findViewById(R.id.toggletimer6);
        this.btnT6.setOnClickListener(this);
        this.btnT7 = (ToggleButton) findViewById(R.id.toggletimer7);
        this.btnT7.setOnClickListener(this);
        this.btnT8 = (ToggleButton) findViewById(R.id.toggletimer8);
        this.btnT8.setOnClickListener(this);
        this.mRfcommClient = new BluetoothService(this, this.mHandler);
        if (this.deviceName1_on != null) {
            this.tvName1.setText(this.deviceName1_on);
        }
        if (this.deviceName2_on != null) {
            this.tvName2.setText(this.deviceName2_on);
        }
        if (this.deviceName3_on != null) {
            this.tvName3.setText(this.deviceName3_on);
        }
        if (this.deviceName4_on != null) {
            this.tvName4.setText(this.deviceName4_on);
        }
        if (this.deviceName5_on != null) {
            this.tvName5.setText(this.deviceName5_on);
        }
        if (this.deviceName6_on != null) {
            this.tvName6.setText(this.deviceName6_on);
        }
        if (this.deviceName7_on != null) {
            this.tvName7.setText(this.deviceName7_on);
        }
        if (this.deviceName8_on != null) {
            this.tvName8.setText(this.deviceName8_on);
        }
    }

    /* access modifiers changed from: private */
    public void BTConnect() {
        startActivityForResult(new Intent(this, DeviceListActivity.class), 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    setup();
                    return;
                }
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
                return;
            case 2:
                if (resultCode == -1) {
//                    this.mRfcommClient.connect(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS)));

                    connectToDevice(data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS));

                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void setDefault(int defaults) {
        // Create a PendingIntent for the notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, BluetoothControl.class), PendingIntent.FLAG_IMMUTABLE);

        // Set the notification text and icon
        CharSequence text = getText(R.string.status_bar_notifications_timer);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.stat_happy)
                .setContentTitle(getText(R.string.status_bar_notifications_title))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        // Set notification defaults
        if ((defaults & Notification.DEFAULT_SOUND) != 0) {
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        if ((defaults & Notification.DEFAULT_VIBRATE) != 0) {
            builder.setVibrate(new long[]{0, 100, 200, 300});
        }
        if ((defaults & Notification.DEFAULT_LIGHTS) != 0) {
            builder.setLights(Color.RED, 1000, 1000);
        }

        // Build and display the notification
        Notification notification = builder.build();
        mNotificationManager.notify(MOOD_NOTIFICATIONS, notification);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 4, 0, R.string.menu_name);
        menu.add(0, 1, 0, R.string.menu_timer);
        menu.add(0, 2, 0, R.string.menu_about);
        menu.add(0, 3, 0, R.string.menu_exit);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent("com.app.control.Preferences"));
                break;
            case 2:
                startActivity(new Intent("com.app.control.about"));
                break;
            case 3:
                exitDialog();
                break;
            case 4:
                startActivity(new Intent("com.app.control.Set_button_name"));
                break;
        }
        return true;
    }

    public void exitDialog() {
//        startActivity(new Intent(this, DialogActivity.class));
    }

    private void connectToDevice(String deviceName) {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported or not enabled", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mmSocket != null && mmSocket.isConnected()) {
            BluetoothControl.this.mBTStatus.setText(R.string.title_connected_to);
            Toast.makeText(getApplicationContext(), "Already connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
            return;
        }

        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals(deviceName)) {
                try {
                    mmDevice = device;
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                    mmSocket.connect();
                    Toast.makeText(getApplicationContext(), "Connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
                    BluetoothControl.this.mBTStatus.setText(R.string.title_connected_to+ mmDevice.getName());
                    outputStream = mmSocket.getOutputStream();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    connectThread = new ConnectThread(device);
                    connectThread.start();
                }
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "Device " + deviceName + " not found in paired devices", Toast.LENGTH_SHORT).show();
    }
    private void connectAgainDevice(String deviceName) {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported or not enabled", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mmSocket != null && mmSocket.isConnected()) {
            BluetoothControl.this.mBTStatus.setText(R.string.title_connected_to);
            Toast.makeText(getApplicationContext(), "Already connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
            return;
        }

        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals(deviceName)) {
                try {
                    mmDevice = device;
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                    mmSocket.connect();
                    Toast.makeText(getApplicationContext(), "Connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
                    BluetoothControl.this.mBTStatus.setText(R.string.title_connected_to+ mmDevice.getName());
                    outputStream = mmSocket.getOutputStream();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    connectThread = new ConnectThread(device);
                    connectThread.start();
                }
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "Device " + deviceName + " not found in paired devices", Toast.LENGTH_SHORT).show();
    }
    public class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
//            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            try {
                mmSocket.connect();
                // If connection successful, you can perform further operations here
                // For example, you can start a separate thread to manage data communication
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
                return;
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
