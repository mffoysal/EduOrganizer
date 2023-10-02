package com.edu.eduorganizer.bubble;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class WidgetAlarm {
    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private int hour = 0;
    private int min = 47;

    public WidgetAlarm(){

    }
    public WidgetAlarm(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public WidgetAlarm(Context context, AlarmManager alarmManager, PendingIntent pendingIntent) {
        this.context = context;
        this.alarmManager = alarmManager;
        this.pendingIntent = pendingIntent;
    }

    public void startDailySchedule(){
        Intent intent = new Intent(context, WidgetAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 123, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            Log.e("widget: ", "Clicked SendBroadcast to WidgetAlarm 2 "+intent.getAction());

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

//        alarmManager.setInexactRepeating();
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    public void stopDailySchedule(){
        if (alarmManager != null && pendingIntent != null){
            alarmManager.cancel(pendingIntent);
        }
    }

}
