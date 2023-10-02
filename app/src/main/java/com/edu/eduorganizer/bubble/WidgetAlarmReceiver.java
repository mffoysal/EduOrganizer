package com.edu.eduorganizer.bubble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.edu.eduorganizer.routine.ScheduleDataChanged;
import com.edu.eduorganizer.schedule.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class WidgetAlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_DATA_CHANGED = "com.schedulerec.ACTION_SCHEDULE_DATA_CHANGED";

    private List<ScheduleItem> schedules = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        setSchedule(context,intent);
    }

    private void setSchedule(Context context, Intent intent) {
        Log.e("widget: ", "Clicked SendBroadcast to WidgetAlarmReceiver 123"+intent.getAction());
//        ScheduleItem ittem2 = new ScheduleItem();
//        ittem2.setStart_time("0300:PM");
//        ittem2.setEnd_time("0500:PM");
//        ittem2.setSub_name("Math112");
//        schedules.add(ittem2);
        ScheduleDataChanged scheduleDataChanged = new ScheduleDataChanged();
        scheduleDataChanged.setSchedules(schedules);

        scheduleDataChanged.onReceive(context,new Intent(ACTION_DATA_CHANGED));
//        context.sendBroadcast(new Intent(ACTION_DATA_CHANGED));
    }
}
