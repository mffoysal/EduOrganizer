package com.edu.eduorganizer.routine;

import android.database.Cursor;
import android.database.MatrixCursor;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.edu.eduorganizer.bubble.ScheduleContract;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Entity(tableName = "classSchedule")
public class ClassScheduleItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String uniqueId;
    private String sId, stdId, tId, temp_code, temp_num;
    private String sub_name, sub_code, t_id, t_name, room, campus, section;
    private String start_time, end_time;
    private String day,key,sync_key;
    private int min,sync_status;

    private Calendar dateTime;

    public ClassScheduleItem(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSection() {
        return section;
    }

    public String getsId() {
        return sId;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getSync_key() {
        return sync_key;
    }

    public void setSync_key(String sync_key) {
        this.sync_key = sync_key;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public ClassScheduleItem(String time, String subject) {
        this.start_time = time;
        this.sub_name = subject;
    }

//    protected ScheduleItem(Parcel in) {
//        id = in.readInt();
//        sId = in.readString();
//        stdId = in.readString();
//        tId = in.readString();
//        temp_code = in.readString();
//        temp_num = in.readString();
//        sub_name = in.readString();
//        sub_code = in.readString();
//        t_id = in.readString();
//        t_name = in.readString();
//        room = in.readString();
//        campus = in.readString();
//        start_time = in.readString();
//        end_time = in.readString();
//        day = in.readString();
//        min = in.readInt();
//    }

//    public static final Creator<ScheduleItem> CREATOR = new Creator<ScheduleItem>() {
//        @Override
//        public ScheduleItem createFromParcel(Parcel in) {
//            return new ScheduleItem(in);
//        }
//
//        @Override
//        public ScheduleItem[] newArray(int size) {
//            return new ScheduleItem[size];
//        }
//    };

    public static ClassScheduleItem fromCursor(Cursor cursor) {
        ClassScheduleItem scheduleItem = new ClassScheduleItem();

        int idColumnIndex = cursor.getColumnIndex(ScheduleContract.ScheduleEntry._ID);

        if (idColumnIndex != -1){
            scheduleItem.setId(cursor.getInt(idColumnIndex));
        }

        // Check if the column exists in the cursor before attempting to retrieve its value
        int subjectColumnIndex = cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_SUBJECT);
        if (subjectColumnIndex != -1) {
            scheduleItem.setSub_name(cursor.getString(subjectColumnIndex));
        }

        int dateTimeColumnIndex = cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_TIME);
        if (dateTimeColumnIndex != -1) {
            Calendar dateTime = Calendar.getInstance();
            dateTime.setTimeInMillis(cursor.getLong(dateTimeColumnIndex));
            scheduleItem.setDateTime(dateTime);
        }

        return scheduleItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSId() {
        return sId;
    }

    public void setSId(String sId) {
        this.sId = sId;
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public String getTId() {
        return tId;
    }

    public void setTId(String tId) {
        this.tId = tId;
    }

    public String getTemp_code() {
        return temp_code;
    }

    public void setTemp_code(String temp_code) {
        this.temp_code = temp_code;
    }

    public String getTemp_num() {
        return temp_num;
    }

    public void setTemp_num(String temp_num) {
        this.temp_num = temp_num;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String toString(){
        return this.sub_name+this.sub_code;
    }


    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Cursor listToCursor(List<ClassScheduleItem> scheduleItemList) {
        String[] columnNames = {"_id","id", "sId", "stdId", "temp_code", "temp_num","sub_name","sub_code","t_id","t_name","room","campus","start_time","end_time","day","dateTime"};
        MatrixCursor cursor = new MatrixCursor(columnNames);

        for (int i = 0; i < scheduleItemList.size(); i++) {
            ClassScheduleItem item = scheduleItemList.get(i);
            Object[] rowData = {i, item.getId(), item.getSId(), item.getStdId(), item.getTemp_code(),item.getTemp_num(),item.getSub_name(),item.getSub_code(),item.getTId(),item.getT_name(),item.getRoom(),item.getCampus(),item.getStart_time(),item.getEnd_time(),item.getDay(),item.getDateTime()};
            cursor.addRow(rowData);
        }

        return cursor;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(@NonNull Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeString(sId);
//        dest.writeString(stdId);
//        dest.writeString(tId);
//        dest.writeString(temp_code);
//        dest.writeString(temp_num);
//        dest.writeString(sub_name);
//        dest.writeString(sub_code);
//        dest.writeString(t_id);
//        dest.writeString(t_name);
//        dest.writeString(room);
//        dest.writeString(campus);
//        dest.writeString(start_time);
//        dest.writeString(end_time);
//        dest.writeString(day);
//        dest.writeInt(min);
//    }
}
