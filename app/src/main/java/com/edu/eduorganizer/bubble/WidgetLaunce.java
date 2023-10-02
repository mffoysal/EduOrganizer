package com.edu.eduorganizer.bubble;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.routine.ScheduleDataChanged;
import com.edu.eduorganizer.schedule.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class WidgetLaunce extends AppCompatActivity {

    public static final String ACTION_DATA_CHANGED = "com.schedulerec.ACTION_SCHEDULE_DATA_CHANGED";

    private Context context ;
    private List<ScheduleItem> schedules = new ArrayList<>();
    private Widget widget = new Widget();
    private int PERMISSION_REQUEST_CODE;

    //    private BroadcastReceiver dataChangeReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, Widget.class));
//            // Update the widget using onUpdate method or a custom update method
//            Widget widgetProvider = new Widget();
//            Log.e("Schedule: ", "Clicked SendBroadcast");
//
//            widgetProvider.onUpdate(context, appWidgetManager, appWidgetIds);
//            notifyDataChanged();
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_launce);

        IntentFilter filter = new IntentFilter(ACTION_DATA_CHANGED);
        ScheduleDataChanged scheduleDataChanged = new ScheduleDataChanged();
        registerReceiver(scheduleDataChanged, filter);
        context = getApplicationContext();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECEIVE_BOOT_COMPLETED}, PERMISSION_REQUEST_CODE);
        }
//        context.sendBroadcast(new Intent("com.schedulerec.ACTION_SCHEDULE_DATA_CHANGED"));
//        Intent intent = new Intent("com.edubox.admin.ACTION_SCHEDULE_DATA_CHANGED");
//        sendBroadcast(intent);
//        Log.e("widget: ", "Clicked SendBroadcast from Schedule");
//        Intent dataChangedIntent = new Intent("com.schedulerec.ACTION_SCHEDULE_DATA_CHANGED");
//        getApplicationContext().sendBroadcast( new Intent(ACTION_DATA_CHANGED));



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ScheduleDataChanged scheduleDataChanged = new ScheduleDataChanged();
//        unregisterReceiver(scheduleDataChanged);
    }


    private void notifyDataChanged() {
        Intent intent = new Intent(ACTION_DATA_CHANGED);
        sendBroadcast(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        ScheduleItem ittem = new ScheduleItem();
//        ittem.setStart_time("0100:PM");
//        ittem.setEnd_time("0330:PM");
//        ittem.setSub_name("CSE");
//        schedules.add(ittem);
//        ScheduleItem ittem2 = new ScheduleItem();
//        ittem2.setStart_time("0300:PM");
//        ittem2.setEnd_time("0500:PM");
//        ittem2.setSub_name("CSE112");
//        schedules.add(ittem2);
//        ScheduleItem ittem3 = new ScheduleItem();
//        ittem3.setStart_time("0100:PM");
//        ittem3.setEnd_time("0330:PM");
//        ittem3.setSub_name("CSE");
//        schedules.add(ittem3);
//        ScheduleItem ittem21 = new ScheduleItem();
//        ittem21.setStart_time("0300:PM");
//        ittem21.setEnd_time("0500:PM");
//        ittem21.setSub_name("CSE112");
//        schedules.add(ittem21);
//        ScheduleItem ittem4 = new ScheduleItem();
//        ittem4.setStart_time("0100:PM");
//        ittem4.setEnd_time("0330:PM");
//        ittem4.setSub_name("CSE");
//        schedules.add(ittem4);
//        ScheduleItem ittem22 = new ScheduleItem();
//        ittem22.setStart_time("0300:PM");
//        ittem22.setEnd_time("0500:PM");
//        ittem22.setSub_name("CSE112");
//        schedules.add(ittem22);
//        ScheduleItem ittem31 = new ScheduleItem();
//        ittem31.setStart_time("0100:PM");
//        ittem31.setEnd_time("0330:PM");
//        ittem31.setSub_name("CSE");
//        schedules.add(ittem31);
//        ScheduleItem ittem212 = new ScheduleItem();
//        ittem212.setStart_time("0300:PM");
//        ittem212.setEnd_time("0500:PM");
//        ittem212.setSub_name("CSE112");
//        schedules.add(ittem212);
//        ScheduleItem ittem5 = new ScheduleItem();
//        ittem5.setStart_time("0100:PM");
//        ittem5.setEnd_time("0330:PM");
//        ittem5.setSub_name("CSE");
//        schedules.add(ittem5);
//        ScheduleItem ittem223 = new ScheduleItem();
//        ittem223.setStart_time("0300:PM");
//        ittem223.setEnd_time("0500:PM");
//        ittem223.setSub_name("CSE112");
//        schedules.add(ittem223);
//        ScheduleItem ittem32 = new ScheduleItem();
//        ittem32.setStart_time("0100:PM");
//        ittem32.setEnd_time("0330:PM");
//        ittem32.setSub_name("CSE");
//        schedules.add(ittem32);
//        ScheduleItem ittem211 = new ScheduleItem();
//        ittem211.setStart_time("0300:PM");
//        ittem211.setEnd_time("0500:PM");
//        ittem211.setSub_name("CSE112");
//        schedules.add(ittem211);
//        ScheduleItem ittem41 = new ScheduleItem();
//        ittem41.setStart_time("0100:PM");
//        ittem41.setEnd_time("0330:PM");
//        ittem41.setSub_name("CSE");
//        schedules.add(ittem41);
//        ScheduleItem ittem221 = new ScheduleItem();
//        ittem221.setStart_time("0300:PM");
//        ittem221.setEnd_time("0500:PM");
//        ittem221.setSub_name("CSE112");
//        schedules.add(ittem221);
//        ScheduleItem ittem311 = new ScheduleItem();
//        ittem311.setStart_time("0100:PM");
//        ittem311.setEnd_time("0330:PM");
//        ittem311.setSub_name("CSE");
//        schedules.add(ittem311);
//        ScheduleItem ittem2121 = new ScheduleItem();
//        ittem2121.setStart_time("0300:PM");
//        ittem2121.setEnd_time("0500:PM");
//        ittem2121.setSub_name("CSE112");
//        schedules.add(ittem2121);


        ScheduleDataChanged scheduleDataChanged = new ScheduleDataChanged();
        scheduleDataChanged.setSchedules(schedules);

        scheduleDataChanged.onReceive(getApplicationContext(),new Intent(ACTION_DATA_CHANGED));
//        getApplicationContext().sendBroadcast(new Intent(ACTION_DATA_CHANGED));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now use AlarmManager and broadcast receivers
            } else {
                // Permission denied, handle this case
            }
        }
    }


}