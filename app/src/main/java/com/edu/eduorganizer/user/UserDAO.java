package com.edu.eduorganizer.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.edu.eduorganizer.User;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.unique.Unique;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO implements UserCallback{
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SID = "sId";
    private static final String COLUMN_UNIQUE_ID = "uniqueId";
    private static final String COLUMN_CURRENT_SESSION = "currSessId";
    private static final String COLUMN_DESIGNATION = "designation";
    private static final String COLUMN_STDID = "stdId";
    private static final String COLUMN_STDNAME = "name";

    private static final String COLUMN_U_TYPE = "u_type";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_MAJOR = "major";
    private static final String COLUMN_USERID = "u_id";
    private static final String COLUMN_PICTURE = "picture";
    private static final String COLUMN_PHOTO = "photo";
    private static final String COLUMN_SYNC_STATUS = "sync_status";
    private static final String COLUMN_SYNC_KEY = "sync_key";
    private static final String COLUMN_FINGERDATA = "fingerData";
    private DatabaseManager databaseManager;

    private FirebaseAuth mAuth;
    private Internet internet;
    private String sId;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = firebaseDatabase.getReference("users");

    private SQLiteDatabase database;

    private static final String SELECT_ALL = "SELECT *  FROM "+TABLE_USERS;
    private UserCallback userCallback;

    public UserDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public UserDAO(Context context) {
        databaseManager = new DatabaseManager(context);
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();
        this.database = database;
    }

    public void syncData(){}
    public long insertUser(User user) {
        long l=-2;
        int r=0;
        r = checkUser(user.getPhone());
        if(r!=0){
            return -3;
        }else {
            Log.e("UserFound","User Insertion!");
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_SID, user.getSId());
//        values.put(COLUMN_ID, new Admin().uniqueId());
        values.put(COLUMN_STDID, user.getStdId());
        values.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        values.put(COLUMN_CURRENT_SESSION, user.getCurrSessId());
        values.put(COLUMN_DESIGNATION, user.getDesignation());
        values.put(COLUMN_STDNAME, user.getStdName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_U_TYPE, user.getU_type());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_PASS, user.getPass());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_STATUS, user.getStatus());
        values.put(COLUMN_MAJOR, user.getMajor());
        values.put(COLUMN_USERID, user.getUserId());
        values.put(COLUMN_PICTURE, user.getPicture());
        values.put(COLUMN_PHOTO, user.getPhoto());
        values.put(COLUMN_SYNC_KEY, user.getSync_key());
        values.put(COLUMN_SYNC_STATUS, user.getSync_status());
        values.put(COLUMN_FINGERDATA, user.getFingerData());

        try {
            l = database.insert(TABLE_USERS, null, values);
        }catch (SQLiteException e){
            String ee = e.getMessage();
            Log.e("error",ee);
        }

        return l;
    }

    public Cursor getUserCursor(){
        User user = new User();

        Cursor cursor = database.rawQuery(SELECT_ALL,null);


        return  cursor;
    }
    public int checkUser(String userIdPhone){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_USERS + " WHERE stdId = ? OR phone=? OR email=? ",new String[]{userIdPhone,userIdPhone,userIdPhone});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }

    public int checkUser(String userIdPhone,String pass){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_USERS + " WHERE pass = ? AND (stdId = ? OR phone=? OR email=?)",new String[]{pass,userIdPhone,userIdPhone,userIdPhone});

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

    public User getUser(String userIdPhone){
        User user = new User();

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_USERS + " WHERE stdId = ? OR phone=? OR email=? ",new String[]{userIdPhone,userIdPhone,userIdPhone});

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
            int currentSessionIdIndex = cursor.getColumnIndex(COLUMN_CURRENT_SESSION);
            if (currentSessionIdIndex >= 0) {
                user.setCurrSessId(cursor.getString(currentSessionIdIndex));
            }
            int designationIdIndex = cursor.getColumnIndex(COLUMN_DESIGNATION);
            if (designationIdIndex >= 0) {
                user.setDesignation(cursor.getString(designationIdIndex));
            }

            int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
            if (syncSIndex >= 0) {
                user.setSync_status(cursor.getInt(syncSIndex));
            }

            int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
            if (syncKdIndex >= 0) {
                user.setSync_key(cursor.getString(syncKdIndex));
            }

            int stdIdIndex = cursor.getColumnIndex(COLUMN_STDID);
            if (stdIdIndex >= 0) {
                user.setStdId(cursor.getString(stdIdIndex));
            }

            int stdNameIndex = cursor.getColumnIndex(COLUMN_STDNAME);
            if (stdNameIndex >= 0) {
                user.setStdName(cursor.getString(stdNameIndex));
            }
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            if (emailIndex >= 0) {
                user.setEmail(cursor.getString(emailIndex));
            }
            int typeIndex = cursor.getColumnIndex(COLUMN_U_TYPE);
            if (typeIndex >= 0) {
                user.setU_type(cursor.getInt(typeIndex));
            }

            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
            if (phoneIndex >= 0) {
                user.setPhone(cursor.getString(phoneIndex));
            }

            int passIndex = cursor.getColumnIndex(COLUMN_PASS);
            if (passIndex >= 0) {
                user.setPass(cursor.getString(passIndex));
            }

            int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
            if (addressIndex >= 0) {
                user.setAddress(cursor.getString(addressIndex));
            }

            int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
            if (statusIndex >= 0) {
                user.setStatus(cursor.getInt(statusIndex));
            }

            int majorIndex = cursor.getColumnIndex(COLUMN_MAJOR);
            if (majorIndex >= 0) {
                user.setMajor(cursor.getString(majorIndex));
            }

            int userIdIndex = cursor.getColumnIndex(COLUMN_USERID);
            if (userIdIndex >= 0) {
                user.setUserId(cursor.getString(userIdIndex));
            }

            int pictureIndex = cursor.getColumnIndex(COLUMN_PICTURE);
            if (pictureIndex >= 0) {
                user.setPicture(cursor.getString(pictureIndex));
            }

            int photoIndex = cursor.getColumnIndex(COLUMN_PHOTO);
            if (photoIndex >= 0) {
                user.setPhoto(cursor.getBlob(photoIndex));
            }

            int fingerDataIndex = cursor.getColumnIndex(COLUMN_FINGERDATA);
            if (fingerDataIndex >= 0) {
                user.setFingerData(cursor.getBlob(fingerDataIndex));
            }
        }

        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        Cursor cursor = database.query(TABLE_USERS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User();
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
                int currentSessionIdIndex = cursor.getColumnIndex(COLUMN_CURRENT_SESSION);
                if (currentSessionIdIndex >= 0) {
                    user.setCurrSessId(cursor.getString(currentSessionIdIndex));
                }
                int designationIdIndex = cursor.getColumnIndex(COLUMN_DESIGNATION);
                if (designationIdIndex >= 0) {
                    user.setDesignation(cursor.getString(designationIdIndex));
                }

                int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
                if (syncSIndex >= 0) {
                    user.setSync_status(cursor.getInt(syncSIndex));
                }

                int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
                if (syncKdIndex >= 0) {
                    user.setSync_key(cursor.getString(syncKdIndex));
                }

                int stdIdIndex = cursor.getColumnIndex(COLUMN_STDID);
                if (stdIdIndex >= 0) {
                    user.setStdId(cursor.getString(stdIdIndex));
                }

                int stdNameIndex = cursor.getColumnIndex(COLUMN_STDNAME);
                if (stdNameIndex >= 0) {
                    user.setStdName(cursor.getString(stdNameIndex));
                }
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                if (emailIndex >= 0) {
                    user.setEmail(cursor.getString(emailIndex));
                }
                int typeIndex = cursor.getColumnIndex(COLUMN_U_TYPE);
                if (typeIndex >= 0) {
                    user.setU_type(cursor.getInt(typeIndex));
                }

                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                if (phoneIndex >= 0) {
                    user.setPhone(cursor.getString(phoneIndex));
                }

                int passIndex = cursor.getColumnIndex(COLUMN_PASS);
                if (passIndex >= 0) {
                    user.setPass(cursor.getString(passIndex));
                }

                int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                if (addressIndex >= 0) {
                    user.setAddress(cursor.getString(addressIndex));
                }

                int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
                if (statusIndex >= 0) {
                    user.setStatus(cursor.getInt(statusIndex));
                }

                int majorIndex = cursor.getColumnIndex(COLUMN_MAJOR);
                if (majorIndex >= 0) {
                    user.setMajor(cursor.getString(majorIndex));
                }

                int userIdIndex = cursor.getColumnIndex(COLUMN_USERID);
                if (userIdIndex >= 0) {
                    user.setUserId(cursor.getString(userIdIndex));
                }

                int pictureIndex = cursor.getColumnIndex(COLUMN_PICTURE);
                if (pictureIndex >= 0) {
                    user.setPicture(cursor.getString(pictureIndex));
                }

                int photoIndex = cursor.getColumnIndex(COLUMN_PHOTO);
                if (photoIndex >= 0) {
                    user.setPhoto(cursor.getBlob(photoIndex));
                }

                int fingerDataIndex = cursor.getColumnIndex(COLUMN_FINGERDATA);
                if (fingerDataIndex >= 0) {
                    user.setFingerData(cursor.getBlob(fingerDataIndex));
                }

                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return userList;
    }

    public long updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_SID, user.getSId());
        values.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        values.put(COLUMN_CURRENT_SESSION, user.getCurrSessId());
        values.put(COLUMN_DESIGNATION, user.getDesignation());
        values.put(COLUMN_STDID, user.getStdId());
        values.put(COLUMN_STDNAME, user.getStdName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_STATUS, user.getStatus());
        values.put(COLUMN_PASS, user.getPass());
        values.put(COLUMN_U_TYPE, user.getU_type());
        values.put(COLUMN_USERID, user.getUserId());
        values.put(COLUMN_MAJOR, user.getMajor());
        values.put(COLUMN_PICTURE, user.getPicture());
        values.put(COLUMN_PHOTO, user.getPhoto());
        values.put(COLUMN_SYNC_STATUS, user.getSync_status());
        values.put(COLUMN_SYNC_KEY, user.getSync_key());
        values.put(COLUMN_FINGERDATA, user.getFingerData());


        String whereClause = COLUMN_PHONE + " = ?";
        String[] whereArgs = {user.getPhone()};

        return database.update(TABLE_USERS, values, whereClause, whereArgs);
    }

    public int deleteUser(String userId) {
        String whereClause = COLUMN_PHONE + " = ?";
        String[] whereArgs = {userId};

        return database.delete(TABLE_USERS, whereClause, whereArgs);
    }

    public void createFirebaseUser(User u) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(u.getPhone(), u.getPass())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User created successfully
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Save additional user information (student ID) in Firestore or Realtime Database
                            saveUserInformation(user.getUid(), u);
                        }
                    } else {
                        // User creation failed
//                        Toast.makeText(getApplicationContext(), "Failed to create user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void saveUserInformation(String userId, User u) {
        // Save additional user information in Firestore or Realtime Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

        Map<String, Object> user = new HashMap<>();
        user.put("sync_key", userId);
        user.put("id", new Unique().uniqueId());
        user.put("sync_status", u.getSync_status());
        user.put("phone", u.getPhone());
        user.put("pass", u.getPass());
        user.put("uniqueId", u.getUniqueId());
        user.put("currSessId", u.getCurrSessId());
        user.put("designation", u.getDesignation());
        user.put("stdName", u.getStdName());
        user.put("email", u.getEmail());
        user.put("address",u.getAddress());
        user.put("sId",u.getSId());
        user.put("stdId",u.getStdId());
        user.put("userId",u.getUserId());
        user.put("major",u.getMajor());
        user.put("photo",u.getPhoto());
        user.put("picture",u.getPicture());
        user.put("status",u.getStatus());
        user.put("u_type",u.getU_type());
        user.put("fingerData",u.getFingerData());

        usersCollection.document(userId)
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

    public void saveUserInformationWithRealtime(String userId, String phoneNumber,String pass, String studentId) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("users");

        User user = new User();
            user.setPhone(phoneNumber);
            user.setPass(pass);
            user.setUserId(userId);
            user.setStdId(studentId);

        usersRef.child(userId)
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

    public void saveUserInformationWithRealtime(String userId, String phoneNumber,String pass) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("users");

        String key = usersRef.push().getKey();

        User user = new User();
        user.setId(new Unique().uniqueId());
        user.setUniqueId(key);
        user.setPhone(phoneNumber);
        user.setPass(pass);
        user.setUserId(userId);

        usersRef.child(userId)
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

    public void saveUserInformationWithRealtime(String userId, User u) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("users");

        String key = usersRef.push().getKey();


        User user = new User();
            user = u;
            user.setId(new Unique().uniqueId());
            user.setUniqueId(key);
            user.setUserId(userId);
            user.setSync_key(userId);
            user.setSync_status(1);

        usersRef.child(userId)
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

    public List<User> getUnsyncedUsersFromSQLite() {
        List<User> unsyncedUsers = new ArrayList<>();

        String[] projection = null;
        String selection = UserContract.UserEntry.COLUMN_SYNC_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(UserContract.UserEntry.SYNC_STATUS_FAILED)};

        Cursor cursor = database.query(
                TABLE_USERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User();
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
                int currentSessionIdIndex = cursor.getColumnIndex(COLUMN_CURRENT_SESSION);
                if (currentSessionIdIndex >= 0) {
                    user.setCurrSessId(cursor.getString(currentSessionIdIndex));
                }
                int designationIdIndex = cursor.getColumnIndex(COLUMN_DESIGNATION);
                if (designationIdIndex >= 0) {
                    user.setDesignation(cursor.getString(designationIdIndex));
                }

                int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
                if (syncSIndex >= 0) {
                    user.setSync_status(cursor.getInt(syncSIndex));
                }

                int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
                if (syncKdIndex >= 0) {
                    user.setSync_key(cursor.getString(syncKdIndex));
                }

                int stdIdIndex = cursor.getColumnIndex(COLUMN_STDID);
                if (stdIdIndex >= 0) {
                    user.setStdId(cursor.getString(stdIdIndex));
                }

                int stdNameIndex = cursor.getColumnIndex(COLUMN_STDNAME);
                if (stdNameIndex >= 0) {
                    user.setStdName(cursor.getString(stdNameIndex));
                }
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                if (emailIndex >= 0) {
                    user.setEmail(cursor.getString(emailIndex));
                }
                int typeIndex = cursor.getColumnIndex(COLUMN_U_TYPE);
                if (typeIndex >= 0) {
                    user.setU_type(cursor.getInt(typeIndex));
                }

                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                if (phoneIndex >= 0) {
                    user.setPhone(cursor.getString(phoneIndex));
                }

                int passIndex = cursor.getColumnIndex(COLUMN_PASS);
                if (passIndex >= 0) {
                    user.setPass(cursor.getString(passIndex));
                }

                int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                if (addressIndex >= 0) {
                    user.setAddress(cursor.getString(addressIndex));
                }

                int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
                if (statusIndex >= 0) {
                    user.setStatus(cursor.getInt(statusIndex));
                }

                int majorIndex = cursor.getColumnIndex(COLUMN_MAJOR);
                if (majorIndex >= 0) {
                    user.setMajor(cursor.getString(majorIndex));
                }

                int userIdIndex = cursor.getColumnIndex(COLUMN_USERID);
                if (userIdIndex >= 0) {
                    user.setUserId(cursor.getString(userIdIndex));
                }

                int pictureIndex = cursor.getColumnIndex(COLUMN_PICTURE);
                if (pictureIndex >= 0) {
                    user.setPicture(cursor.getString(pictureIndex));
                }

                int photoIndex = cursor.getColumnIndex(COLUMN_PHOTO);
                if (photoIndex >= 0) {
                    user.setPhoto(cursor.getBlob(photoIndex));
                }

                int fingerDataIndex = cursor.getColumnIndex(COLUMN_FINGERDATA);
                if (fingerDataIndex >= 0) {
                    user.setFingerData(cursor.getBlob(fingerDataIndex));
                }

                unsyncedUsers.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        cursor.close();

        return unsyncedUsers;
    }


    public void checkAndSyncData() {
            List<User> unsyncedUsers = getUnsyncedUsersFromSQLite();
            for (User user : unsyncedUsers) {
                // Add or update user data in Firebase
                addOrUpdateUserInFirebase(user);
            }
    }

    public void addOrUpdateUserInFirebase(User user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(String.valueOf(user.getId()));
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
        values.put(UserContract.UserEntry.COLUMN_SYNC_STATUS, syncStatus);

        String selection =  "id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        database.update(UserContract.UserEntry.TABLE_USERS, values, selection, selectionArgs);
    }


    public void updateUserData(User user, School school) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        user.setSId(school.getsId());
        String userId = user.getUserId();

        if (userId != null) {
            usersRef.child(userId).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Handle successful update
                            long r = updateUser(user);
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

    public User getUserData(String userPhone) {
        Query query = usersRef.orderByChild("phone").equalTo(userPhone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        // User found based on the phone number
                        // Pass the user object to the callback
                        userCallback.onUserRetrieved(user);
                        return;
                    }
                }
                // User not found based on the phone number
                userCallback.onUserNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });

        // Return a placeholder user object or null if needed
        return new User();
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