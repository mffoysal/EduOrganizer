package com.edu.eduorganizer.routine;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.edu.eduorganizer.bubble.Widget;
import com.edu.eduorganizer.schedule.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDataChanged extends BroadcastReceiver {


    private Widget widget = new Widget();
    private List<ScheduleItem> schedules = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, Widget.class));
            // Update the widget using onUpdate method or a custom update method
        Log.e("widget: ", "Clicked SendBroadcast to Schedule "+intent.getAction());
        Widget widgetProvider = new Widget();
        widgetProvider.newSchedules(schedules);
        widgetProvider.onUpdate(context, appWidgetManager, appWidgetIds);
        
    }
    public void setSchedules(List<ScheduleItem> schedules){
        this.schedules = schedules;
    }
}
