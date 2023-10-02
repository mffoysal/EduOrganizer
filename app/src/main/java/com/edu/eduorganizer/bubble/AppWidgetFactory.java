package com.edu.eduorganizer.bubble;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.schedule.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class AppWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor cursor;
    private List<ScheduleItem> schedules = new ArrayList<>();

    public AppWidgetFactory(Context applicationContext, Intent intent) {
        this.context = applicationContext;
    }

    public AppWidgetFactory(Context applicationContext, Intent intent, List<ScheduleItem> schedules) {
        this.context = applicationContext;
        this.schedules = schedules;
    }

    public void updateCursor(){
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        Long identityToken = Binder.clearCallingIdentity(); // what the hell is this ?
        String[] projection = {
                ScheduleCo.ScheduleEntry._ID,
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

        cursor = new ScheduleItem().listToCursor(schedules);

        cursor = context.getContentResolver().query(ScheduleCo.ScheduleEntry.CONTENT_URI, projection, ScheduleCo.ScheduleEntry.COLUMN_DAY + " = " + ScheduleCo.ScheduleEntry.COLUMN_DAY, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        updateCursor();
    }

    @Override
    public void onDataSetChanged() {
        updateCursor();
    }

    @Override
    public void onDestroy() {
        if (cursor!=null){
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public int getCount() {

        return schedules.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
//        cursor.moveToPosition(position);
        ScheduleItem scheduleItem = schedules.get(position);
        rv.setTextViewText(R.id.edu_title_text_view_item_widget, scheduleItem.getSub_code()+"   ~   "+scheduleItem.getSection());
//        rv.setTextViewText(R.id.edu_title_text_view_item_widget, scheduleItem.getSub_name()+" - "+scheduleItem.getSub_code());
//        rv.setTextViewText(R.id.edu_title_text_view_item_widget, scheduleItem.getSub_name().replace("\n", " "));
        rv.setTextViewText(R.id.edu_date_time_text_view_item_widget, "  "+scheduleItem.getStart_time()+" - "+scheduleItem.getEnd_time()+"  "+scheduleItem.getT_name());
//        rv.setTextViewText(R.id.edu_date_time_text_view_item_widget, todo.getDateTime().getTimeInMillis() == 0 ? "" : DateFormat.is24HourFormat(context) ? new SimpleDateFormat("MMMM dd, yyyy  h:mm").format(todo.getDateTime().getTime()) : new SimpleDateFormat("MMMM dd, yyyy  h:mm a").format(todo.getDateTime().getTime()));
        rv.setOnClickFillInIntent(R.id.todo_layout_item_widget, new Intent().putExtra(ScheduleCo.ScheduleEntry._ID, scheduleItem.getId()));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (position >= 0 && position < schedules.size()) {
            ScheduleItem scheduleItem = schedules.get(position);
            return scheduleItem.getId();
        }
        return -1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
