package com.edu.eduorganizer.school;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.NonNull;


import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserContract;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class SchoolDAO implements SchoolCallback{
    private static final String TABLE_USERS = "school";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SID = "sId";
    private static final String COLUMN_UNIQUE_ID = "uniqueId";
    private static final String COLUMN_CURRENT_SESSION = "currSessId";

    private static final String COLUMN_SCHOOL_NAME = "sName";

    private static final String COLUMN_ACTIVATE = "sActivate";
    private static final String COLUMN_SCHOOL_EMAIL = "sEmail";
    private static final String COLUMN_SCHOOL_PHONE = "sPhone";
    private static final String COLUMN_PASS = "sPass";
    private static final String COLUMN_ADDRESS = "sAdrs";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_EIIN = "sEiin";
    private static final String COLUMN_TEACHER = "sTeacher";
    private static final String COLUMN_COURSE = "sCourse";
    private static final String COLUMN_SECTION = "sSec";
    private static final String COLUMN_USER = "sUser";
    private static final String COLUMN_CLASS = "sClass";
    private static final String COLUMN_SCHOOL_IT_P1 = "sItp1";
    private static final String COLUMN_SCHOOL_IT_P2 = "sItp2";
    private static final String COLUMN_SCHOOL_IT_EMAIL = "sItEmail";
    private static final String COLUMN_WEB = "sWeb";
    private static final String COLUMN_FUNDS_BALANCE = "sFundsBal";
    private static final String COLUMN_FUNDS_BANK = "sFundsBank";
    private static final String COLUMN_FUNDS_ACCOUNT = "sFundsAN";
    private static final String COLUMN_VERIFICATION = "sVerification";
    private static final String COLUMN_EMPLOYEE = "sEmpl";
    private static final String COLUMN_STUDENT = "sStudent";
    private static final String COLUMN_PICTURE = "proPic";
    private static final String COLUMN_LOGO = "sLogo";
    private static final String COLUMN_SYNC_STATUS = "sync_status";
    private static final String COLUMN_SYNC_KEY = "sync_key";
    private static final String COLUMN_FINGERDATA = "fingerData";
    private DatabaseManager databaseManager;

    private FirebaseAuth mAuth;
    private Internet internet;
    private String sId;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = firebaseDatabase.getReference("school");
    private DatabaseReference schoolRef = firebaseDatabase.getReference("school");

    private SQLiteDatabase database;

    private static final String SELECT_ALL = "SELECT *  FROM "+TABLE_USERS;
    private SchoolCallback schoolCallback;

    private School school;

    public SchoolDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public SchoolDAO(Context context) {
        databaseManager = new DatabaseManager(context);
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();
        this.database = database;
    }

    public long insertSchool(School user) {
        long l=-2;
        int r=0;
        r = checkSchool(user.getsPhone());
        if(r!=0){
            return -3;
        }else {
            Log.e("UserFound","School number found");
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_SID, user.getsId());
        values.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        values.put(COLUMN_CURRENT_SESSION, user.getCurrSessId());
        values.put(COLUMN_SCHOOL_NAME, user.getsName());
        values.put(COLUMN_SCHOOL_PHONE, user.getsPhone());
        values.put(COLUMN_SCHOOL_IT_P2, user.getsItp2());
        values.put(COLUMN_STUDENT, user.getsStudent());
        values.put(COLUMN_SCHOOL_IT_P1, user.getsItp1());
        values.put(COLUMN_PASS, user.getsPass());
        values.put(COLUMN_ADDRESS, user.getsAdrs());
//        values.put(COLUMN_STATUS, user.getStatus());
        values.put(COLUMN_SECTION, user.getsSec());
        values.put(COLUMN_EIIN, user.getsEiin());
        values.put(COLUMN_EMPLOYEE, user.getsEmpl());
        values.put(COLUMN_SCHOOL_EMAIL, user.getsEmail());
        values.put(COLUMN_SCHOOL_IT_EMAIL, user.getsItEmail());
        values.put(COLUMN_CLASS, user.getsClass());
        values.put(COLUMN_ACTIVATE, user.getsActivate());
        values.put(COLUMN_VERIFICATION, user.getsVerification());
        values.put(COLUMN_WEB, user.getsWeb());
        values.put(COLUMN_FUNDS_BANK, user.getsFundsBank());
        values.put(COLUMN_FUNDS_ACCOUNT, user.getsFundsAN());
        values.put(COLUMN_FUNDS_BALANCE, user.getsFundsBal());
        values.put(COLUMN_TEACHER, user.getsTeacher());
        values.put(COLUMN_COURSE, user.getsCourse());
//        values.put(COLUMN_WEB, user.getUserId());
        values.put(COLUMN_USER, user.getsUser());
        values.put(COLUMN_LOGO, user.getsLogo());
        values.put(COLUMN_SYNC_KEY, user.getSync_key());
        values.put(COLUMN_SYNC_STATUS, user.getSync_status());

        try {
            l = database.insert(TABLE_USERS, null, values);
        }catch (SQLiteException e){
            String ee = e.getMessage();
            Log.e("error",ee);
        }

        return l;
    }

    public Cursor getSchoolCursor(){
        School user = new School();

        Cursor cursor = database.rawQuery(SELECT_ALL,null);


        return  cursor;
    }
    public int checkSchool(String userIdPhone){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_USERS + " WHERE sPhone=? OR sEmail=? ",new String[]{userIdPhone,userIdPhone});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }

    public int checkSchool(String userIdPhone,String pass){
        int r = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_USERS + " WHERE sPass = ? AND ( sPhone=? OR sEmail=?)",new String[]{pass,userIdPhone,userIdPhone});

        if (cursor != null && cursor.getCount() > 0){
            r = cursor.getCount();
        }

        return r;
    }

    public boolean checkSchool(){
        boolean i = false;



        return i;
    }

    public School getSchool(String userIdPhone){
        School user = new School();

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_USERS + " WHERE sPhone=? OR sEmail=? ",new String[]{userIdPhone,userIdPhone});

        if (cursor != null && cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            if (idIndex >= 0) {
                user.setId(cursor.getInt(idIndex));
            }

            int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
            if (sIdIndex >= 0) {
                user.setsId(cursor.getString(sIdIndex));
            }

            int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
            if (uniqueIdIndex >= 0) {
                user.setUniqueId(cursor.getString(uniqueIdIndex));
            }
            int currentSessionIdIndex = cursor.getColumnIndex(COLUMN_CURRENT_SESSION);
            if (currentSessionIdIndex >= 0) {
                user.setCurrSessId(cursor.getString(currentSessionIdIndex));
            }

            int snameIndex = cursor.getColumnIndex(COLUMN_SCHOOL_NAME);
            if (snameIndex >= 0) {
                user.setsName(cursor.getString(snameIndex));
            }

            int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
            if (syncSIndex >= 0) {
                user.setSync_status(cursor.getInt(syncSIndex));
            }

            int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
            if (syncKdIndex >= 0) {
                user.setSync_key(cursor.getString(syncKdIndex));
            }

            int stdIdIndex = cursor.getColumnIndex(COLUMN_EMPLOYEE);
            if (stdIdIndex >= 0) {
                user.setsClass(cursor.getInt(stdIdIndex));
            }

            int stdNameIndex = cursor.getColumnIndex(COLUMN_SECTION);
            if (stdNameIndex >= 0) {
                user.setsSec(cursor.getInt(stdNameIndex));
            }
            int sCourseIndex = cursor.getColumnIndex(COLUMN_COURSE);
            if (sCourseIndex >= 0) {
                user.setsCourse(cursor.getInt(sCourseIndex));
            }
            int sClassIndex = cursor.getColumnIndex(COLUMN_CLASS);
            if (sClassIndex >= 0) {
                user.setsClass(cursor.getInt(sClassIndex));
            }
            int teacherIndex = cursor.getColumnIndex(COLUMN_TEACHER);
            if (teacherIndex >= 0) {
                user.setsTeacher(cursor.getInt(teacherIndex));
            }
            int studentIndex = cursor.getColumnIndex(COLUMN_STUDENT);
            if (studentIndex >= 0) {
                user.setsStudent(cursor.getInt(studentIndex));
            }
            int emailIndex = cursor.getColumnIndex(COLUMN_SCHOOL_EMAIL);
            if (emailIndex >= 0) {
                user.setsEmail(cursor.getString(emailIndex));
            }
            int typeIndex = cursor.getColumnIndex(COLUMN_USER);
            if (typeIndex >= 0) {
                user.setsUser(cursor.getInt(typeIndex));
            }

            int phoneIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_P1);
            if (phoneIndex >= 0) {
                user.setsItp1(cursor.getString(phoneIndex));
            }

            int passIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_P2);
            if (passIndex >= 0) {
                user.setsItp2(cursor.getString(passIndex));
            }

            int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
            if (addressIndex >= 0) {
                user.setsAdrs(cursor.getString(addressIndex));
            }

            int statusIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_EMAIL);
            if (statusIndex >= 0) {
                user.setsItEmail(cursor.getString(statusIndex));
            }

            int majorIndex = cursor.getColumnIndex(COLUMN_WEB);
            if (majorIndex >= 0) {
                user.setsWeb(cursor.getString(majorIndex));
            }

            int userIdIndex = cursor.getColumnIndex(COLUMN_EIIN);
            if (userIdIndex >= 0) {
                user.setsEiin(cursor.getString(userIdIndex));
            }

            int sphoneIndex = cursor.getColumnIndex(COLUMN_SCHOOL_PHONE);
            if (sphoneIndex >= 0) {
                user.setsPhone(cursor.getString(sphoneIndex));
            }

            int ActivateIndex = cursor.getColumnIndex(COLUMN_ACTIVATE);
            if (ActivateIndex >= 0) {
                user.setsActivate(cursor.getInt(ActivateIndex));
            }

            int veriIndex = cursor.getColumnIndex(COLUMN_VERIFICATION);
            if (veriIndex >= 0) {
                user.setsVerification(cursor.getString(veriIndex));
            }

            int photoIndex = cursor.getColumnIndex(COLUMN_LOGO);
            if (photoIndex >= 0) {
                user.setsLogo(cursor.getString(photoIndex));
            }
            int fundsbalIndex = cursor.getColumnIndex(COLUMN_FUNDS_BALANCE);
            if (fundsbalIndex >= 0) {
                user.setsFundsBal(cursor.getString(fundsbalIndex));
            }
            int fundsAcIndex = cursor.getColumnIndex(COLUMN_FUNDS_ACCOUNT);
            if (fundsAcIndex >= 0) {
                user.setsFundsAN(cursor.getString(fundsAcIndex));
            }
            int fundsbankIndex = cursor.getColumnIndex(COLUMN_FUNDS_BANK);
            if (fundsbankIndex >= 0) {
                user.setsFundsBank(cursor.getString(fundsbankIndex));
            }

        }

        return user;
    }


    public School getSchoolBySID(String sId){
        School user = new School();

        Cursor cursor = database.rawQuery("SELECT * FROM " +TABLE_USERS + " WHERE sId=? OR sPhone=? OR sEmail=? ",new String[]{sId,sId,sId});

        if (cursor != null && cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            if (idIndex >= 0) {
                user.setId(cursor.getInt(idIndex));
            }

            int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
            if (sIdIndex >= 0) {
                user.setsId(cursor.getString(sIdIndex));
            }

            int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
            if (uniqueIdIndex >= 0) {
                user.setUniqueId(cursor.getString(uniqueIdIndex));
            }
            int currentSessionIdIndex = cursor.getColumnIndex(COLUMN_CURRENT_SESSION);
            if (currentSessionIdIndex >= 0) {
                user.setCurrSessId(cursor.getString(currentSessionIdIndex));
            }

            int snameIndex = cursor.getColumnIndex(COLUMN_SCHOOL_NAME);
            if (snameIndex >= 0) {
                user.setsName(cursor.getString(snameIndex));
            }

            int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
            if (syncSIndex >= 0) {
                user.setSync_status(cursor.getInt(syncSIndex));
            }

            int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
            if (syncKdIndex >= 0) {
                user.setSync_key(cursor.getString(syncKdIndex));
            }

            int stdIdIndex = cursor.getColumnIndex(COLUMN_EMPLOYEE);
            if (stdIdIndex >= 0) {
                user.setsClass(cursor.getInt(stdIdIndex));
            }

            int stdNameIndex = cursor.getColumnIndex(COLUMN_SECTION);
            if (stdNameIndex >= 0) {
                user.setsSec(cursor.getInt(stdNameIndex));
            }
            int sCourseIndex = cursor.getColumnIndex(COLUMN_COURSE);
            if (sCourseIndex >= 0) {
                user.setsCourse(cursor.getInt(sCourseIndex));
            }
            int sClassIndex = cursor.getColumnIndex(COLUMN_CLASS);
            if (sClassIndex >= 0) {
                user.setsClass(cursor.getInt(sClassIndex));
            }
            int teacherIndex = cursor.getColumnIndex(COLUMN_TEACHER);
            if (teacherIndex >= 0) {
                user.setsTeacher(cursor.getInt(teacherIndex));
            }
            int studentIndex = cursor.getColumnIndex(COLUMN_STUDENT);
            if (studentIndex >= 0) {
                user.setsStudent(cursor.getInt(studentIndex));
            }
            int emailIndex = cursor.getColumnIndex(COLUMN_SCHOOL_EMAIL);
            if (emailIndex >= 0) {
                user.setsEmail(cursor.getString(emailIndex));
            }
            int typeIndex = cursor.getColumnIndex(COLUMN_USER);
            if (typeIndex >= 0) {
                user.setsUser(cursor.getInt(typeIndex));
            }

            int phoneIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_P1);
            if (phoneIndex >= 0) {
                user.setsItp1(cursor.getString(phoneIndex));
            }

            int passIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_P2);
            if (passIndex >= 0) {
                user.setsItp2(cursor.getString(passIndex));
            }

            int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
            if (addressIndex >= 0) {
                user.setsAdrs(cursor.getString(addressIndex));
            }

            int statusIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_EMAIL);
            if (statusIndex >= 0) {
                user.setsItEmail(cursor.getString(statusIndex));
            }

            int majorIndex = cursor.getColumnIndex(COLUMN_WEB);
            if (majorIndex >= 0) {
                user.setsWeb(cursor.getString(majorIndex));
            }

            int userIdIndex = cursor.getColumnIndex(COLUMN_EIIN);
            if (userIdIndex >= 0) {
                user.setsEiin(cursor.getString(userIdIndex));
            }

            int sphoneIndex = cursor.getColumnIndex(COLUMN_SCHOOL_PHONE);
            if (sphoneIndex >= 0) {
                user.setsPhone(cursor.getString(sphoneIndex));
            }

            int ActivateIndex = cursor.getColumnIndex(COLUMN_ACTIVATE);
            if (ActivateIndex >= 0) {
                user.setsActivate(cursor.getInt(ActivateIndex));
            }

            int veriIndex = cursor.getColumnIndex(COLUMN_VERIFICATION);
            if (veriIndex >= 0) {
                user.setsVerification(cursor.getString(veriIndex));
            }

            int photoIndex = cursor.getColumnIndex(COLUMN_LOGO);
            if (photoIndex >= 0) {
                user.setsLogo(cursor.getString(photoIndex));
            }
            int fundsbalIndex = cursor.getColumnIndex(COLUMN_FUNDS_BALANCE);
            if (fundsbalIndex >= 0) {
                user.setsFundsBal(cursor.getString(fundsbalIndex));
            }
            int fundsAcIndex = cursor.getColumnIndex(COLUMN_FUNDS_ACCOUNT);
            if (fundsAcIndex >= 0) {
                user.setsFundsAN(cursor.getString(fundsAcIndex));
            }
            int fundsbankIndex = cursor.getColumnIndex(COLUMN_FUNDS_BANK);
            if (fundsbankIndex >= 0) {
                user.setsFundsBank(cursor.getString(fundsbankIndex));
            }

        }

        return user;
    }


    public List<School> getAllSchool() {
        List<School> userList = new ArrayList<>();

        Cursor cursor = database.query(TABLE_USERS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                School user = new School();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setsId(cursor.getString(sIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
                }
                int currentSessionIdIndex = cursor.getColumnIndex(COLUMN_CURRENT_SESSION);
                if (currentSessionIdIndex >= 0) {
                    user.setCurrSessId(cursor.getString(currentSessionIdIndex));
                }

                int snameIndex = cursor.getColumnIndex(COLUMN_SCHOOL_NAME);
                if (snameIndex >= 0) {
                    user.setsName(cursor.getString(snameIndex));
                }

                int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
                if (syncSIndex >= 0) {
                    user.setSync_status(cursor.getInt(syncSIndex));
                }

                int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
                if (syncKdIndex >= 0) {
                    user.setSync_key(cursor.getString(syncKdIndex));
                }

                int stdIdIndex = cursor.getColumnIndex(COLUMN_EMPLOYEE);
                if (stdIdIndex >= 0) {
                    user.setsClass(cursor.getInt(stdIdIndex));
                }

                int stdNameIndex = cursor.getColumnIndex(COLUMN_SECTION);
                if (stdNameIndex >= 0) {
                    user.setsSec(cursor.getInt(stdNameIndex));
                }
                int sCourseIndex = cursor.getColumnIndex(COLUMN_COURSE);
                if (sCourseIndex >= 0) {
                    user.setsCourse(cursor.getInt(sCourseIndex));
                }
                int sClassIndex = cursor.getColumnIndex(COLUMN_CLASS);
                if (sClassIndex >= 0) {
                    user.setsClass(cursor.getInt(sClassIndex));
                }
                int teacherIndex = cursor.getColumnIndex(COLUMN_TEACHER);
                if (teacherIndex >= 0) {
                    user.setsTeacher(cursor.getInt(teacherIndex));
                }
                int studentIndex = cursor.getColumnIndex(COLUMN_STUDENT);
                if (studentIndex >= 0) {
                    user.setsStudent(cursor.getInt(studentIndex));
                }
                int emailIndex = cursor.getColumnIndex(COLUMN_SCHOOL_EMAIL);
                if (emailIndex >= 0) {
                    user.setsEmail(cursor.getString(emailIndex));
                }
                int typeIndex = cursor.getColumnIndex(COLUMN_USER);
                if (typeIndex >= 0) {
                    user.setsUser(cursor.getInt(typeIndex));
                }

                int phoneIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_P1);
                if (phoneIndex >= 0) {
                    user.setsItp1(cursor.getString(phoneIndex));
                }

                int passIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_P2);
                if (passIndex >= 0) {
                    user.setsItp2(cursor.getString(passIndex));
                }

                int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                if (addressIndex >= 0) {
                    user.setsAdrs(cursor.getString(addressIndex));
                }

                int statusIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_EMAIL);
                if (statusIndex >= 0) {
                    user.setsItEmail(cursor.getString(statusIndex));
                }

                int majorIndex = cursor.getColumnIndex(COLUMN_WEB);
                if (majorIndex >= 0) {
                    user.setsWeb(cursor.getString(majorIndex));
                }

                int userIdIndex = cursor.getColumnIndex(COLUMN_EIIN);
                if (userIdIndex >= 0) {
                    user.setsEiin(cursor.getString(userIdIndex));
                }

                int sphoneIndex = cursor.getColumnIndex(COLUMN_SCHOOL_PHONE);
                if (sphoneIndex >= 0) {
                    user.setsPhone(cursor.getString(sphoneIndex));
                }

                int ActivateIndex = cursor.getColumnIndex(COLUMN_ACTIVATE);
                if (ActivateIndex >= 0) {
                    user.setsActivate(cursor.getInt(ActivateIndex));
                }

                int veriIndex = cursor.getColumnIndex(COLUMN_VERIFICATION);
                if (veriIndex >= 0) {
                    user.setsVerification(cursor.getString(veriIndex));
                }

                int photoIndex = cursor.getColumnIndex(COLUMN_LOGO);
                if (photoIndex >= 0) {
                    user.setsLogo(cursor.getString(photoIndex));
                }
                int fundsbalIndex = cursor.getColumnIndex(COLUMN_FUNDS_BALANCE);
                if (fundsbalIndex >= 0) {
                    user.setsFundsBal(cursor.getString(fundsbalIndex));
                }
                int fundsAcIndex = cursor.getColumnIndex(COLUMN_FUNDS_ACCOUNT);
                if (fundsAcIndex >= 0) {
                    user.setsFundsAN(cursor.getString(fundsAcIndex));
                }
                int fundsbankIndex = cursor.getColumnIndex(COLUMN_FUNDS_BANK);
                if (fundsbankIndex >= 0) {
                    user.setsFundsBank(cursor.getString(fundsbankIndex));
                }

                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return userList;
    }

    public long updateSchool(School user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SID, user.getsId());
        values.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        values.put(COLUMN_CURRENT_SESSION, user.getCurrSessId());
        values.put(COLUMN_SCHOOL_NAME, user.getsName());
        values.put(COLUMN_SCHOOL_PHONE, user.getsPhone());
        values.put(COLUMN_SCHOOL_IT_P2, user.getsItp2());
        values.put(COLUMN_STUDENT, user.getsStudent());
        values.put(COLUMN_SCHOOL_IT_P1, user.getsItp1());
        values.put(COLUMN_PASS, user.getsPass());
        values.put(COLUMN_ADDRESS, user.getsAdrs());
//        values.put(COLUMN_STATUS, user.getStatus());
        values.put(COLUMN_SECTION, user.getsSec());
        values.put(COLUMN_EIIN, user.getsEiin());
        values.put(COLUMN_EMPLOYEE, user.getsEmpl());
        values.put(COLUMN_SCHOOL_EMAIL, user.getsEmail());
        values.put(COLUMN_SCHOOL_IT_EMAIL, user.getsItEmail());
        values.put(COLUMN_CLASS, user.getsClass());
        values.put(COLUMN_ACTIVATE, user.getsActivate());
        values.put(COLUMN_VERIFICATION, user.getsVerification());
        values.put(COLUMN_WEB, user.getsWeb());
        values.put(COLUMN_FUNDS_BANK, user.getsFundsBank());
        values.put(COLUMN_FUNDS_ACCOUNT, user.getsFundsAN());
        values.put(COLUMN_FUNDS_BALANCE, user.getsFundsBal());
        values.put(COLUMN_TEACHER, user.getsTeacher());
        values.put(COLUMN_COURSE, user.getsCourse());
//        values.put(COLUMN_WEB, user.getUserId());
        values.put(COLUMN_USER, user.getsUser());
        values.put(COLUMN_LOGO, user.getsLogo());
        values.put(COLUMN_SYNC_KEY, user.getSync_key());
        values.put(COLUMN_SYNC_STATUS, user.getSync_status());

        String whereClause = COLUMN_SCHOOL_PHONE + " = ?";
        String[] whereArgs = {user.getsPhone()};

        return database.update(TABLE_USERS, values, whereClause, whereArgs);
    }

    public int deleteSchool(String userId) {
        String whereClause = COLUMN_SCHOOL_PHONE + " = ?";
        String[] whereArgs = {userId};

        return database.delete(TABLE_USERS, whereClause, whereArgs);
    }
    public int createSqlSchool(String a, String b, String c, String d, int syncStatus) {

        School user = new School();
//        user.setId(new Admin().uniqueId());
        user.setSync_status(syncStatus);
        user.setsPhone(a);
        user.setsPass(b);
        user.setsName(c);
        user.setsEmail(d);

        int r=0,re=0;
        r = checkSchool(user.getsPhone());
        if(r!=0){
            Log.e("UserFound","School already have with this number");
            return re;
        }
        long rs = insertSchool(user);

        if(rs==-1){
            re=-1;
            Log.e("UserFound","Data Not inserted! Try again!  "+rs+"  ");

        }else if(rs==-3){
            re=-3;
            Log.e("UserFound","School already have with this number");

        }else if(rs==-2){
            re=-2;
            Log.e("UserFound","Error occured! Try again!  "+rs+"  ");

        } else {

            Log.e("UserFound","Schoo Data Successfully Inserted, Thanks!");

        }

        return re;
    }
    public int createSqlSchool(School school) {

        School user = new School();
//        user.setId(new Admin().uniqueId());
        user.setSync_status(school.getSync_status());
        user.setSync_key(school.getSync_key());
        user.setsPhone(school.getsPhone());
        user.setsPass(school.getsPass());
        user.setsName(school.getsName());
        user.setsEmail(school.getsEmail());
        user=school;

        int r=0,re=0;
        r = checkSchool(user.getsPhone());
        if(r!=0){
            Log.e("UserFound","School already have with this number");
            return re;
        }
        long rs = insertSchool(user);

        if(rs==-1){
            re=-1;
            Log.e("UserFound","Data Not inserted! Try again!  "+rs+"  ");

        }else if(rs==-3){
            re=-3;
            Log.e("UserFound","School already have with this number");

        }else if(rs==-2){
            re=-2;
            Log.e("UserFound","Error occured! Try again!  "+rs+"  ");

        } else {

            Log.e("UserFound","Schoo Data Successfully Inserted, Thanks!");

        }

        return re;
    }

    public void createFirebaseUserRealtime(String phoneNumber, String password, String sclName, String sclEmail) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(sclEmail, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User created successfully
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Save additional user information (student ID) in Firestore or Realtime Database
                            School school = new School();
                            school.setsId(new Unique().uId());
                            school.setsPhone(phoneNumber);
                            school.setsPass(password);
                            school.setsName(sclName);
                            school.setsEmail(sclEmail);
                            saveSchoolInformationWithRealtime(user.getUid(), school);

                        } else {
                            // User creation failed
//                            Toast.makeText(getApplicationContext(), "Failed to create user: User is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User creation failed
//                        Toast.makeText(getApplicationContext(), "Failed to create user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any potential exceptions during user creation
//                    Toast.makeText(getApplicationContext(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void createFirebaseUserRealtime(School school) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(school.getsEmail(), school.getsPass())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User created successfully
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Save additional user information (student ID) in Firestore or Realtime Database

                            saveSchoolInformationWithRealtime(user.getUid(), school);

                        } else {
                            // User creation failed
//                            Toast.makeText(getApplicationContext(), "Failed to create user: User is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User creation failed
//                        Toast.makeText(getApplicationContext(), "Failed to create user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any potential exceptions during user creation
//                    Toast.makeText(getApplicationContext(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void createFirebaseUserWithRealtimeSchool(School school) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(school.getsEmail(), school.getsPass())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User created successfully
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Save additional user information (student ID) in Firestore or Realtime Database

                            saveSchoolInformationWithRealtime(user.getUid(), school);

                        } else {
                            // User creation failed
//                            Toast.makeText(getApplicationContext(), "Failed to create user: User is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User creation failed
//                        Toast.makeText(getApplicationContext(), "Failed to create user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any potential exceptions during user creation
//                    Toast.makeText(getApplicationContext(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void createFirebaseUser(String phoneNumber, String password, String sclName, String sclEmail) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(sclEmail, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User created successfully
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Save additional user information (student ID) in Firestore or Realtime Database

                            saveSchoolInformation(user.getUid(), phoneNumber, password, sclName, sclEmail);

                        } else {
                            // User creation failed
//                            Toast.makeText(getApplicationContext(), "Failed to create user: User is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User creation failed
//                        Toast.makeText(getApplicationContext(), "Failed to create user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any potential exceptions during user creation
//                    Toast.makeText(getApplicationContext(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void createFirebaseUser(School school) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(school.getsEmail(), school.getsPass())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User created successfully
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Save additional user information (student ID) in Firestore or Realtime Database

                            saveSchoolInformation(user.getUid(), school.getsPhone(), school.getsPass(), school.getsName(), school.getsEmail());

                        } else {
                            // User creation failed
//                            Toast.makeText(getApplicationContext(), "Failed to create user: User is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User creation failed
//                        Toast.makeText(getApplicationContext(), "Failed to create user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any potential exceptions during user creation
//                    Toast.makeText(getApplicationContext(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void createFirebaseUserWithSchool(School school) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(school.getsEmail(), school.getsPass())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User created successfully
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Save additional user information (student ID) in Firestore or Realtime Database

                            saveSchoolInformation(user.getUid(), school);

                        } else {
                            // User creation failed
//                            Toast.makeText(getApplicationContext(), "Failed to create user: User is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User creation failed
//                        Toast.makeText(getApplicationContext(), "Failed to create user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any potential exceptions during user creation
//                    Toast.makeText(getApplicationContext(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void saveSchoolInformation(String userId, String phoneNumber, String password, String sclName, String sclEmail) {
        // Save additional user information in Firestore or Realtime Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("school");

        Map<String, Object> user = new HashMap<>();
        user.put("sync_key", userId);
        user.put("id", new Unique().uniqueId());
        user.put("sync_status", 1);
        user.put("sPhone", phoneNumber);
        user.put("sPass", password);
        user.put("sName", sclName);
        user.put("sEmail", sclEmail);
        user.put("sId", new Unique().uId());
        user.put("sLogo","");
        user.put("sAdrs","");
        user.put("sEiin","");
        user.put("sItp1","");
        user.put("sItp2","");
        user.put("sItEmail","");
        user.put("sWeb","");
        user.put("uniqueId", "");
        user.put("currSessId", "");
        user.put("sFundsBal","");
        user.put("sFundsBank","");
        user.put("sFundsAN","");
        user.put("sActivate",0);
        user.put("sVerification","");
        user.put("sStudent",0);
        user.put("sTeacher",0);
        user.put("sClass",0);
        user.put("sUser",0);
        user.put("sSec",0);
        user.put("sCourse",0);
        user.put("sEmpl",0);

        usersCollection.document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
//                    Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
//                    Toast.makeText(this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void saveSchoolInformation(String userId, School s) {
        // Save additional user information in Firestore or Realtime Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("school");

        Map<String, Object> user = new HashMap<>();
        user.put("sync_key", userId);
        user.put("id", new Unique().uniqueId());
        user.put("sync_status", s.getSync_status());
        user.put("sPhone", s.getsPhone());
        user.put("sPass", s.getsPass());
        user.put("sName", s.getsName());
        user.put("sEmail", s.getsEmail());
        user.put("sWeb",s.getsWeb());
        user.put("sUser",s.getsUser());
        user.put("sTeacher",s.getsTeacher());
        user.put("sStudent",s.getsStudent());
        user.put("sSec",s.getsSec());
        user.put("sClass",s.getsClass());
        user.put("uniqueId", s.getUniqueId());
        user.put("currSessId", s.getCurrSessId());
        user.put("sCourse",s.getsCourse());
        user.put("sEmpl",s.getsEmpl());
        user.put("sEiin",s.getsEiin());
        user.put("sItp1",s.getsItp1());
        user.put("sItp2",s.getsItp2());
        user.put("sLogo",s.getsLogo());
        user.put("sFundsAN",s.getsFundsAN());
        user.put("sFundsBal",s.getsFundsBal());
        user.put("sFundsBank",s.getsFundsBank());
        user.put("sVerification",s.getsVerification());
        user.put("sActivate",s.getsActivate());
        user.put("sItEmail",s.getsItEmail());
        user.put("sAdrs",s.getsAdrs());
        user.put("sAYear",s.getsAYear());
        user.put("sId",s.getsId());


        usersCollection.document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
//                    Toast.makeText(get, "User created successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
//                    Toast.makeText(this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void saveSchoolInformationWithRealtime(School school) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("school");

        String key = usersRef.push().getKey();

        String userId = new Unique().userId();

        School s = new School();
        s = school;
        s.setUniqueId(key);
        s.setSync_key(userId);
        s.setSync_status(1);

        usersRef.child(userId)
                .setValue(s)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
//                    Toast.makeText(getApplicationContext(), "School User created successfully", Toast.LENGTH_SHORT).show();
//                    finish(); // Finish sign-up activity and return to login screen
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
//                    Toast.makeText(getApplicationContext(), "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void saveSchoolInformationWithRealtime(String userId, School s) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("school");

        String key = usersRef.push().getKey();

        School user = new School();
        user = s;
        user.setId(new Unique().uniqueId());
        user.setUniqueId(key);
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


    public List<School> getUnsyncedSchoolFromSQLite() {
        List<School> unsyncedUsers = new ArrayList<>();

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
                School user = new School();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if (idIndex >= 0) {
                    user.setId(cursor.getInt(idIndex));
                }

                int sIdIndex = cursor.getColumnIndex(COLUMN_SID);
                if (sIdIndex >= 0) {
                    user.setsId(cursor.getString(sIdIndex));
                }

                int uniqueIdIndex = cursor.getColumnIndex(COLUMN_UNIQUE_ID);
                if (uniqueIdIndex >= 0) {
                    user.setUniqueId(cursor.getString(uniqueIdIndex));
                }
                int currentSessionIdIndex = cursor.getColumnIndex(COLUMN_CURRENT_SESSION);
                if (currentSessionIdIndex >= 0) {
                    user.setCurrSessId(cursor.getString(currentSessionIdIndex));
                }

                int snameIndex = cursor.getColumnIndex(COLUMN_SCHOOL_NAME);
                if (snameIndex >= 0) {
                    user.setsName(cursor.getString(snameIndex));
                }

                int syncSIndex = cursor.getColumnIndex(COLUMN_SYNC_STATUS);
                if (syncSIndex >= 0) {
                    user.setSync_status(cursor.getInt(syncSIndex));
                }

                int syncKdIndex = cursor.getColumnIndex(COLUMN_SYNC_KEY);
                if (syncKdIndex >= 0) {
                    user.setSync_key(cursor.getString(syncKdIndex));
                }

                int stdIdIndex = cursor.getColumnIndex(COLUMN_EMPLOYEE);
                if (stdIdIndex >= 0) {
                    user.setsClass(cursor.getInt(stdIdIndex));
                }

                int stdNameIndex = cursor.getColumnIndex(COLUMN_SECTION);
                if (stdNameIndex >= 0) {
                    user.setsSec(cursor.getInt(stdNameIndex));
                }
                int sCourseIndex = cursor.getColumnIndex(COLUMN_COURSE);
                if (sCourseIndex >= 0) {
                    user.setsCourse(cursor.getInt(sCourseIndex));
                }
                int sClassIndex = cursor.getColumnIndex(COLUMN_CLASS);
                if (sClassIndex >= 0) {
                    user.setsClass(cursor.getInt(sClassIndex));
                }
                int teacherIndex = cursor.getColumnIndex(COLUMN_TEACHER);
                if (teacherIndex >= 0) {
                    user.setsTeacher(cursor.getInt(teacherIndex));
                }
                int studentIndex = cursor.getColumnIndex(COLUMN_STUDENT);
                if (studentIndex >= 0) {
                    user.setsStudent(cursor.getInt(studentIndex));
                }
                int emailIndex = cursor.getColumnIndex(COLUMN_SCHOOL_EMAIL);
                if (emailIndex >= 0) {
                    user.setsEmail(cursor.getString(emailIndex));
                }
                int typeIndex = cursor.getColumnIndex(COLUMN_USER);
                if (typeIndex >= 0) {
                    user.setsUser(cursor.getInt(typeIndex));
                }

                int phoneIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_P1);
                if (phoneIndex >= 0) {
                    user.setsItp1(cursor.getString(phoneIndex));
                }

                int passIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_P2);
                if (passIndex >= 0) {
                    user.setsItp2(cursor.getString(passIndex));
                }

                int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                if (addressIndex >= 0) {
                    user.setsAdrs(cursor.getString(addressIndex));
                }

                int statusIndex = cursor.getColumnIndex(COLUMN_SCHOOL_IT_EMAIL);
                if (statusIndex >= 0) {
                    user.setsItEmail(cursor.getString(statusIndex));
                }

                int majorIndex = cursor.getColumnIndex(COLUMN_WEB);
                if (majorIndex >= 0) {
                    user.setsWeb(cursor.getString(majorIndex));
                }

                int userIdIndex = cursor.getColumnIndex(COLUMN_EIIN);
                if (userIdIndex >= 0) {
                    user.setsEiin(cursor.getString(userIdIndex));
                }

                int sphoneIndex = cursor.getColumnIndex(COLUMN_SCHOOL_PHONE);
                if (sphoneIndex >= 0) {
                    user.setsPhone(cursor.getString(sphoneIndex));
                }

                int ActivateIndex = cursor.getColumnIndex(COLUMN_ACTIVATE);
                if (ActivateIndex >= 0) {
                    user.setsActivate(cursor.getInt(ActivateIndex));
                }

                int veriIndex = cursor.getColumnIndex(COLUMN_VERIFICATION);
                if (veriIndex >= 0) {
                    user.setsVerification(cursor.getString(veriIndex));
                }

                int photoIndex = cursor.getColumnIndex(COLUMN_LOGO);
                if (photoIndex >= 0) {
                    user.setsLogo(cursor.getString(photoIndex));
                }
                int fundsbalIndex = cursor.getColumnIndex(COLUMN_FUNDS_BALANCE);
                if (fundsbalIndex >= 0) {
                    user.setsFundsBal(cursor.getString(fundsbalIndex));
                }
                int fundsAcIndex = cursor.getColumnIndex(COLUMN_FUNDS_ACCOUNT);
                if (fundsAcIndex >= 0) {
                    user.setsFundsAN(cursor.getString(fundsAcIndex));
                }
                int fundsbankIndex = cursor.getColumnIndex(COLUMN_FUNDS_BANK);
                if (fundsbankIndex >= 0) {
                    user.setsFundsBank(cursor.getString(fundsbankIndex));
                }

                unsyncedUsers.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        cursor.close();

        return unsyncedUsers;
    }


    public void checkAndSyncData() {
        List<School> unsyncedUsers = getUnsyncedSchoolFromSQLite();
        for (School uuser : unsyncedUsers) {
            // Add or update user data in Firebase
            addOrUpdateSchoolInFirebase(uuser);
        }
    }

    private void addOrUpdateSchoolInFirebase(School user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("school").child(String.valueOf(user.getId()));
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

    private School getSchoolData(String sId) {
        Query query = schoolRef.orderByChild("sId").equalTo(sId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    School scl = userSnapshot.getValue(School.class);
                    if (scl != null) {
//                        Log.e("UserFound","User FF Found");
                        school = scl;
                        schoolCallback.onSchoolRetrieved(scl);
                        return;
                    }
                }
                schoolCallback.onSchoolNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return new School();
    }

    public void updateSchoolDataBysId(School scl) {

        DatabaseReference schoolRef = FirebaseDatabase.getInstance().getReference("school");

        String key = schoolRef.push().getKey();
        String sId = scl.getsId();
        if(scl.getSync_key()==null){
            scl.setSync_key(key);
        }

        if (sId != null) {
            schoolRef.child(sId).setValue(scl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            long rr = updateSchool(scl);
                            if (rr!=-1){
                                Log.e("UserFound","school data Successfully Update In Local & Online");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failed update
                            Log.e("schoolError","School data not Updated!");
                        }
                    });
        }else {
            Log.e("schoolError","school data not found");
        }

    }


    public void updateSchoolDataByUid(School scl) {

        DatabaseReference schoolRef = FirebaseDatabase.getInstance().getReference("school");
        String key = schoolRef.push().getKey();
        String uniqueId = scl.getUniqueId();
        if(scl.getSync_key()==null){
            scl.setSync_key(key);
        }
        if (uniqueId != null) {
            schoolRef.child(uniqueId).setValue(scl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            long rr = updateSchool(scl);
                            if (rr!=-1){
                                Log.e("UserFound","school data Successfully Update In Local & Online");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failed update
                            Log.e("schoolError","School data not Updated!");
                        }
                    });
        }else {
            Log.e("schoolError","school data not found");
        }

    }

    public void updateSchoolData(School scl) {
        DatabaseReference schoolRef = FirebaseDatabase.getInstance().getReference("school");

        Query query = schoolRef.orderByChild("sId").equalTo(scl.getsId());
        String key = schoolRef.push().getKey();
        if(scl.getSync_key()==null){
            scl.setSync_key(key);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    DatabaseReference schoolChildRef = childSnapshot.getRef();

                    schoolChildRef.setValue(scl)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    long rr = updateSchool(scl);
                                    if (rr!=-1){
                                        Log.e("UserFound","school data Successfully Update In Local & Online");
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Log.e("UpdateError", "School data update failed in Firebase: " + e.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the query cancellation or failure
                Log.e("QueryError", "Query to find matching school data canceled or failed: " + databaseError.getMessage());
            }
        });
    }


    @Override
    public void onSchoolRetrieved(School school) {

    }

    @Override
    public void onSchoolNotFound() {

    }
}