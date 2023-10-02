package com.edu.eduorganizer.bubble;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.db.DatabaseHelper;
import com.edu.eduorganizer.schedule.ScheduleItem;
import com.edu.eduorganizer.widget.WidgetUpdateReceiver;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AppWidget extends AppWidgetProvider {

    private static final String NEXT_ACTION = "com.example.widget.NEXT_ACTION";
    private static final String PREV_ACTION = "com.example.widget.PREV_ACTION";
    private static final String TODAY_ACTION = "com.example.widget.TODAY_ACTION";
    private static final String SELECTED_DAY_KEY = "selected_day_key";
    private List<ScheduleItem> schedules = new ArrayList<>();
    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private int hour = 0;
    private int min = 59;
    Gson gson = new Gson();
    public void setSchedules(List<ScheduleItem> items){
        schedules = items;
    }

    public void setSchedule(ScheduleItem item){
        schedules.add(item);
    }

    public void newSchedules(List<ScheduleItem> items){
        schedules.clear();
        schedules = items;
    }

    public void newSchedule(ScheduleItem item){
        schedules.clear();
        schedules.add(item);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            updateAppWidgets(context,appWidgetManager,appWidgetId);

        }
    }

    private void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_app_widget);

        Intent intent = new Intent(context, WidgetLaunce.class);
        intent.putExtra("day",getSelectedDay(context));
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.addButtonId, pendingIntent);


        Intent nextIntent = new Intent(context, AppWidget.class);
        nextIntent.setAction(NEXT_ACTION);
        nextIntent.putExtra("day",getSelectedDay(context));
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.nextButton, nextPendingIntent);

        Intent prevIntent = new Intent(context, AppWidget.class);
        prevIntent.setAction(PREV_ACTION);
        prevIntent.putExtra("day",getSelectedDay(context));
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(context, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.prevButton, prevPendingIntent);

        Intent todayIntent = new Intent(context, AppWidget.class);
        todayIntent.setAction(TODAY_ACTION);
        todayIntent.putExtra("day",getSelectedDay(context));
        PendingIntent todayPendingIntent = PendingIntent.getBroadcast(context, 0, todayIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widgetHText, todayPendingIntent);


//            Calendar calendar = Calendar.getInstance();
//            String selectedDay = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        String selectedDay = getSelectedDay(context);
        views.setTextViewText(R.id.widgetHText,"Edubox - "+selectedDay);


//            List<ScheduleItem> scheduleItems = fetchScheduleDataFromDatabase(context, selectedDay);
//            List<ScheduleItem> schedules = new ArrayList<>();

//            ScheduleItem ittem = new ScheduleItem();
//            ittem.setStart_time("0100:PM");
//            ittem.setEnd_time("0330:PM");
//            ittem.setSub_name("CSEm");
//            ittem.setSub_code("farhadr");
//            schedules.add(ittem);
//            ScheduleItem ittem2 = new ScheduleItem();
//            ittem2.setStart_time("0300:PM");
//            ittem2.setEnd_time("0500:PM");
//            ittem2.setSub_name("CSE112");
//            ittem2.setSub_code("farhadr");
//            schedules.add(ittem2);

        views.removeAllViews(R.id.widget_container);

        RemoteViews itemView = new RemoteViews(context.getPackageName(), R.layout.widget_item);
//        itemView.setTextViewText(R.id.edu_title_text_view_item_widget, item.getStart_time()+" - "+item.getEnd_time());
//        itemView.setTextViewText(R.id.edu_date_time_text_view_item_widget, item.getSub_name());

        Intent intent1 = new Intent(context,AppWidgetService.class);
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        String schedulesJson = gson.toJson(schedules);
        intent1.putExtra("schedulesJson", schedulesJson);
        intent1.putExtra("day",getSelectedDay(context));
        intent1.setData(Uri.parse(intent1.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.schedule_list_view_widget, intent1);
        views.setEmptyView(R.id.schedule_list_view_widget, R.id.empty_view_widget);

        for (ScheduleItem item : schedules) {
//                LayoutInflater inflater = LayoutInflater.from(context);
//                View view = inflater.inflate(R.layout.routin_item, this, true);

//            RemoteViews itemView = new RemoteViews(context.getPackageName(), R.layout.widget_item);
//            itemView.setTextViewText(R.id.edu_title_text_view_item_widget, item.getStart_time()+" - "+item.getEnd_time());
//            itemView.setTextViewText(R.id.edu_date_time_text_view_item_widget, item.getSub_name());
//
//            Intent intent1 = new Intent(context,AppWidgetService.class);
//            intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
//            intent1.setData(Uri.parse(intent1.toUri(Intent.URI_INTENT_SCHEME)));
//            views.setRemoteAdapter(R.id.schedule_list_view_widget, intent1);
//            views.setEmptyView(R.id.schedule_list_view_widget, R.id.empty_view_widget);

            // Add the item view to the widget layout
//            views.addView(R.id.widget_container, itemView);
        }
//            views.removeAllViews(R.id.widget_container);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private List<ScheduleItem> fetchScheduleDataFromDatabase(Context context, String day) {
        // Fetch schedule data from the SQLite database based on the selected day
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ScheduleCo.ScheduleEntry.COLUMN_SUBJECT,
                ScheduleCo.ScheduleEntry.COLUMN_SUBJECT_CODE,
                ScheduleCo.ScheduleEntry.COLUMN_TIME,
                ScheduleCo.ScheduleEntry.COLUMN_START_TIME,
                ScheduleCo.ScheduleEntry.COLUMN_END_TIME,
                ScheduleCo.ScheduleEntry.COLUMN_DAY,
                ScheduleCo.ScheduleEntry.COLUMN_SECTION,
                ScheduleCo.ScheduleEntry.COLUMN_ROOM,
                ScheduleCo.ScheduleEntry.COLUMN_T_NAME
        };

        String selection = ScheduleCo.ScheduleEntry.COLUMN_DAY + " = ?";
        String[] selectionArgs = { day };

        Cursor cursor = db.query(
                ScheduleCo.ScheduleEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        List<ScheduleItem> scheduleItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            String time = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleCo.ScheduleEntry.COLUMN_TIME));
            String subject = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleCo.ScheduleEntry.COLUMN_SUBJECT));
            String subCode = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleCo.ScheduleEntry.COLUMN_SUBJECT_CODE));
            String section = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleCo.ScheduleEntry.COLUMN_SECTION));
            String room = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleCo.ScheduleEntry.COLUMN_ROOM));
            String startTime = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleCo.ScheduleEntry.COLUMN_START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleCo.ScheduleEntry.COLUMN_END_TIME));
            String scheduleDay = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleCo.ScheduleEntry.COLUMN_DAY));
            String tName = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleCo.ScheduleEntry.COLUMN_T_NAME));
            scheduleItems.add(new ScheduleItem(subject,subCode,tName,room,section,startTime,endTime,scheduleDay));
        }


//        ScheduleItem ittem = new ScheduleItem();
//        ittem.setStart_time("0100:PM");
//        ittem.setEnd_time("0330:PM");
//        ittem.setSub_name("CSE");
//        ittem.setSub_code("CSE");
//        scheduleItems.add(ittem);
//        ScheduleItem ittem2 = new ScheduleItem();
//        ittem2.setStart_time("0300:PM");
//        ittem2.setEnd_time("0500:PM");
//        ittem2.setSub_code("CSE112");
//        ittem2.setSub_name("CSE112");
//        scheduleItems.add(ittem2);
//        ScheduleItem ittem3 = new ScheduleItem();
//        ittem3.setStart_time("0100:PM");
//        ittem3.setEnd_time("0330:PM");
//        ittem3.setSub_code("CSE");
//        ittem3.setSub_name("CSE");
//        scheduleItems.add(ittem3);
//        ScheduleItem ittem21 = new ScheduleItem();
//        ittem21.setStart_time("0300:PM");
//        ittem21.setEnd_time("0500:PM");
//        ittem21.setSub_code("CSE112");
//        ittem21.setSub_name("CSE112");
//        scheduleItems.add(ittem21);
//        ScheduleItem ittem4 = new ScheduleItem();
//        ittem4.setStart_time("0100:PM");
//        ittem4.setEnd_time("0330:PM");
//        ittem4.setSub_code("CSE");
//        ittem4.setSub_name("CSE");
//        scheduleItems.add(ittem4);
//        ScheduleItem ittem22 = new ScheduleItem();
//        ittem22.setStart_time("0300:PM");
//        ittem22.setEnd_time("0500:PM");
//        ittem22.setSub_name("CSE112");
//        ittem22.setSub_code("CSE112");
//        scheduleItems.add(ittem22);
//        ScheduleItem ittem31 = new ScheduleItem();
//        ittem31.setStart_time("0100:PM");
//        ittem31.setEnd_time("0330:PM");
//        ittem31.setSub_code("CSE");
//        ittem31.setSub_name("CSE");
//        scheduleItems.add(ittem31);
//        ScheduleItem ittem212 = new ScheduleItem();
//        ittem212.setStart_time("0300:PM");
//        ittem212.setEnd_time("0500:PM");
//        ittem212.setSub_name("CSE112");
//        ittem212.setSub_code("CSE112");
//        scheduleItems.add(ittem212);
//        ScheduleItem ittem5 = new ScheduleItem();
//        ittem5.setStart_time("0100:PM");
//        ittem5.setEnd_time("0330:PM");
//        ittem5.setSub_name("CSE");
//        ittem5.setSub_code("CSE");
//        scheduleItems.add(ittem5);
//        ScheduleItem ittem223 = new ScheduleItem();
//        ittem223.setStart_time("0300:PM");
//        ittem223.setEnd_time("0500:PM");
//        ittem223.setSub_name("CSE112");
//        ittem223.setSub_code("CSE112");
//        scheduleItems.add(ittem223);
//        ScheduleItem ittem32 = new ScheduleItem();
//        ittem32.setStart_time("0100:PM");
//        ittem32.setEnd_time("0330:PM");
//        ittem32.setSub_name("CSE");
//        ittem32.setSub_code("CSE");
//        scheduleItems.add(ittem32);
//        ScheduleItem ittem211 = new ScheduleItem();
//        ittem211.setStart_time("0300:PM");
//        ittem211.setEnd_time("0500:PM");
//        ittem211.setSub_name("CSE112");
//        ittem211.setSub_code("CSE112");
//        scheduleItems.add(ittem211);
//        ScheduleItem ittem41 = new ScheduleItem();
//        ittem41.setStart_time("0100:PM");
//        ittem41.setEnd_time("0330:PM");
//        ittem41.setSub_name("CSE");
//        ittem41.setSub_code("CSE");
//        scheduleItems.add(ittem41);
//        ScheduleItem ittem221 = new ScheduleItem();
//        ittem221.setStart_time("0300:PM");
//        ittem221.setEnd_time("0500:PM");
//        ittem221.setSub_name("CSE112");
//        ittem221.setSub_code("CSE112");
//        scheduleItems.add(ittem221);
//        ScheduleItem ittem311 = new ScheduleItem();
//        ittem311.setStart_time("0100:PM");
//        ittem311.setEnd_time("0330:PM");
//        ittem311.setSub_name("CSE");
//        ittem311.setSub_code("CSE");
//        scheduleItems.add(ittem311);
//        ScheduleItem ittem2121 = new ScheduleItem();
//        ittem2121.setStart_time("0300:PM");
//        ittem2121.setEnd_time("0500:PM");
//        ittem2121.setSub_name("CSE112");
//        ittem2121.setSub_code("CSE112");
//        scheduleItems.add(ittem2121);

        cursor.close();
        db.close();
        return scheduleItems;
    }

    private String getDayOfWeek(int dayOfWeek) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[dayOfWeek - 1];
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        this.context = context;
        scheduleDailyAlarm(context);
        WidgetAlarm widgetAlarm = new WidgetAlarm(context);
        widgetAlarm.startDailySchedule();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    public static String getNextAction() {
        return NEXT_ACTION;
    }

    public static String getPrevAction() {
        return PREV_ACTION;
    }

    public static String getTodayAction() {
        return TODAY_ACTION;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void scheduleDailyAlarm(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WidgetUpdateReceiver.class);
        intent.putExtra("day",getSelectedDay(context));
        pendingIntent = PendingIntent.getBroadcast(context, 121, intent, PendingIntent.FLAG_IMMUTABLE);

//        WidgetAlarm widgetAlarm = new WidgetAlarm(context);
//        widgetAlarm.startDailySchedule();

        context.sendBroadcast(new Intent("android.appwidget.action.APPWIDGET_UPDATE"));
//        WidgetUpdateReceiver widgetUpdateReceiver = new WidgetUpdateReceiver();
//        widgetUpdateReceiver.onReceive(context,intent);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        } else {
//            Log.e("widget: ", "Clicked SendBroadcast to WidgetAlarm 2 "+intent.getAction());
//
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        }


        // Set the alarm to trigger every 15 minutes
        long intervalMillis = 15 * 60 * 1000; // 15 minutes in milliseconds
        long triggerAtMillis = System.currentTimeMillis() + intervalMillis;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }


//        alarmManager.setInexactRepeating();
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (NEXT_ACTION.equals(intent.getAction())) {
            incrementSelectedDay(context);
            schedules = fetchScheduleDataFromDatabase(context,getSelectedDay(context));
            updateWidgets(context);
        } else if (PREV_ACTION.equals(intent.getAction())) {
            decrementSelectedDay(context);
            schedules = fetchScheduleDataFromDatabase(context,getSelectedDay(context));
            updateWidgets(context);
        }else if (TODAY_ACTION.equals(intent.getAction())) {
            Calendar calendar = Calendar.getInstance();
            String selectedDay = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
            setSelectedDay(context,selectedDay);
            schedules = fetchScheduleDataFromDatabase(context,getSelectedDay(context));
            updateWidgets(context);
        } else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            schedules = fetchScheduleDataFromDatabase(context,getSelectedDay(context));
            updateAppWidgetListData(context);
        }
    }

    private void updateAppWidgetListData(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.schedule_list_view_widget);
    }

    private String getSelectedDay(Context context) {
        Calendar calendar = Calendar.getInstance();
        String selectedDay = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_DAY_KEY, selectedDay);
    }

    private void setSelectedDay(Context context, String day) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_DAY_KEY, day);
        editor.apply();
    }

    private void incrementSelectedDay(Context context) {
        String selectedDay = getSelectedDay(context);

        // Days of the week in chronological order
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        int currentDayIndex = Arrays.asList(daysOfWeek).indexOf(selectedDay);
        int nextDayIndex = (currentDayIndex + 1) % daysOfWeek.length;

        String nextDay = daysOfWeek[nextDayIndex];
        setSelectedDay(context, nextDay);
    }

    private void decrementSelectedDay(Context context) {
        String selectedDay = getSelectedDay(context);

        // Days of the week in reverse chronological order
        String[] daysOfWeek = {"Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday"};

        int currentDayIndex = Arrays.asList(daysOfWeek).indexOf(selectedDay);
        int prevDayIndex = (currentDayIndex + 1) % daysOfWeek.length;

        String prevDay = daysOfWeek[prevDayIndex];
        setSelectedDay(context, prevDay);
    }

    private void updateWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, AppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }



}