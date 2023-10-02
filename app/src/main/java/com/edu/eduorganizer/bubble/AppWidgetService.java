package com.edu.eduorganizer.bubble;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.edu.eduorganizer.schedule.ScheduleItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Gson gson = new Gson();
        String schedulesJson = intent.getStringExtra("schedulesJson");
        Type listType = new TypeToken<List<ScheduleItem>>() {}.getType();
        List<ScheduleItem> schedules = gson.fromJson(schedulesJson, listType);
//        return new AppWidgetFactory(getApplicationContext(), intent);
        return new AppWidgetFactory(getApplicationContext(), intent, schedules);
    }
}
