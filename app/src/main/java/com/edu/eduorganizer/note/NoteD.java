package com.edu.eduorganizer.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.edu.eduorganizer.User;
import com.edu.eduorganizer.bubble.ScheduleContract;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.entity.Note;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.routine.CScheduleCallback;
import com.edu.eduorganizer.routine.ClassScheduleItem;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NoteD implements UserCallback {
    private static final String TABLE_NOTE = "note";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SID = "sId";
    private static final String COLUMN_UNIQUE_ID = "uniqueId";

    private static final String COLUMN_STD_ID = "stdId";
    private static final String COLUMN_TASK_NAME = "task_name";

    private static final String COLUMN_TASK_CODE = "task_code";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_LOCATION = "task_location";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_STATUS = "aStatus";
    private static final String COLUMN_DATETIME = "dateTime";
    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_UID = "uId";
    private static final String COLUMN_CALENDAR = "calendar";
    private static final String COLUMN_TASK_DETAILS = "task_details";
    private static final String COLUMN_SYNC_STATUS = "sync_status";
    private static final String COLUMN_SYNC_KEY = "sync_key";
    private static final String COLUMN_DONE = "done";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_SCHEDULE_ID = "scheduleId";
    private DatabaseManager databaseManager;

    private FirebaseAuth mAuth;
    private Internet internet;
    private String sId;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = firebaseDatabase.getReference("users");

    private SQLiteDatabase database;

    private static final String SELECT_ALL = "SELECT *  FROM "+TABLE_NOTE;
    private CScheduleCallback scheduleCallback;

    public NoteD(SQLiteDatabase database) {
        this.database = database;
    }

    public NoteD(Context context) {
        databaseManager = new DatabaseManager(context);
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();
        this.database = database;
    }

    public void syncData(){}
    public long insertNote(Note user) {
        long l=-2;
        int r=0;
        r = checkNoteId(user.getUser_id(),user.getTask_code(),user.getTask_id(),user.getUniqueId());
        if(r!=0){
            return -3;
        }else {
            Log.e("note","Note Insertion!");
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_SID, user.getSId());
        values.put(COLUMN_STD_ID, user.getStdId());
        values.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        values.put(COLUMN_DAY, user.getDay());
        values.put(COLUMN_TASK_NAME, user.getTask_name());
        values.put(COLUMN_LOCATION, user.getTask_location());
        values.put(COLUMN_DONE, user.getDone());
        values.put(COLUMN_TASK_DETAILS, user.getTask_details());
        values.put(COLUMN_TASK_CODE, user.getTask_code());
        values.put(COLUMN_CALENDAR, user.getCalendar());
        values.put(COLUMN_DATETIME, user.getDateTime());
        values.put(COLUMN_LINK, user.getLink());
        values.put(COLUMN_URL, user.getUrl());
        values.put(COLUMN_TIME, user.getTime());
        values.put(COLUMN_USER_ID, user.getUser_id());
        values.put(COLUMN_TASK_ID, user.getTask_id());
        values.put(COLUMN_UID, user.getuId());
        values.put(COLUMN_SYNC_KEY, user.getSync_key());
        values.put(COLUMN_SYNC_STATUS, user.getSync_status());
        values.put(COLUMN_SCHEDULE_ID, user.getScheduleId());

        try {
            l = database.insert(TABLE_NOTE, null, values);
        }catch (SQLiteException e){
            String ee = e.getMessage();
            Log.e("error",ee);
        }

        return l;
    }

    public Cursor getNoteCursor(){
        ClassScheduleItem user = new ClassScheduleItem();

        Cursor cursor = database.rawQuery(SELECT_ALL,null);


        return  cursor;
    }

    public int checkNoteId(String userId,String taskCode, String taskId, String uniqueId){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_NOTE + " WHERE user_id = ? AND task_code=? AND task_id=? AND uniqueId=? ",new String[]{userId,taskCode,taskId,uniqueId});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }

    public int checkNote(String taskName,String task_code, String task_id, String day){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_NOTE + " WHERE task_name = ? AND task_code=? AND task_id=? AND day=? ",new String[]{taskName,task_code,task_id,day});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }

    public int checkNote(String taskName,String task_code, String task_id, String day, String stdId){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_NOTE + " WHERE task_name = ? AND task_code=? AND task_id=? AND day=? AND stdId=? ",new String[]{taskName,task_code,task_id,day,stdId});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }



    @Override
    public void onUserRetrieved(User user) {

    }

    @Override
    public void onUserNotFound() {

    }

    public interface UserExistenceCallback {
        void onUserExistenceChecked(boolean userExists);
    }

    public void isUser(String mailPhone, UserExistenceCallback callback) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(mailPhone)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the result of the task
                        SignInMethodQueryResult result = task.getResult();
                        List<String> signInMethods = result.getSignInMethods();

                        if (signInMethods != null && !signInMethods.isEmpty()) {
                            // User exists
                            callback.onUserExistenceChecked(true);
                        } else {
                            // User does not exist
                            callback.onUserExistenceChecked(false);
                        }
                    } else {
                        // An error occurred while checking the sign-in methods
                        Exception exception = task.getException();
                        // Handle the error
                    }
                });
    }


    public int isUser(String mailPhone,String pass){
        int i = 0;
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(mailPhone,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                }else {

                }
            }
        });

        return i;
    }

    public Note getNote(String uniqueId){
        Note user = new Note();

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_NOTE + " WHERE uniqueId = ? OR task_code=? OR task_id=? ",new String[]{uniqueId,uniqueId,uniqueId});

        if (cursor != null && cursor.moveToFirst()){

            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            if (idIndex >= 0) {
                user.setId(cursor.getInt(idIndex));
            }

            int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
            if (sIdIndex >= 0) {
                user.setSId(cursor.getString(sIdIndex));
            }

            int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
            if (uniqueIdIndex >= 0) {
                user.setUniqueId(cursor.getString(uniqueIdIndex));
            }
            int currentLocationIdIndex = cursor.getColumnIndex(COLUMN_LOCATION);
            if (currentLocationIdIndex >= 0) {
                user.setTask_location(cursor.getString(currentLocationIdIndex));
            }
            int dayIdIndex = cursor.getColumnIndex(COLUMN_DAY);
            if (dayIdIndex >= 0) {
                user.setDay(cursor.getString(dayIdIndex));
            }

            int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
            if (syncSIndex >= 0) {
                user.setSync_status(cursor.getInt(syncSIndex));
            }

            int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
            if (syncKdIndex >= 0) {
                user.setSync_key(cursor.getString(syncKdIndex));
            }

            int stdIdIndex = cursor.getColumnIndex(COLUMN_STD_ID);
            if (stdIdIndex >= 0) {
                user.setStdId(cursor.getString(stdIdIndex));
            }

            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            if (userIdIndex >= 0) {
                user.setUser_id(cursor.getString(userIdIndex));
            }

            int subNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
            if (subNameIndex >= 0) {
                user.setTask_name(cursor.getString(subNameIndex));
            }
            int subCodeIndex = cursor.getColumnIndex(COLUMN_TASK_CODE);
            if (subCodeIndex >= 0) {
                user.setTask_code(cursor.getString(subCodeIndex));
            }
            int roomIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
            if (roomIndex >= 0) {
                user.setTask_id(cursor.getString(roomIndex));
            }
            int campusIndex = cursor.getColumnIndex(COLUMN_TASK_DETAILS);
            if (campusIndex >= 0) {
                user.setTask_details(cursor.getString(campusIndex));
            }

            int tCodeIndex = cursor.getColumnIndex(COLUMN_SCHEDULE_ID);
            if (tCodeIndex >= 0) {
                user.setScheduleId(cursor.getString(tCodeIndex));
            }

            int tNumIndex = cursor.getColumnIndex(COLUMN_DATETIME);
            if (tNumIndex >= 0) {
                user.setDateTime(cursor.getString(tNumIndex));
            }

            int tIdIndex = cursor.getColumnIndex(COLUMN_TIME);
            if (tIdIndex >= 0) {
                user.setTime(cursor.getString(tIdIndex));
            }

            int teacherIdIndex = cursor.getColumnIndex(COLUMN_CALENDAR);
            if (teacherIdIndex >= 0) {
                user.setCalendar(cursor.getString(teacherIdIndex));
            }

            int teacherNameIndex = cursor.getColumnIndex(COLUMN_URL);
            if (teacherNameIndex >= 0) {
                user.setUrl(cursor.getString(teacherNameIndex));
            }


            int minIndex = cursor.getColumnIndex(COLUMN_DONE);
            if (minIndex >= 0) {
                user.setDone(cursor.getInt(minIndex));
            }

            int sTimeIndex = cursor.getColumnIndex(COLUMN_LINK);
            if (sTimeIndex >= 0) {
                user.setLink(cursor.getString(sTimeIndex));
            }

            int eTimeIndex = cursor.getColumnIndex(COLUMN_UID);
            if (eTimeIndex >= 0) {
                user.setuId(cursor.getString(eTimeIndex));
            }
        }

        return user;
    }

    public List<Note> getAllNote() {
        List<Note> userList = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NOTE, null, null, null, null, null, "done ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {

                Note user = new Note();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setSId(cursor.getString(sIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
                }
                int currentLocationIdIndex = cursor.getColumnIndex(COLUMN_LOCATION);
                if (currentLocationIdIndex >= 0) {
                    user.setTask_location(cursor.getString(currentLocationIdIndex));
                }
                int dayIdIndex = cursor.getColumnIndex(COLUMN_DAY);
                if (dayIdIndex >= 0) {
                    user.setDay(cursor.getString(dayIdIndex));
                }

                int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
                if (syncSIndex >= 0) {
                    user.setSync_status(cursor.getInt(syncSIndex));
                }

                int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
                if (syncKdIndex >= 0) {
                    user.setSync_key(cursor.getString(syncKdIndex));
                }

                int stdIdIndex = cursor.getColumnIndex(COLUMN_STD_ID);
                if (stdIdIndex >= 0) {
                    user.setStdId(cursor.getString(stdIdIndex));
                }

                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                if (userIdIndex >= 0) {
                    user.setUser_id(cursor.getString(userIdIndex));
                }

                int subNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                if (subNameIndex >= 0) {
                    user.setTask_name(cursor.getString(subNameIndex));
                }
                int subCodeIndex = cursor.getColumnIndex(COLUMN_TASK_CODE);
                if (subCodeIndex >= 0) {
                    user.setTask_code(cursor.getString(subCodeIndex));
                }
                int roomIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
                if (roomIndex >= 0) {
                    user.setTask_id(cursor.getString(roomIndex));
                }
                int campusIndex = cursor.getColumnIndex(COLUMN_TASK_DETAILS);
                if (campusIndex >= 0) {
                    user.setTask_details(cursor.getString(campusIndex));
                }

                int tCodeIndex = cursor.getColumnIndex(COLUMN_SCHEDULE_ID);
                if (tCodeIndex >= 0) {
                    user.setScheduleId(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_DATETIME);
                if (tNumIndex >= 0) {
                    user.setDateTime(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_TIME);
                if (tIdIndex >= 0) {
                    user.setTime(cursor.getString(tIdIndex));
                }

                int teacherIdIndex = cursor.getColumnIndex(COLUMN_CALENDAR);
                if (teacherIdIndex >= 0) {
                    user.setCalendar(cursor.getString(teacherIdIndex));
                }

                int teacherNameIndex = cursor.getColumnIndex(COLUMN_URL);
                if (teacherNameIndex >= 0) {
                    user.setUrl(cursor.getString(teacherNameIndex));
                }


                int minIndex = cursor.getColumnIndex(COLUMN_DONE);
                if (minIndex >= 0) {
                    user.setDone(cursor.getInt(minIndex));
                }

                int sTimeIndex = cursor.getColumnIndex(COLUMN_LINK);
                if (sTimeIndex >= 0) {
                    user.setLink(cursor.getString(sTimeIndex));
                }

                int eTimeIndex = cursor.getColumnIndex(COLUMN_UID);
                if (eTimeIndex >= 0) {
                    user.setuId(cursor.getString(eTimeIndex));
                }

                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return userList;
    }

    public List<Note> getAllNote(String userId, String taskCode) {
        List<Note> userList = new ArrayList<>();

        String selection = "user_id = ? AND (task_code = ? OR task_id = ?)";

        String[] selectionArgs = new String[]{userId, taskCode, taskCode};
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());

//        Cursor cursor = database.query(TABLE_SCHEDULE, null, selection, selectionArgs, null, null, null);

        String sqlQuery = "SELECT * FROM "+TABLE_NOTE+" " +
                "WHERE user_id= ? AND task_code = ? OR task_id = ?" +
                "ORDER BY " +
                "CASE " +
                "WHEN time >= ? THEN 1 " +
                "ELSE 2 " +
                "END, " +
                "time ASC";

        Cursor cursor = database.rawQuery(sqlQuery, new String[]{userId,taskCode,taskCode,currentTime});


        if (cursor != null && cursor.moveToFirst()) {
            do {

                Note user = new Note();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setSId(cursor.getString(sIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
                }
                int currentLocationIdIndex = cursor.getColumnIndex(COLUMN_LOCATION);
                if (currentLocationIdIndex >= 0) {
                    user.setTask_location(cursor.getString(currentLocationIdIndex));
                }
                int dayIdIndex = cursor.getColumnIndex(COLUMN_DAY);
                if (dayIdIndex >= 0) {
                    user.setDay(cursor.getString(dayIdIndex));
                }

                int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
                if (syncSIndex >= 0) {
                    user.setSync_status(cursor.getInt(syncSIndex));
                }

                int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
                if (syncKdIndex >= 0) {
                    user.setSync_key(cursor.getString(syncKdIndex));
                }

                int stdIdIndex = cursor.getColumnIndex(COLUMN_STD_ID);
                if (stdIdIndex >= 0) {
                    user.setStdId(cursor.getString(stdIdIndex));
                }

                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                if (userIdIndex >= 0) {
                    user.setUser_id(cursor.getString(userIdIndex));
                }

                int subNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                if (subNameIndex >= 0) {
                    user.setTask_name(cursor.getString(subNameIndex));
                }
                int subCodeIndex = cursor.getColumnIndex(COLUMN_TASK_CODE);
                if (subCodeIndex >= 0) {
                    user.setTask_code(cursor.getString(subCodeIndex));
                }
                int roomIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
                if (roomIndex >= 0) {
                    user.setTask_id(cursor.getString(roomIndex));
                }
                int campusIndex = cursor.getColumnIndex(COLUMN_TASK_DETAILS);
                if (campusIndex >= 0) {
                    user.setTask_details(cursor.getString(campusIndex));
                }

                int tCodeIndex = cursor.getColumnIndex(COLUMN_SCHEDULE_ID);
                if (tCodeIndex >= 0) {
                    user.setScheduleId(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_DATETIME);
                if (tNumIndex >= 0) {
                    user.setDateTime(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_TIME);
                if (tIdIndex >= 0) {
                    user.setTime(cursor.getString(tIdIndex));
                }

                int teacherIdIndex = cursor.getColumnIndex(COLUMN_CALENDAR);
                if (teacherIdIndex >= 0) {
                    user.setCalendar(cursor.getString(teacherIdIndex));
                }

                int teacherNameIndex = cursor.getColumnIndex(COLUMN_URL);
                if (teacherNameIndex >= 0) {
                    user.setUrl(cursor.getString(teacherNameIndex));
                }


                int minIndex = cursor.getColumnIndex(COLUMN_DONE);
                if (minIndex >= 0) {
                    user.setDone(cursor.getInt(minIndex));
                }

                int sTimeIndex = cursor.getColumnIndex(COLUMN_LINK);
                if (sTimeIndex >= 0) {
                    user.setLink(cursor.getString(sTimeIndex));
                }

                int eTimeIndex = cursor.getColumnIndex(COLUMN_UID);
                if (eTimeIndex >= 0) {
                    user.setuId(cursor.getString(eTimeIndex));
                }

                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return userList;
    }

    public int deleteAllNote(String uniqueId) {
        String whereClause = COLUMN_USER_ID + " = ? OR stdId = ?";
        String[] whereArgs = {uniqueId};

        return database.delete(TABLE_NOTE, whereClause, whereArgs);
    }

    public int deleteAllNoteWithTaskCode(String taskCode) {
        String whereClause = COLUMN_TASK_ID + " = ? OR task_code = ?";
        String[] whereArgs = {taskCode,taskCode};

        return database.delete(TABLE_NOTE, whereClause, whereArgs);
    }

    public int deleteAllNoteWithTempCode(String taskId,String taskCode) {
        String whereClause = COLUMN_TASK_ID + " = ? OR task_code = ?";
        String[] whereArgs = {taskId,taskCode};

        return database.delete(TABLE_NOTE, whereClause, whereArgs);
    }

    public List<Note> getAllNote(String day) {
        List<Note> userList = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE uniqueId = ? OR stdId=? OR tId=? ",new String[]{uniqueId,uniqueId,uniqueId});

        String selection = "day = ? OR day = ?";

        String[] selectionArgs = new String[]{day, "Everyday"};
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());

//        Cursor cursor = database.query(TABLE_SCHEDULE, null, selection, selectionArgs, null, null, null);

//        Cursor cursor = database.query(TABLE_SCHEDULE, null, selection, selectionArgs, null, null, "start_time ASC");
//        Cursor cursor = database.query(TABLE_SCHEDULE, null, selection, selectionArgs, null, null, "start_time ASC","CASE WHEN start_time >= ? THEN 1 ELSE 2 END, start_time ASC", new String[]{currentTime});

//        Cursor cursor = database.query(
//                "schedule",  // Replace with your table name
//                null,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                "event_time ASC",  // Replace with your actual time column name
//                "CASE WHEN event_time >= ? THEN 1 ELSE 2 END, event_time ASC",
//                new String[]{currentTime}
//        );

        String sqlQuery = "SELECT * FROM "+TABLE_NOTE+" " +
                "WHERE day = ? OR day= ? " +
                "ORDER BY " +
                "CASE " +
                "WHEN time >= ? THEN 1 " +
                "ELSE 2 " +
                "END, " +
                "time ASC";

        Cursor cursor = database.rawQuery(sqlQuery, new String[]{day,"Everyday", currentTime});

        if (cursor != null && cursor.moveToFirst()) {
            do {

                Note user = new Note();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setSId(cursor.getString(sIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
                }
                int currentLocationIdIndex = cursor.getColumnIndex(COLUMN_LOCATION);
                if (currentLocationIdIndex >= 0) {
                    user.setTask_location(cursor.getString(currentLocationIdIndex));
                }
                int dayIdIndex = cursor.getColumnIndex(COLUMN_DAY);
                if (dayIdIndex >= 0) {
                    user.setDay(cursor.getString(dayIdIndex));
                }

                int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
                if (syncSIndex >= 0) {
                    user.setSync_status(cursor.getInt(syncSIndex));
                }

                int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
                if (syncKdIndex >= 0) {
                    user.setSync_key(cursor.getString(syncKdIndex));
                }

                int stdIdIndex = cursor.getColumnIndex(COLUMN_STD_ID);
                if (stdIdIndex >= 0) {
                    user.setStdId(cursor.getString(stdIdIndex));
                }

                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                if (userIdIndex >= 0) {
                    user.setUser_id(cursor.getString(userIdIndex));
                }

                int subNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                if (subNameIndex >= 0) {
                    user.setTask_name(cursor.getString(subNameIndex));
                }
                int subCodeIndex = cursor.getColumnIndex(COLUMN_TASK_CODE);
                if (subCodeIndex >= 0) {
                    user.setTask_code(cursor.getString(subCodeIndex));
                }
                int roomIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
                if (roomIndex >= 0) {
                    user.setTask_id(cursor.getString(roomIndex));
                }
                int campusIndex = cursor.getColumnIndex(COLUMN_TASK_DETAILS);
                if (campusIndex >= 0) {
                    user.setTask_details(cursor.getString(campusIndex));
                }

                int tCodeIndex = cursor.getColumnIndex(COLUMN_SCHEDULE_ID);
                if (tCodeIndex >= 0) {
                    user.setScheduleId(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_DATETIME);
                if (tNumIndex >= 0) {
                    user.setDateTime(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_TIME);
                if (tIdIndex >= 0) {
                    user.setTime(cursor.getString(tIdIndex));
                }

                int teacherIdIndex = cursor.getColumnIndex(COLUMN_CALENDAR);
                if (teacherIdIndex >= 0) {
                    user.setCalendar(cursor.getString(teacherIdIndex));
                }

                int teacherNameIndex = cursor.getColumnIndex(COLUMN_URL);
                if (teacherNameIndex >= 0) {
                    user.setUrl(cursor.getString(teacherNameIndex));
                }


                int minIndex = cursor.getColumnIndex(COLUMN_DONE);
                if (minIndex >= 0) {
                    user.setDone(cursor.getInt(minIndex));
                }

                int sTimeIndex = cursor.getColumnIndex(COLUMN_LINK);
                if (sTimeIndex >= 0) {
                    user.setLink(cursor.getString(sTimeIndex));
                }

                int eTimeIndex = cursor.getColumnIndex(COLUMN_UID);
                if (eTimeIndex >= 0) {
                    user.setuId(cursor.getString(eTimeIndex));
                }

                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.d("eee","retrieving");
        return userList;
    }

    public List<Note> getAllNote(String userId, String taskCode,String day) {
        List<Note> userList = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE uniqueId = ? OR stdId=? OR tId=? ",new String[]{uniqueId,uniqueId,uniqueId});

        String selection = "day = ? OR day = ? AND user_id = ? AND task_code = ? OR task_id = ?";

        String[] selectionArgs = new String[]{day, "Everyday", userId, taskCode, taskCode};
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());

//        Cursor cursor = database.query(TABLE_SCHEDULE, null, selection, selectionArgs, null, null, null);

//        Cursor cursor = database.query(TABLE_SCHEDULE, null, selection, selectionArgs, null, null, "start_time ASC");
//        Cursor cursor = database.query(TABLE_SCHEDULE, null, selection, selectionArgs, null, null, "start_time ASC","CASE WHEN start_time >= ? THEN 1 ELSE 2 END, start_time ASC", new String[]{currentTime});

//        Cursor cursor = database.query(
//                "schedule",  // Replace with your table name
//                null,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                "event_time ASC",  // Replace with your actual time column name
//                "CASE WHEN event_time >= ? THEN 1 ELSE 2 END, event_time ASC",
//                new String[]{currentTime}
//        );

        String sqlQuery = "SELECT * FROM "+TABLE_NOTE+" " +
                "WHERE (day = ? OR day= ?) AND user_id = ? AND (task_code = ? OR task_id = ?)" +
                "ORDER BY " +
                "CASE " +
                "WHEN time >= ? THEN 1 " +
                "ELSE 2 " +
                "END, " +
                "time ASC";

        Cursor cursor = database.rawQuery(sqlQuery, new String[]{day,"Everyday",userId,taskCode,taskCode, currentTime});

        if (cursor != null && cursor.moveToFirst()) {
            do {

                Note user = new Note();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setSId(cursor.getString(sIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
                }
                int currentLocationIdIndex = cursor.getColumnIndex(COLUMN_LOCATION);
                if (currentLocationIdIndex >= 0) {
                    user.setTask_location(cursor.getString(currentLocationIdIndex));
                }
                int dayIdIndex = cursor.getColumnIndex(COLUMN_DAY);
                if (dayIdIndex >= 0) {
                    user.setDay(cursor.getString(dayIdIndex));
                }

                int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
                if (syncSIndex >= 0) {
                    user.setSync_status(cursor.getInt(syncSIndex));
                }

                int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
                if (syncKdIndex >= 0) {
                    user.setSync_key(cursor.getString(syncKdIndex));
                }

                int stdIdIndex = cursor.getColumnIndex(COLUMN_STD_ID);
                if (stdIdIndex >= 0) {
                    user.setStdId(cursor.getString(stdIdIndex));
                }

                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                if (userIdIndex >= 0) {
                    user.setUser_id(cursor.getString(userIdIndex));
                }

                int subNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                if (subNameIndex >= 0) {
                    user.setTask_name(cursor.getString(subNameIndex));
                }
                int subCodeIndex = cursor.getColumnIndex(COLUMN_TASK_CODE);
                if (subCodeIndex >= 0) {
                    user.setTask_code(cursor.getString(subCodeIndex));
                }
                int roomIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
                if (roomIndex >= 0) {
                    user.setTask_id(cursor.getString(roomIndex));
                }
                int campusIndex = cursor.getColumnIndex(COLUMN_TASK_DETAILS);
                if (campusIndex >= 0) {
                    user.setTask_details(cursor.getString(campusIndex));
                }

                int tCodeIndex = cursor.getColumnIndex(COLUMN_SCHEDULE_ID);
                if (tCodeIndex >= 0) {
                    user.setScheduleId(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_DATETIME);
                if (tNumIndex >= 0) {
                    user.setDateTime(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_TIME);
                if (tIdIndex >= 0) {
                    user.setTime(cursor.getString(tIdIndex));
                }

                int teacherIdIndex = cursor.getColumnIndex(COLUMN_CALENDAR);
                if (teacherIdIndex >= 0) {
                    user.setCalendar(cursor.getString(teacherIdIndex));
                }

                int teacherNameIndex = cursor.getColumnIndex(COLUMN_URL);
                if (teacherNameIndex >= 0) {
                    user.setUrl(cursor.getString(teacherNameIndex));
                }


                int minIndex = cursor.getColumnIndex(COLUMN_DONE);
                if (minIndex >= 0) {
                    user.setDone(cursor.getInt(minIndex));
                }

                int sTimeIndex = cursor.getColumnIndex(COLUMN_LINK);
                if (sTimeIndex >= 0) {
                    user.setLink(cursor.getString(sTimeIndex));
                }

                int eTimeIndex = cursor.getColumnIndex(COLUMN_UID);
                if (eTimeIndex >= 0) {
                    user.setuId(cursor.getString(eTimeIndex));
                }

                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.d("eee","retrieving");
        return userList;
    }

    public long updateNote(Note user) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_SID, user.getSId());
        values.put(COLUMN_STD_ID, user.getStdId());
        values.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        values.put(COLUMN_DAY, user.getDay());
        values.put(COLUMN_TASK_NAME, user.getTask_name());
        values.put(COLUMN_LOCATION, user.getTask_location());
        values.put(COLUMN_DONE, user.getDone());
        values.put(COLUMN_TASK_DETAILS, user.getTask_details());
        values.put(COLUMN_TASK_CODE, user.getTask_code());
        values.put(COLUMN_CALENDAR, user.getCalendar());
        values.put(COLUMN_DATETIME, user.getDateTime());
        values.put(COLUMN_LINK, user.getLink());
        values.put(COLUMN_URL, user.getUrl());
        values.put(COLUMN_TIME, user.getTime());
        values.put(COLUMN_USER_ID, user.getUser_id());
        values.put(COLUMN_TASK_ID, user.getTask_id());
        values.put(COLUMN_UID, user.getuId());
        values.put(COLUMN_SYNC_KEY, user.getSync_key());
        values.put(COLUMN_SYNC_STATUS, user.getSync_status());
        values.put(COLUMN_SCHEDULE_ID, user.getScheduleId());


        String whereClause = COLUMN_UNIQUE_ID + " = ?";
        String[] whereArgs = {user.getUniqueId()};

        return database.update(TABLE_NOTE, values, whereClause, whereArgs);
    }

    public int deleteNote(String userId) {
        String whereClause = COLUMN_UNIQUE_ID + " = ?";
        String[] whereArgs = {userId};

        return database.delete(TABLE_NOTE, whereClause, whereArgs);
    }


    public void saveNoteInformation(String uniqueId, Note user) {
        // Save additional user information in Firestore or Realtime Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("note");

        Map<String, Object> values = new HashMap<>();
        values.put(COLUMN_SID, user.getSId());
        values.put(COLUMN_ID, new Unique().unique_id());
        values.put(COLUMN_STD_ID, user.getStdId());
        values.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        values.put(COLUMN_DAY, user.getDay());
        values.put(COLUMN_TASK_NAME, user.getTask_name());
        values.put(COLUMN_LOCATION, user.getTask_location());
        values.put(COLUMN_DONE, user.getDone());
        values.put(COLUMN_TASK_DETAILS, user.getTask_details());
        values.put(COLUMN_TASK_CODE, user.getTask_code());
        values.put(COLUMN_CALENDAR, user.getCalendar());
        values.put(COLUMN_DATETIME, user.getDateTime());
        values.put(COLUMN_LINK, user.getLink());
        values.put(COLUMN_URL, user.getUrl());
        values.put(COLUMN_TIME, user.getTime());
        values.put(COLUMN_USER_ID, user.getUser_id());
        values.put(COLUMN_TASK_ID, user.getTask_id());
        values.put(COLUMN_UID, user.getuId());
        values.put(COLUMN_SYNC_KEY, user.getSync_key());
        values.put(COLUMN_SYNC_STATUS, user.getSync_status());
        values.put(COLUMN_SCHEDULE_ID, user.getScheduleId());


        usersCollection.document(uniqueId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
//                    Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
//                    finish(); // Finish sign-up activity and return to login screen
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
//                    Toast.makeText(this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void saveNoteInformationWithRealtime(String uniqueId,String studentId, String taskName,String taskCode, String taskId) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("note");

        Note user = new Note();
        user.setTask_name(taskName);
        user.setTask_code(taskCode);
        user.setTask_id(taskId);
        user.setUniqueId(uniqueId);
        user.setStdId(studentId);

        usersRef.child(uniqueId)
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
//                    Toast.makeText(get, "User created successfully", Toast.LENGTH_SHORT).show();
//                    finish(); // Finish sign-up activity and return to login screen
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
//                    Toast.makeText(this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void saveNoteInformationWithRealtime(Note scheduleItem) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("note");

        String key = usersRef.push().getKey();

        Note user = new Note();
        user.setId(new Unique().uniqueId());
        user.setUniqueId(scheduleItem.getUniqueId());


        usersRef.child(scheduleItem.getUniqueId())
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
//                    Toast.makeText(get, "User created successfully", Toast.LENGTH_SHORT).show();
//                    finish(); // Finish sign-up activity and return to login screen
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
//                    Toast.makeText(this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void saveNoteInformationWithRealtime(String uniqueId, Note u) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("note");

        String key = usersRef.push().getKey();


        Note user = new Note();
        user = u;
        user.setId(new Unique().uniqueId());
        user.setUniqueId(key);
        user.setSync_key(uniqueId);
        user.setSync_status(1);

        usersRef.child(uniqueId)
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
//                    Toast.makeText(get, "User created successfully", Toast.LENGTH_SHORT).show();
//                    finish(); // Finish sign-up activity and return to login screen
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
//                    Toast.makeText(this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public List<Note> getUnsyncedNoteFromSQLite() {
        List<Note> unsyncedUsers = new ArrayList<>();

        String[] projection = null;
        String selection = UserContract.UserEntry.COLUMN_SYNC_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(UserContract.UserEntry.SYNC_STATUS_FAILED)};

        Cursor cursor = database.query(
                TABLE_NOTE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Note user = new Note();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setSId(cursor.getString(sIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
                }
                int currentLocationIdIndex = cursor.getColumnIndex(COLUMN_LOCATION);
                if (currentLocationIdIndex >= 0) {
                    user.setTask_location(cursor.getString(currentLocationIdIndex));
                }
                int dayIdIndex = cursor.getColumnIndex(COLUMN_DAY);
                if (dayIdIndex >= 0) {
                    user.setDay(cursor.getString(dayIdIndex));
                }

                int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
                if (syncSIndex >= 0) {
                    user.setSync_status(cursor.getInt(syncSIndex));
                }

                int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
                if (syncKdIndex >= 0) {
                    user.setSync_key(cursor.getString(syncKdIndex));
                }

                int stdIdIndex = cursor.getColumnIndex(COLUMN_STD_ID);
                if (stdIdIndex >= 0) {
                    user.setStdId(cursor.getString(stdIdIndex));
                }

                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                if (userIdIndex >= 0) {
                    user.setUser_id(cursor.getString(userIdIndex));
                }

                int subNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                if (subNameIndex >= 0) {
                    user.setTask_name(cursor.getString(subNameIndex));
                }
                int subCodeIndex = cursor.getColumnIndex(COLUMN_TASK_CODE);
                if (subCodeIndex >= 0) {
                    user.setTask_code(cursor.getString(subCodeIndex));
                }
                int roomIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
                if (roomIndex >= 0) {
                    user.setTask_id(cursor.getString(roomIndex));
                }
                int campusIndex = cursor.getColumnIndex(COLUMN_TASK_DETAILS);
                if (campusIndex >= 0) {
                    user.setTask_details(cursor.getString(campusIndex));
                }

                int tCodeIndex = cursor.getColumnIndex(COLUMN_SCHEDULE_ID);
                if (tCodeIndex >= 0) {
                    user.setScheduleId(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_DATETIME);
                if (tNumIndex >= 0) {
                    user.setDateTime(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_TIME);
                if (tIdIndex >= 0) {
                    user.setTime(cursor.getString(tIdIndex));
                }

                int teacherIdIndex = cursor.getColumnIndex(COLUMN_CALENDAR);
                if (teacherIdIndex >= 0) {
                    user.setCalendar(cursor.getString(teacherIdIndex));
                }

                int teacherNameIndex = cursor.getColumnIndex(COLUMN_URL);
                if (teacherNameIndex >= 0) {
                    user.setUrl(cursor.getString(teacherNameIndex));
                }


                int minIndex = cursor.getColumnIndex(COLUMN_DONE);
                if (minIndex >= 0) {
                    user.setDone(cursor.getInt(minIndex));
                }

                int sTimeIndex = cursor.getColumnIndex(COLUMN_LINK);
                if (sTimeIndex >= 0) {
                    user.setLink(cursor.getString(sTimeIndex));
                }

                int eTimeIndex = cursor.getColumnIndex(COLUMN_UID);
                if (eTimeIndex >= 0) {
                    user.setuId(cursor.getString(eTimeIndex));
                }


                unsyncedUsers.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        cursor.close();

        return unsyncedUsers;
    }


    public void checkAndSyncData() {
        List<Note> unsyncedUsers = getUnsyncedNoteFromSQLite();
        for (Note user : unsyncedUsers) {
            // Add or update user data in Firebase
            addOrUpdateNoteInFirebase(user);
        }
    }

    public void addOrUpdateNoteInFirebase(Note user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("note").child(String.valueOf(user.getId()));
        usersRef.setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // Update sync status in SQLite upon successful synchronization
                    updateSyncStatusInSQLite(user.getId(), UserContract.UserEntry.SYNC_STATUS_SUCCESS);
                })
                .addOnFailureListener(e -> {
                    // Failed to add or update user in Firebase
//                    Toast.makeText(this, "Failed to sync user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void updateSyncStatusInSQLite(int userId, int syncStatus) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SYNC_STATUS, syncStatus);

        String selection =  "id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        database.update(TABLE_NOTE, values, selection, selectionArgs);
    }


    public void updateNoteData(Note user, School school) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("note");

        user.setSId(school.getsId());
        String uniqueId = user.getUniqueId();

        if (uniqueId != null) {
            usersRef.child(uniqueId).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Handle successful update
                            long r = updateNote(user);
                            if (r!=-1){
                                Log.e("UserFound","User sId Successfully Update In Local & Online");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failed update
                            Log.e("UserFound","User sId not Updated!");
                        }
                    });
        }else {
            Log.e("UserFound","User Id not found");
        }
    }

    public Note getNoteData(String userPhone) {
        Query query = usersRef.orderByChild("task_code").equalTo(userPhone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    ClassScheduleItem user = userSnapshot.getValue(ClassScheduleItem.class);
                    if (user != null) {
                        // User found based on the phone number
                        // Pass the user object to the callback
                        scheduleCallback.onUserRetrieved(user);
                        return;
                    }
                }
                // User not found based on the phone number
                scheduleCallback.onUserNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });

        // Return a placeholder user object or null if needed
        return new Note();
    }

//    usersRef = FirebaseDatabase.getInstance().getReference("users");
//
//    // Initialize the userCallback
//    userCallback = new UserCallback() {
//        @Override
//        public void onUserRetrieved(User user) {
//            // Handle the retrieved user object here
//            // For example, update the UI or pass it to another method
//            processUser(user);
//        }
//
//        @Override
//        public void onUserNotFound() {
//            // Handle the case where no user matches the given phone number
//            // For example, display an error message or take appropriate action
//            handleUserNotFound();
//        }
//    };

}