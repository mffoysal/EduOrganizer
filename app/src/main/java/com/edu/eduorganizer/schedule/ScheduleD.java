package com.edu.eduorganizer.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.edu.eduorganizer.User;
import com.edu.eduorganizer.bubble.ScheduleCo;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
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

public class ScheduleD implements UserCallback {
    private static final String TABLE_SCHEDULE = "schedule";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SID = "sId";
    private static final String COLUMN_UNIQUE_ID = "uniqueId";
    private static final String COLUMN_CURRENT_SESSION = "currSessId";

    private static final String COLUMN_STD_ID = "stdId";
    private static final String COLUMN_SUB_NAME = "sub_name";

    private static final String COLUMN_SUB_CODE = "sub_code";
    private static final String COLUMN_ROOM = "room";
    private static final String COLUMN_TID = "tId";
    private static final String COLUMN_CAMPUS = "campus";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_STATUS = "aStatus";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_T_NAME = "t_name";
    private static final String COLUMN_T_ID = "t_id";
    private static final String COLUMN_TEMP_NUM = "temp_num";
    private static final String COLUMN_TEMP_CODE = "temp_code";
    private static final String COLUMN_SYNC_STATUS = "sync_status";
    private static final String COLUMN_SYNC_KEY = "sync_key";
    private static final String COLUMN_END_TIME = "end_time";
    private static final String COLUMN_MIN = "min";
    private static final String COLUMN_SECTION = "section";
    private DatabaseManager databaseManager;

    private FirebaseAuth mAuth;
    private Internet internet;
    private String sId;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = firebaseDatabase.getReference("users");

    private SQLiteDatabase database;

    private static final String SELECT_ALL = "SELECT *  FROM "+TABLE_SCHEDULE;
    private ScheduleCallback scheduleCallback;

    public ScheduleD(SQLiteDatabase database) {
        this.database = database;
    }

    public ScheduleD(Context context) {
        databaseManager = new DatabaseManager(context);
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();
        this.database = database;
    }

    public void syncData(){}
    public long insertSchedule(ScheduleItem user) {
        long l=-2;
        int r=0;
        r = checkScheduleId(user.getTId(),user.getTemp_code(),user.getTemp_num(),user.getUniqueId());
        if(r!=0){
            return -3;
        }else {
            Log.e("schedule","Schedule Insertion!");
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_SID, user.getSId());
//        values.put(COLUMN_ID, new Admin().uniqueId());
        values.put(COLUMN_STD_ID, user.getStdId());
        values.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        values.put(COLUMN_DAY, user.getDay());
        values.put(COLUMN_SUB_NAME, user.getSub_name());
        values.put(COLUMN_SUB_CODE, user.getSub_code());
        values.put(COLUMN_MIN, user.getMin());
        values.put(COLUMN_START_TIME, user.getStart_time());
        values.put(COLUMN_END_TIME, user.getEnd_time());
        values.put(COLUMN_SECTION, user.getSection());
        values.put(COLUMN_TEMP_CODE, user.getTemp_code());
        values.put(COLUMN_TEMP_NUM, user.getTemp_num());
        values.put(COLUMN_T_NAME, user.getT_name());
        values.put(COLUMN_T_ID, user.getT_id());
        values.put(COLUMN_TID, user.getTId());
        values.put(COLUMN_ROOM, user.getRoom());
        values.put(COLUMN_SYNC_KEY, user.getSync_key());
        values.put(COLUMN_SYNC_STATUS, user.getSync_status());
        values.put(COLUMN_CAMPUS, user.getCampus());

        try {
            l = database.insert(TABLE_SCHEDULE, null, values);
        }catch (SQLiteException e){
            String ee = e.getMessage();
            Log.e("error",ee);
        }

        return l;
    }

    public Cursor getScheduleCursor(){
        ScheduleItem user = new ScheduleItem();

        Cursor cursor = database.rawQuery(SELECT_ALL,null);


        return  cursor;
    }
    public int checkSchedule(String subName,String sub_code, String section, String day){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE sub_name = ? AND sub_code=? AND section=? AND day=? ",new String[]{subName,sub_code,section,day});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }
    public int checkScheduleId(String userId,String tempCode, String tempNum, String uniqueId){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE tId = ? AND temp_code=? AND temp_num=? AND uniqueId=? ",new String[]{userId,tempCode,tempNum,uniqueId});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }
    public int checkSchedule(String subName,String sub_code, String section, String day, String stdId){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE sub_name = ? AND sub_code=? AND section=? AND day=? AND stdId=? ",new String[]{subName,sub_code,section,day,stdId});

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

    public ScheduleItem getSchedule(String uniqueId){
        ScheduleItem user = new ScheduleItem();

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE uniqueId = ? OR stdId=? OR tId=? ",new String[]{uniqueId,uniqueId,uniqueId});

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
            int currentSectionIdIndex = cursor.getColumnIndex(COLUMN_SECTION);
            if (currentSectionIdIndex >= 0) {
                user.setSection(cursor.getString(currentSectionIdIndex));
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

            int subNameIndex = cursor.getColumnIndex(COLUMN_SUB_NAME);
            if (subNameIndex >= 0) {
                user.setSub_name(cursor.getString(subNameIndex));
            }
            int subCodeIndex = cursor.getColumnIndex(COLUMN_SUB_CODE);
            if (subCodeIndex >= 0) {
                user.setSub_code(cursor.getString(subCodeIndex));
            }
            int roomIndex = cursor.getColumnIndex(COLUMN_ROOM);
            if (roomIndex >= 0) {
                user.setRoom(cursor.getString(roomIndex));
            }
            int campusIndex = cursor.getColumnIndex(COLUMN_CAMPUS);
            if (campusIndex >= 0) {
                user.setCampus(cursor.getString(campusIndex));
            }

            int tCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_CODE);
            if (tCodeIndex >= 0) {
                user.setTemp_code(cursor.getString(tCodeIndex));
            }

            int tNumIndex = cursor.getColumnIndex(COLUMN_TEMP_NUM);
            if (tNumIndex >= 0) {
                user.setTemp_num(cursor.getString(tNumIndex));
            }

            int tIdIndex = cursor.getColumnIndex(COLUMN_TID);
            if (tIdIndex >= 0) {
                user.setTId(cursor.getString(tIdIndex));
            }

            int teacherIdIndex = cursor.getColumnIndex(COLUMN_T_ID);
            if (teacherIdIndex >= 0) {
                user.setT_id(cursor.getString(teacherIdIndex));
            }

            int teacherNameIndex = cursor.getColumnIndex(COLUMN_T_NAME);
            if (teacherNameIndex >= 0) {
                user.setT_name(cursor.getString(teacherNameIndex));
            }


            int minIndex = cursor.getColumnIndex(COLUMN_MIN);
            if (minIndex >= 0) {
                user.setMin(cursor.getInt(minIndex));
            }

            int sTimeIndex = cursor.getColumnIndex(COLUMN_START_TIME);
            if (sTimeIndex >= 0) {
                user.setStart_time(cursor.getString(sTimeIndex));
            }

            int eTimeIndex = cursor.getColumnIndex(COLUMN_END_TIME);
            if (eTimeIndex >= 0) {
                user.setEnd_time(cursor.getString(eTimeIndex));
            }
        }

        return user;
    }

    public List<ScheduleItem> getAllSchedule() {
        List<ScheduleItem> userList = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SCHEDULE, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                ScheduleItem user = new ScheduleItem();
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
                int currentSectionIdIndex = cursor.getColumnIndex(COLUMN_SECTION);
                if (currentSectionIdIndex >= 0) {
                    user.setSection(cursor.getString(currentSectionIdIndex));
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

                int subNameIndex = cursor.getColumnIndex(COLUMN_SUB_NAME);
                if (subNameIndex >= 0) {
                    user.setSub_name(cursor.getString(subNameIndex));
                }
                int subCodeIndex = cursor.getColumnIndex(COLUMN_SUB_CODE);
                if (subCodeIndex >= 0) {
                    user.setSub_code(cursor.getString(subCodeIndex));
                }
                int roomIndex = cursor.getColumnIndex(COLUMN_ROOM);
                if (roomIndex >= 0) {
                    user.setRoom(cursor.getString(roomIndex));
                }
                int campusIndex = cursor.getColumnIndex(COLUMN_CAMPUS);
                if (campusIndex >= 0) {
                    user.setCampus(cursor.getString(campusIndex));
                }

                int tCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_CODE);
                if (tCodeIndex >= 0) {
                    user.setTemp_code(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_TEMP_NUM);
                if (tNumIndex >= 0) {
                    user.setTemp_num(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_TID);
                if (tIdIndex >= 0) {
                    user.setTId(cursor.getString(tIdIndex));
                }

                int teacherIdIndex = cursor.getColumnIndex(COLUMN_T_ID);
                if (teacherIdIndex >= 0) {
                    user.setT_id(cursor.getString(teacherIdIndex));
                }

                int teacherNameIndex = cursor.getColumnIndex(COLUMN_T_NAME);
                if (teacherNameIndex >= 0) {
                    user.setT_name(cursor.getString(teacherNameIndex));
                }


                int minIndex = cursor.getColumnIndex(COLUMN_MIN);
                if (minIndex >= 0) {
                    user.setMin(cursor.getInt(minIndex));
                }

                int sTimeIndex = cursor.getColumnIndex(COLUMN_START_TIME);
                if (sTimeIndex >= 0) {
                    user.setStart_time(cursor.getString(sTimeIndex));
                }

                int eTimeIndex = cursor.getColumnIndex(COLUMN_END_TIME);
                if (eTimeIndex >= 0) {
                    user.setEnd_time(cursor.getString(eTimeIndex));
                }

                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return userList;
    }

    public int deleteAllSchedule(String uniqueId) {
        String whereClause = COLUMN_TID + " = ? OR stdId = ?";
        String[] whereArgs = {uniqueId};

        return database.delete(TABLE_SCHEDULE, whereClause, whereArgs);
    }

    public int deleteAllScheduleFromLocal(String uniqueId) {
        String whereClause = COLUMN_TID + " = ? OR stdId = ?";
        String[] whereArgs = {uniqueId};

        return database.delete(TABLE_SCHEDULE, whereClause, whereArgs);
    }

    public List<ScheduleItem> getAllSchedule(String day) {
        List<ScheduleItem> userList = new ArrayList<>();
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

        String sqlQuery = "SELECT * FROM "+TABLE_SCHEDULE+" " +
                "WHERE day = ? OR day= ? " +
                "ORDER BY " +
                "CASE " +
                "WHEN start_time >= ? THEN 1 " +
                "ELSE 2 " +
                "END, " +
                "start_time ASC";

        Cursor cursor = database.rawQuery(sqlQuery, new String[]{day,"Everyday", currentTime});

        if (cursor != null && cursor.moveToFirst()) {
            do {

                ScheduleItem user = new ScheduleItem();
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
                int currentSectionIdIndex = cursor.getColumnIndex(COLUMN_SECTION);
                if (currentSectionIdIndex >= 0) {
                    user.setSection(cursor.getString(currentSectionIdIndex));
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

                int subNameIndex = cursor.getColumnIndex(COLUMN_SUB_NAME);
                if (subNameIndex >= 0) {
                    user.setSub_name(cursor.getString(subNameIndex));
                }
                int subCodeIndex = cursor.getColumnIndex(COLUMN_SUB_CODE);
                if (subCodeIndex >= 0) {
                    user.setSub_code(cursor.getString(subCodeIndex));
                }
                int roomIndex = cursor.getColumnIndex(COLUMN_ROOM);
                if (roomIndex >= 0) {
                    user.setRoom(cursor.getString(roomIndex));
                }
                int campusIndex = cursor.getColumnIndex(COLUMN_CAMPUS);
                if (campusIndex >= 0) {
                    user.setCampus(cursor.getString(campusIndex));
                }

                int tCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_CODE);
                if (tCodeIndex >= 0) {
                    user.setTemp_code(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_TEMP_NUM);
                if (tNumIndex >= 0) {
                    user.setTemp_num(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_TID);
                if (tIdIndex >= 0) {
                    user.setTId(cursor.getString(tIdIndex));
                }

                int teacherIdIndex = cursor.getColumnIndex(COLUMN_T_ID);
                if (teacherIdIndex >= 0) {
                    user.setT_id(cursor.getString(teacherIdIndex));
                }

                int teacherNameIndex = cursor.getColumnIndex(COLUMN_T_NAME);
                if (teacherNameIndex >= 0) {
                    user.setT_name(cursor.getString(teacherNameIndex));
                }


                int minIndex = cursor.getColumnIndex(COLUMN_MIN);
                if (minIndex >= 0) {
                    user.setMin(cursor.getInt(minIndex));
                }

                int sTimeIndex = cursor.getColumnIndex(COLUMN_START_TIME);
                if (sTimeIndex >= 0) {
                    user.setStart_time(cursor.getString(sTimeIndex));
                }

                int eTimeIndex = cursor.getColumnIndex(COLUMN_END_TIME);
                if (eTimeIndex >= 0) {
                    user.setEnd_time(cursor.getString(eTimeIndex));
                }

                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.d("eee","retrieving");
        return userList;
    }

    public long updateSchedule(ScheduleItem u) {
        ContentValues users = new ContentValues();

        users.put("sync_key", u.getUniqueId());
        users.put("sync_status", u.getSync_status());
        users.put("day", u.getDay());
        users.put("room", u.getRoom());
        users.put("uniqueId", u.getUniqueId());
        users.put("campus", u.getCampus());
        users.put("section", u.getSection());
        users.put("sub_name", u.getSub_name());
        users.put("sub_code", u.getSub_code());
        users.put("temp_code",u.getTemp_code());
        users.put("sId",u.getSId());
        users.put("stdId",u.getStdId());
        users.put("tId",u.getTId());
        users.put("t_id",u.getT_id());
        users.put("t_name",u.getT_name());
        users.put("start_time",u.getStart_time());
        users.put("end_time",u.getEnd_time());
        users.put("min",u.getMin());

        String whereClause = COLUMN_UNIQUE_ID + " = ?";
        String[] whereArgs = {u.getUniqueId()};

        return database.update(TABLE_SCHEDULE, users, whereClause, whereArgs);
    }

    public int deleteSchedule(String userId) {
        String whereClause = COLUMN_UNIQUE_ID + " = ?";
        String[] whereArgs = {userId};

        return database.delete(TABLE_SCHEDULE, whereClause, whereArgs);
    }


    public void saveScheduleInformation(String uniqueId, ScheduleItem u) {
        // Save additional user information in Firestore or Realtime Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("schedule");

        Map<String, Object> user = new HashMap<>();
        user.put("sync_key", uniqueId);
        user.put("id", new Unique().uniqueId());
        user.put("sync_status", u.getSync_status());
        user.put("day", u.getDay());
        user.put("room", u.getRoom());
        user.put("uniqueId", u.getUniqueId());
        user.put("campus", u.getCampus());
        user.put("section", u.getSection());
        user.put("sub_name", u.getSub_name());
        user.put("sub_code", u.getSub_code());
        user.put("temp_code",u.getTemp_code());
        user.put("sId",u.getSId());
        user.put("stdId",u.getStdId());
        user.put("tId",u.getTId());
        user.put("t_id",u.getT_id());
        user.put("t_name",u.getT_name());
        user.put("start_time",u.getStart_time());
        user.put("end_time",u.getEnd_time());
        user.put("min",u.getMin());


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

    public void saveScheduleInformationWithRealtime(String uniqueId,String studentId, String subName,String subCode, String section) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("schedule");

        ScheduleItem user = new ScheduleItem();
        user.setSub_name(subName);
        user.setSub_code(subCode);
        user.setSection(section);
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

    public void saveScheduleInformationWithRealtime(ScheduleItem scheduleItem) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("users");

        String key = usersRef.push().getKey();

        ScheduleItem user = new ScheduleItem();
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

    public void saveScheduleInformationWithRealtime(String uniqueId, ScheduleItem u) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("users");

        String key = usersRef.push().getKey();


        ScheduleItem user = new ScheduleItem();
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

    public List<ScheduleItem> getUnsyncedSchedulesFromSQLite() {
        List<ScheduleItem> unsyncedUsers = new ArrayList<>();

        String[] projection = null;
        String selection = UserContract.UserEntry.COLUMN_SYNC_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(UserContract.UserEntry.SYNC_STATUS_FAILED)};

        Cursor cursor = database.query(
                TABLE_SCHEDULE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ScheduleItem user = new ScheduleItem();
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
                int currentSectionIdIndex = cursor.getColumnIndex(COLUMN_SECTION);
                if (currentSectionIdIndex >= 0) {
                    user.setSection(cursor.getString(currentSectionIdIndex));
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

                int subNameIndex = cursor.getColumnIndex(COLUMN_SUB_NAME);
                if (subNameIndex >= 0) {
                    user.setSub_name(cursor.getString(subNameIndex));
                }
                int subCodeIndex = cursor.getColumnIndex(COLUMN_SUB_CODE);
                if (subCodeIndex >= 0) {
                    user.setSub_code(cursor.getString(subCodeIndex));
                }
                int roomIndex = cursor.getColumnIndex(COLUMN_ROOM);
                if (roomIndex >= 0) {
                    user.setRoom(cursor.getString(roomIndex));
                }
                int campusIndex = cursor.getColumnIndex(COLUMN_CAMPUS);
                if (campusIndex >= 0) {
                    user.setCampus(cursor.getString(campusIndex));
                }

                int tCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_CODE);
                if (tCodeIndex >= 0) {
                    user.setTemp_code(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_TEMP_NUM);
                if (tNumIndex >= 0) {
                    user.setTemp_num(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_TID);
                if (tIdIndex >= 0) {
                    user.setTId(cursor.getString(tIdIndex));
                }

                int teacherIdIndex = cursor.getColumnIndex(COLUMN_T_ID);
                if (teacherIdIndex >= 0) {
                    user.setT_id(cursor.getString(teacherIdIndex));
                }

                int teacherNameIndex = cursor.getColumnIndex(COLUMN_T_NAME);
                if (teacherNameIndex >= 0) {
                    user.setT_name(cursor.getString(teacherNameIndex));
                }


                int minIndex = cursor.getColumnIndex(COLUMN_MIN);
                if (minIndex >= 0) {
                    user.setMin(cursor.getInt(minIndex));
                }

                int sTimeIndex = cursor.getColumnIndex(COLUMN_START_TIME);
                if (sTimeIndex >= 0) {
                    user.setStart_time(cursor.getString(sTimeIndex));
                }

                int eTimeIndex = cursor.getColumnIndex(COLUMN_END_TIME);
                if (eTimeIndex >= 0) {
                    user.setEnd_time(cursor.getString(eTimeIndex));
                }


                unsyncedUsers.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        cursor.close();

        return unsyncedUsers;
    }


    public void checkAndSyncData() {
        List<ScheduleItem> unsyncedUsers = getUnsyncedSchedulesFromSQLite();
        for (ScheduleItem user : unsyncedUsers) {
            // Add or update user data in Firebase
            addOrUpdateScheduleInFirebase(user);
        }
    }

    public void addOrUpdateScheduleInFirebase(ScheduleItem user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("schedule").child(String.valueOf(user.getId()));
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
        values.put(ScheduleCo.ScheduleEntry.COLUMN_SYNC_STATUS, syncStatus);

        String selection =  "id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        database.update(ScheduleCo.ScheduleEntry.TABLE_NAME, values, selection, selectionArgs);
    }


    public void updateScheduleData(ScheduleItem user, School school) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("schedule");

        user.setSId(school.getsId());
        String uniqueId = user.getUniqueId();

        if (uniqueId != null) {
            usersRef.child(uniqueId).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Handle successful update
                            long r = updateSchedule(user);
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

    public ScheduleItem getScheduleData(String userPhone) {
        Query query = usersRef.orderByChild("phone").equalTo(userPhone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    ScheduleItem user = userSnapshot.getValue(ScheduleItem.class);
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
        return new ScheduleItem();
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