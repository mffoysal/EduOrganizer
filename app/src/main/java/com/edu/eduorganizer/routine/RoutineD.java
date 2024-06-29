package com.edu.eduorganizer.routine;

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
import com.edu.eduorganizer.schedule.ScheduleCallback;
import com.edu.eduorganizer.schedule.ScheduleItem;
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

public class RoutineD implements UserCallback {
    private static final String TABLE_SCHEDULE = "routine";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SID = "sId";
    private static final String COLUMN_UID = "uId";
    private static final String COLUMN_UNIQUE_ID = "uniqueId";

    private static final String COLUMN_STD_ID = "stdId";
    private static final String COLUMN_T_ID = "tId";
    private static final String COLUMN_TEMP_NAME = "temp_name";

    private static final String COLUMN_TEMP_CODE = "temp_code";
    private static final String COLUMN_TEMP_NUM = "temp_num";

    private static final String COLUMN_TEMP_DETAILS = "temp_details";

    private static final String COLUMN_STATUS = "aStatus";

    private static final String COLUMN_SYNC_STATUS = "sync_status";
    private static final String COLUMN_SYNC_KEY = "sync_key";

    private DatabaseManager databaseManager;

    private FirebaseAuth mAuth;
    private Internet internet;
    private String sId;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = firebaseDatabase.getReference("routine");

    private SQLiteDatabase database;

    private static final String SELECT_ALL = "SELECT *  FROM "+TABLE_SCHEDULE;
    private RoutineCallback scheduleCallback;

    public RoutineD(SQLiteDatabase database) {
        this.database = database;
    }

    public RoutineD(Context context) {
        databaseManager = new DatabaseManager(context);
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();
        this.database = database;
    }

    public void syncData(){}
    public long insertRoutine(Routine user) {
        long l=-2;
        int r=0;
        r = checkRoutine(user.getTemp_name(),user.getTemp_code(),user.getTemp_num());
        if(r!=0){
            return -3;
        }else {
            Log.e("routine","Routine Insertion!");
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_SID, user.getSId());
        values.put(COLUMN_UID, user.getuId());
//        values.put(COLUMN_ID, new Admin().uniqueId());
        values.put(COLUMN_STD_ID, user.getStdId());
        values.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        values.put(COLUMN_TEMP_NAME, user.getTemp_name());
        values.put(COLUMN_TEMP_DETAILS, user.getTemp_details());
        values.put(COLUMN_TEMP_CODE, user.getTemp_code());
        values.put(COLUMN_TEMP_NUM, user.getTemp_num());
        values.put(COLUMN_T_ID, user.getT_id());
        values.put(COLUMN_SYNC_KEY, user.getSync_key());
        values.put(COLUMN_SYNC_STATUS, user.getSync_status());
        values.put(COLUMN_STATUS, user.getaStatus());


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
    public int checkRoutine(String tempName,String temp_code, String temp_num){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE temp_name = ? AND temp_code = ? AND temp_num = ? ",new String[]{tempName,temp_code,temp_num});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }

    public int checkRoutine(String temp_code){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE temp_code = ? OR temp_num = ? ",new String[]{temp_code,temp_code});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }

    public int checkRoutine(String tempName,String temp_code, String temp_num, String stdId){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE temp_name = ? AND temp_code=? AND temp_num=? AND stdId=? ",new String[]{tempName,temp_code,temp_num,stdId});

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

    public Routine RoutineData(String tempCode){
        Routine user = new Routine();
        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE temp_code = ? OR temp_num = ? OR uniqueId =? ",new String[]{tempCode,tempCode,tempCode});

        if (cursor != null && cursor.moveToFirst()){


            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            if (idIndex >= 0) {
                user.setId(cursor.getInt(idIndex));
            }

            int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
            if (sIdIndex >= 0) {
                user.setSId(cursor.getString(sIdIndex));
            }

            int uIdIndex = cursor.getColumnIndex(COLUMN_UID);
            if (uIdIndex >= 0) {
                user.setuId(cursor.getString(uIdIndex));
            }

            int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
            if (uniqueIdIndex >= 0) {
                user.setUniqueId(cursor.getString(uniqueIdIndex));
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

            int tempNameIndex = cursor.getColumnIndex(COLUMN_TEMP_NAME);
            if (tempNameIndex >= 0) {
                user.setTemp_name(cursor.getString(tempNameIndex));
            }
            int tempCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_CODE);
            if (tempCodeIndex >= 0) {
                user.setTemp_code(cursor.getString(tempCodeIndex));
            }


            int tCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_DETAILS);
            if (tCodeIndex >= 0) {
                user.setTemp_details(cursor.getString(tCodeIndex));
            }

            int tNumIndex = cursor.getColumnIndex(COLUMN_TEMP_NUM);
            if (tNumIndex >= 0) {
                user.setTemp_num(cursor.getString(tNumIndex));
            }

            int tIdIndex = cursor.getColumnIndex(COLUMN_T_ID);
            if (tIdIndex >= 0) {
                user.setT_id(cursor.getString(tIdIndex));
            }

        }

        return user;
    }

    public Routine getRoutine(String uniqueId){
        Routine user = new Routine();
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

            int uIdIndex = cursor.getColumnIndex(COLUMN_UID);
            if (uIdIndex >= 0) {
                user.setuId(cursor.getString(uIdIndex));
            }

            int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
            if (uniqueIdIndex >= 0) {
                user.setUniqueId(cursor.getString(uniqueIdIndex));
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

            int tempNameIndex = cursor.getColumnIndex(COLUMN_TEMP_NAME);
            if (tempNameIndex >= 0) {
                user.setTemp_name(cursor.getString(tempNameIndex));
            }
            int tempCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_CODE);
            if (tempCodeIndex >= 0) {
                user.setTemp_code(cursor.getString(tempCodeIndex));
            }


            int tCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_DETAILS);
            if (tCodeIndex >= 0) {
                user.setTemp_details(cursor.getString(tCodeIndex));
            }

            int tNumIndex = cursor.getColumnIndex(COLUMN_TEMP_NUM);
            if (tNumIndex >= 0) {
                user.setTemp_num(cursor.getString(tNumIndex));
            }

            int tIdIndex = cursor.getColumnIndex(COLUMN_T_ID);
            if (tIdIndex >= 0) {
                user.setT_id(cursor.getString(tIdIndex));
            }

        }

        return user;
    }

    public List<Routine> getAllRoutine() {
        List<Routine> userList = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SCHEDULE, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                Routine user = new Routine();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setSId(cursor.getString(sIdIndex));
                }

                int uIdIndex = cursor.getColumnIndex(COLUMN_UID);
                if (uIdIndex >= 0) {
                    user.setuId(cursor.getString(uIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
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

                int tempNameIndex = cursor.getColumnIndex(COLUMN_TEMP_NAME);
                if (tempNameIndex >= 0) {
                    user.setTemp_name(cursor.getString(tempNameIndex));
                }
                int tempCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_CODE);
                if (tempCodeIndex >= 0) {
                    user.setTemp_code(cursor.getString(tempCodeIndex));
                }


                int tCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_DETAILS);
                if (tCodeIndex >= 0) {
                    user.setTemp_details(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_TEMP_NUM);
                if (tNumIndex >= 0) {
                    user.setTemp_num(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_T_ID);
                if (tIdIndex >= 0) {
                    user.setT_id(cursor.getString(tIdIndex));
                }


                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return userList;
    }

    public List<Routine> getAllRoutine(String userId) {
        List<Routine> userList = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_SCHEDULE + " WHERE uniqueId = ? OR stdId=? OR tId=? ",new String[]{uniqueId,uniqueId,uniqueId});

        String selection = "uId = ? OR tId = ?";

        String[] selectionArgs = new String[]{userId};
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
                "WHERE uId = ? OR tId = ? " +
                "ORDER BY Id DESC";

        Cursor cursor = database.rawQuery(sqlQuery, new String[]{userId,userId});

        if (cursor != null && cursor.moveToFirst()) {
            do {

                Routine user = new Routine();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setSId(cursor.getString(sIdIndex));
                }

                int uIdIndex = cursor.getColumnIndex(COLUMN_UID);
                if (uIdIndex >= 0) {
                    user.setuId(cursor.getString(uIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
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

                int tempNameIndex = cursor.getColumnIndex(COLUMN_TEMP_NAME);
                if (tempNameIndex >= 0) {
                    user.setTemp_name(cursor.getString(tempNameIndex));
                }
                int tempCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_CODE);
                if (tempCodeIndex >= 0) {
                    user.setTemp_code(cursor.getString(tempCodeIndex));
                }


                int tCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_DETAILS);
                if (tCodeIndex >= 0) {
                    user.setTemp_details(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_TEMP_NUM);
                if (tNumIndex >= 0) {
                    user.setTemp_num(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_T_ID);
                if (tIdIndex >= 0) {
                    user.setT_id(cursor.getString(tIdIndex));
                }


                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.d("eee","retrieving");
        return userList;
    }

    public long updateRoutine(Routine u) {
        ContentValues users = new ContentValues();

        users.put("sync_key", u.getUniqueId());
        users.put("sync_status", u.getSync_status());
        users.put("uniqueId", u.getUniqueId());
        users.put("temp_name", u.getTemp_name());
        users.put("temp_details", u.getTemp_details());
        users.put("temp_code", u.getTemp_code());
        users.put("temp_num",u.getTemp_num());
        users.put("sId",u.getSId());
        users.put("stdId",u.getStdId());

        String whereClause = COLUMN_UNIQUE_ID + " = ?";
        String[] whereArgs = {u.getUniqueId()};

        return database.update(TABLE_SCHEDULE, users, whereClause, whereArgs);
    }

    public int deleteRoutine(String userId) {
        String whereClause = COLUMN_UNIQUE_ID + " = ?";
        String[] whereArgs = {userId};

        return database.delete(TABLE_SCHEDULE, whereClause, whereArgs);
    }


    public void saveRoutineInformation(String uniqueId, Routine u) {
        // Save additional user information in Firestore or Realtime Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("routine");

        Map<String, Object> user = new HashMap<>();
        user.put("sync_key", uniqueId);
        user.put("id", new Unique().uniqueId());
        user.put("sync_status", u.getSync_status());
        user.put("uniqueId", u.getUniqueId());
        user.put("temp_name", u.getTemp_name());
        user.put("temp_details", u.getTemp_details());
        user.put("temp_code",u.getTemp_code());
        user.put("temp_num",u.getTemp_num());
        user.put("sId",u.getSId());
        user.put("uId",u.getuId());
        user.put("stdId",u.getStdId());
        user.put("tId",u.getT_id());


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

    public void saveRoutineInformationWithRealtime(String uniqueId,String studentId, String subName,String subCode, String section) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("routine");

        Routine user = new Routine();
        user.setTemp_name(subName);
        user.setTemp_code(subCode);
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

    public void saveRoutineInformationWithRealtime(Routine routine) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("users");

        String key = usersRef.push().getKey();

        ScheduleItem user = new ScheduleItem();
        user.setId(new Unique().uniqueId());
        user.setUniqueId(routine.getUniqueId());


        usersRef.child(routine.getUniqueId())
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

    public void saveRoutineInformationWithRealtime(String uniqueId, Routine u) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("users");

        String key = usersRef.push().getKey();


        Routine user = new Routine();
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

    public List<Routine> getUnsyncedRoutineFromSQLite() {
        List<Routine> unsyncedUsers = new ArrayList<>();

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
                Routine user = new Routine();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setSId(cursor.getString(sIdIndex));
                }

                int uIdIndex = cursor.getColumnIndex(COLUMN_UID);
                if (uIdIndex >= 0) {
                    user.setuId(cursor.getString(uIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
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

                int tempNameIndex = cursor.getColumnIndex(COLUMN_TEMP_NAME);
                if (tempNameIndex >= 0) {
                    user.setTemp_name(cursor.getString(tempNameIndex));
                }
                int tempCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_CODE);
                if (tempCodeIndex >= 0) {
                    user.setTemp_code(cursor.getString(tempCodeIndex));
                }


                int tCodeIndex = cursor.getColumnIndex(COLUMN_TEMP_DETAILS);
                if (tCodeIndex >= 0) {
                    user.setTemp_details(cursor.getString(tCodeIndex));
                }

                int tNumIndex = cursor.getColumnIndex(COLUMN_TEMP_NUM);
                if (tNumIndex >= 0) {
                    user.setTemp_num(cursor.getString(tNumIndex));
                }

                int tIdIndex = cursor.getColumnIndex(COLUMN_T_ID);
                if (tIdIndex >= 0) {
                    user.setT_id(cursor.getString(tIdIndex));
                }


                unsyncedUsers.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        cursor.close();

        return unsyncedUsers;
    }


    public void checkAndSyncData() {
        List<Routine> unsyncedUsers = getUnsyncedRoutineFromSQLite();
        for (Routine user : unsyncedUsers) {
            // Add or update user data in Firebase
            addOrUpdateRoutineInFirebase(user);
        }
    }

    public void addOrUpdateRoutineInFirebase(Routine user) {
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
        values.put(RoutineCo.RoutineEntry.COLUMN_SYNC_STATUS, syncStatus);

        String selection =  "id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        database.update(RoutineCo.RoutineEntry.TABLE_NAME, values, selection, selectionArgs);
    }


    public void updateRoutineData(Routine user, School school) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("schedule");

        user.setSId(school.getsId());
        String uniqueId = user.getUniqueId();

        if (uniqueId != null) {
            usersRef.child(uniqueId).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Handle successful update
                            long r = updateRoutine(user);
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
            Log.e("RoutineFound","Routine Id not found");
        }
    }

    public Routine getRoutineData(String uniqueId) {
        Query query = usersRef.orderByChild("uniqueId").equalTo(uniqueId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Routine routine = userSnapshot.getValue(Routine.class);
                    if (routine != null) {
                        // User found based on the phone number
                        // Pass the user object to the callback
                        scheduleCallback.onUserRetrieved(routine);
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
        return new Routine();
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