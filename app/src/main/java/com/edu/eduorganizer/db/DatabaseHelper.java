package com.edu.eduorganizer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eduboxDB.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_PATH = "data/data/com.example.database/databases/";
    private Context context;

    public static final String A_YEAR =
            "CREATE TABLE IF NOT EXISTS aYear (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uId TEXT," +
                    "aYname TEXT," +
                    "uniqueId TEXT," +
                    "sYear TEXT," +
                    "sMonth TEXT," +
                    "eYear TEXT," +
                    "eMonth TEXT," +
                    "aStatus INTEGER," +
                    "sId TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String SECTIONS =
            "CREATE TABLE IF NOT EXISTS sections (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sId TEXT," +
                    "uniqueId TEXT," +
                    "subId TEXT," +
                    "sessionId TEXT," +
                    "clsId TEXT," +
                    "secName TEXT," +
                    "secCode TEXT," +
                    "secFee TEXT," +
                    "secNumStd INTEGER," +
                    "secTeaId TEXT," +
                    "aStatus INTEGER," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String SCHOOL =
            "CREATE TABLE IF NOT EXISTS school (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sName TEXT," +
                    "uniqueId TEXT," +
                    "currSessId TEXT," +
                    "sPhone TEXT," +
                    "sPass TEXT," +
                    "sEmail TEXT," +
                    "sLogo TEXT," +
                    "sId TEXT," +
                    "sAdrs TEXT," +
                    "sEiin TEXT," +
                    "sStudent INTEGER," +
                    "sTeacher INTEGER," +
                    "sCourse INTEGER," +
                    "sSec INTEGER," +
                    "sUser INTEGER," +
                    "sClass INTEGER," +
                    "sItp1 TEXT," +
                    "sItp2 TEXT," +
                    "sItEmail TEXT," +
                    "sWeb TEXT," +
                    "sFundsBal TEXT," +
                    "sFundsBank TEXT," +
                    "sFundsAN TEXT," +
                    "sActivate INTEGER," +
                    "sVerification TEXT," +
                    "sEmpl INTEGER," +
                    "proPic BLOB," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String MAJOR =
            "CREATE TABLE IF NOT EXISTS Major (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sId TEXT," +
                    "uniqueId TEXT," +
                    "location TEXT," +
                    "deanId TEXT," +
                    "phone TEXT," +
                    "mName TEXT," +
                    "mStart TEXT," +
                    "mEnd TEXT," +
                    "mStatus INTEGER," +
                    "currentId TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String STUDENTS =
            "CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sId TEXT," +
                    "studentId TEXT," +
                    "uniqueId TEXT," +
                    "currSessId TEXT," +
                    "stdId TEXT," +
                    "uId TEXT," +
                    "stdName TEXT," +
                    "nidBirth TEXT," +
                    "stdPhone TEXT," +
                    "stdEmail TEXT," +
                    "homePhone TEXT," +
                    "stdReligion TEXT," +
                    "dob TEXT," +
                    "address TEXT," +
                    "country TEXT," +
                    "UnionWord TEXT," +
                    "aStatus INTEGER," +
                    "fatherName TEXT," +
                    "motherName TEXT," +
                    "fNid TEXT," +
                    "mNid TEXT," +
                    "gName TEXT," +
                    "gAddress TEXT," +
                    "gPhone TEXT," +
                    "gEmail TEXT," +
                    "stdImg TEXT," +
                    "sMajor TEXT," +
                    "stdPass TEXT," +
                    "gender TEXT," +
                    "addDate TEXT," +
                    "proPic BLOB," +
                    "lastlogin TEXT," +
                    "sMajorId TEXT," +
                    "sync_status INTEGER," +
                    "program INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String SUBJECT =
            "CREATE TABLE IF NOT EXISTS subject (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subName TEXT," +
                    "uniqueId TEXT," +
                    "credit TEXT," +
                    "subCode TEXT," +
                    "subId TEXT," +
                    "depId INTEGER," +
                    "typeId INTEGER," +
                    "subFee TEXT," +
                    "status INTEGER," +
                    "sId TEXT," +
                    "semester TEXT," +
                    "program INTEGER," +
                    "departmentId TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String SUB_ON_SEC =
            "CREATE TABLE IF NOT EXISTS subOnsec (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subAId TEXT," +
                    "uniqueId TEXT," +
                    "sId TEXT," +
                    "subId INTEGER," +
                    "subjectId TEXT," +
                    "sectionId TEXT," +
                    "secId INTEGER," +
                    "aStatus INTEGER," +
                    "subFee TEXT," +
                    "secFee TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String SUB_ASSIGNED =
            "CREATE TABLE IF NOT EXISTS subAssigned (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subId INTEGER," +
                    "subjectId TEXT," +
                    "uniqueId TEXT," +
                    "secId INTEGER," +
                    "sectionId TEXT," +
                    "clsId INTEGER," +
                    "classId TEXT," +
                    "sId TEXT," +
                    "stdId TEXT," +
                    "feeTk TEXT," +
                    "aStatus INTEGER," +
                    "subAId TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String SEC_ASSIGNED =
            "CREATE TABLE IF NOT EXISTS secAssigned (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "secId INTEGER," +
                    "uniqueId TEXT," +
                    "clsId INTEGER," +
                    "stdId TEXT," +
                    "sectionId TEXT," +
                    "sessionId TEXT," +
                    "classId TEXT," +
                    "sId TEXT," +
                    "date TEXT," +
                    "aYear INTEGER," +
                    "aStatus INTEGER," +
                    "feeTk TEXT," +
                    "secAId TEXT," +
                    "aYearId INTEGER," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String TEACHER =
            "CREATE TABLE IF NOT EXISTS teacher (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sId TEXT," +
                    "uniqueId TEXT," +
                    "designation TEXT," +
                    "tName TEXT," +
                    "tPhone TEXT," +
                    "tPass TEXT," +
                    "tEmail TEXT," +
                    "tAddress TEXT," +
                    "aStatus INTEGER," +
                    "tMajor TEXT," +
                    "tBal TEXT," +
                    "tLogo TEXT," +
                    "tId TEXT," +
                    "uType INTEGER," +
                    "proPic BLOB," +
                    "nidBirth TEXT," +
                    "uId TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String ATTENDANCE_SHEET =
            "CREATE TABLE IF NOT EXISTS attendanceSheet (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sId TEXT," +
                    "uniqueId TEXT," +
                    "stdId TEXT," +
                    "subId INTEGER," +
                    "subjectId TEXT," +
                    "secId INTEGER," +
                    "sectionId TEXT," +
                    "clsId TEXT," +
                    "teacherId TEXT," +
                    "date TEXT," +
                    "time TEXT," +
                    "status INTEGER," +
                    "attendance TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String PAYMENT =
            "CREATE TABLE IF NOT EXISTS payment (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "paymentId INTEGER," +
                    "sessionId INTEGER," +
                    "sessionTextId TEXT," +
                    "fId INTEGER," +
                    "uniqueId TEXT," +
                    "payMethodType INTEGER," +
                    "feeType INTEGER," +
                    "sId TEXT," +
                    "stdId TEXT," +
                    "feeId TEXT," +
                    "date TEXT," +
                    "time TEXT," +
                    "trxId TEXT," +
                    "payAmount TEXT," +
                    "payMethod TEXT," +
                    "register TEXT," +
                    "phone TEXT," +
                    "onlinePhone TEXT," +
                    "onlineTrxId TEXT," +
                    "bankA TEXT," +
                    "bankName TEXT," +
                    "depositer TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String FEE_TYPE =
            "CREATE TABLE IF NOT EXISTS feeType (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "typeId INTEGER," +
                    "sessionId INTEGER," +
                    "sessionTextId TEXT," +
                    "uniqueId TEXT," +
                    "depId INTEGER," +
                    "subId INTEGER," +
                    "secId INTEGER," +
                    "clsId INTEGER," +
                    "status INTEGER," +
                    "discount INTEGER," +
                    "typeName TEXT," +
                    "sId TEXT," +
                    "Amount TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String MAIL =
            "CREATE TABLE IF NOT EXISTS mail (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sendId TEXT," +
                    "uniqueId TEXT," +
                    "sendMail TEXT," +
                    "recId TEXT," +
                    "recMail TEXT," +
                    "sendDate TEXT," +
                    "sendTime TEXT," +
                    "deliverDate TEXT," +
                    "deliverTime TEXT," +
                    "recDate TEXT," +
                    "recTime TEXT," +
                    "sub TEXT," +
                    "msg TEXT," +
                    "file TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String FEE =
            "CREATE TABLE IF NOT EXISTS fee (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fee_type INTEGER," +
                    "status INTEGER," +
                    "uniqueId TEXT," +
                    "scholarshipStatus INTEGER," +
                    "payStatus INTEGER," +
                    "sessionId INTEGER," +
                    "depId INTEGER," +
                    "clsId INTEGER," +
                    "secId INTEGER," +
                    "subId INTEGER," +
                    "discountStatus INTEGER," +
                    "discount INTEGER," +
                    "payMethod TEXT," +
                    "feeId TEXT," +
                    "feeDetails TEXT," +
                    "fee TEXT," +
                    "sId TEXT," +
                    "date TEXT," +
                    "time TEXT," +
                    "trxId TEXT," +
                    "disAmount TEXT," +
                    "stdId TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String CLASSES =
            "CREATE TABLE IF NOT EXISTS classes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "clsId TEXT," +
                    "maxSec INTEGER," +
                    "uniqueId TEXT," +
                    "sId TEXT," +
                    "clsName TEXT," +
                    "clsCode TEXT," +
                    "aYear TEXT," +
                    "aStatus INTEGER," +
                    "aYearId INTEGER," +
                    "sessionId TEXT," +
                    "depId INTEGER," +
                    "program INTEGER," +
                    "departmentId TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";


    public static final String CLASS_SCHEDULE =
            "CREATE TABLE IF NOT EXISTS classSchedule (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "temp_code TEXT," +
                    "min INTEGER," +
                    "uniqueId TEXT," +
                    "sId TEXT," +
                    "stdId TEXT," +
                    "temp_num TEXT," +
                    "tId TEXT," +
                    "aStatus INTEGER," +
                    "sub_name TEXT," +
                    "sub_code TEXT," +
                    "t_id TEXT," +
                    "t_name TEXT," +
                    "room TEXT," +
                    "campus TEXT," +
                    "day TEXT," +
                    "dateTime TEXT," +
                    "start_time TEXT," +
                    "end_time TEXT," +
                    "section TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";


    public static final String SCHEDULE =
            "CREATE TABLE IF NOT EXISTS schedule (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "temp_code TEXT," +
                    "min INTEGER," +
                    "uniqueId TEXT," +
                    "sId TEXT," +
                    "stdId TEXT," +
                    "temp_num TEXT," +
                    "tId TEXT," +
                    "aStatus INTEGER," +
                    "sub_name TEXT," +
                    "sub_code TEXT," +
                    "t_id TEXT," +
                    "t_name TEXT," +
                    "room TEXT," +
                    "campus TEXT," +
                    "day TEXT," +
                    "dateTime TEXT," +
                    "start_time TEXT," +
                    "end_time TEXT," +
                    "section TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String TASK =
            "CREATE TABLE IF NOT EXISTS task (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "task_name TEXT," +
                    "uniqueId TEXT," +
                    "sId TEXT," +
                    "stdId TEXT," +
                    "uId TEXT," +
                    "user_id TEXT," +
                    "aStatus INTEGER," +
                    "task_details TEXT," +
                    "task_location TEXT," +
                    "task_id TEXT," +
                    "calendar DATETIME," +
                    "time TEXT," +
                    "day TEXT," +
                    "dateTime TEXT," +
                    "done INTEGER, "+
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String NOTE =
            "CREATE TABLE IF NOT EXISTS NOTE (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "task_name TEXT," +
                    "uniqueId TEXT," +
                    "sId TEXT," +
                    "stdId TEXT," +
                    "uId TEXT," +
                    "user_id TEXT," +
                    "aStatus INTEGER," +
                    "task_details TEXT," +
                    "task_location TEXT," +
                    "task_id TEXT," +
                    "calendar DATETIME," +
                    "time TIME," +
                    "day TEXT," +
                    "dateTime TEXT," +
                    "done INTEGER, "+
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";
    public static final String ALL_SUBJECTS =
            "CREATE TABLE IF NOT EXISTS allSubjects (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subName TEXT," +
                    "uniqueId TEXT," +
                    "credit TEXT," +
                    "subCode TEXT," +
                    "subId TEXT," +
                    "depId INTEGER," +
                    "typeId INTEGER," +
                    "subFee TEXT," +
                    "status INTEGER," +
                    "sId TEXT," +
                    "semester TEXT," +
                    "program INTEGER," +
                    "departmentId TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String ROUTINE =
            "CREATE TABLE IF NOT EXISTS ROUTINE (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "temp_name TEXT," +
                    "uniqueId TEXT," +
                    "sId TEXT," +
                    "stdId TEXT," +
                    "uId TEXT," +
                    "aStatus INTEGER," +
                    "temp_details TEXT," +
                    "temp_code TEXT," +
                    "tId TEXT," +
                    "temp_num TEXT, "+
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public static final String USERS =
            "CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "u_id TEXT," +
                    "uniqueId TEXT," +
                    "currSessId TEXT," +
                    "designation TEXT," +
                    "name TEXT," +
                    "email TEXT," +
                    "phone TEXT," +
                    "pass TEXT," +
                    "bal TEXT," +
                    "u_type INTEGER," +
                    "sId TEXT," +
                    "photo BLOB," +
                    "picture TEXT," +
                    "fingerData BLOB," +
                    "stdId TEXT," +
                    "major TEXT," +
                    "status INTEGER," +
                    "address TEXT," +
                    "sync_status INTEGER," +
                    "sync_key TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
//        Toast.makeText(context.getApplicationContext(), "hey!",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables and perform any initial setup here
        // Example:
        // db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, email TEXT)");



        try {
            db.execSQL(USERS);
            db.execSQL(A_YEAR);
            db.execSQL(SECTIONS);
            db.execSQL(SCHOOL);
            db.execSQL(MAJOR);
            db.execSQL(STUDENTS);
            db.execSQL(SUBJECT);
            db.execSQL(SUB_ON_SEC);
            db.execSQL(SUB_ASSIGNED);
            db.execSQL(SEC_ASSIGNED);
            db.execSQL(TEACHER);
            db.execSQL(ATTENDANCE_SHEET);
            db.execSQL(PAYMENT);
            db.execSQL(FEE_TYPE);
            db.execSQL(MAIL);
            db.execSQL(FEE);
            db.execSQL(CLASSES);
            db.execSQL(ALL_SUBJECTS);
            db.execSQL(CLASS_SCHEDULE);
            db.execSQL(SCHEDULE);
            db.execSQL(TASK);
            db.execSQL(NOTE);
            db.execSQL(ROUTINE);

//            this.copyDatabaseFromAssets();
            Toast.makeText(context.getApplicationContext(), "DataBase Created Successfully! Welcome to EdUBox!",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            this.copyDatabaseFromAssets();
            Toast.makeText(context.getApplicationContext(), "DataBase does not Created! Try Again EdUBox!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades here
        try {
            db.execSQL(USERS);
            db.execSQL(A_YEAR);
            db.execSQL(SECTIONS);
            db.execSQL(SCHOOL);
            db.execSQL(MAJOR);
            db.execSQL(STUDENTS);
            db.execSQL(SUBJECT);
            db.execSQL(SUB_ON_SEC);
            db.execSQL(SUB_ASSIGNED);
            db.execSQL(SEC_ASSIGNED);
            db.execSQL(TEACHER);
            db.execSQL(ATTENDANCE_SHEET);
            db.execSQL(PAYMENT);
            db.execSQL(FEE_TYPE);
            db.execSQL(MAIL);
            db.execSQL(FEE);
            db.execSQL(CLASSES);
            db.execSQL(ALL_SUBJECTS);
            db.execSQL(CLASS_SCHEDULE);
            db.execSQL(SCHEDULE);
            db.execSQL(TASK);
            db.execSQL(NOTE);
            db.execSQL(ROUTINE);

//            this.copyDatabaseFromAssets();
            Toast.makeText(context.getApplicationContext(), "DataBase Created Successfully! Welcome to EdUBox!",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context.getApplicationContext(), "DataBase does not Created! Try Again EdUBox!",Toast.LENGTH_SHORT).show();
        }
    }

    public void copyDatabaseFromAssets() {
        try {
            // Open your local db as the input stream
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);

            // Path to the just created empty db
            String outFileName = DATABASE_PATH + DATABASE_NAME;

//            String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();

            // Open the empty db as the output stream
            OutputStream outputStream = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[100024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close the streams
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
