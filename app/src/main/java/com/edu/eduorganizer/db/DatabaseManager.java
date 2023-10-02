package com.edu.eduorganizer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    public DatabaseManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void openDatabase() {
        database = databaseHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        if (database != null) {
            database.close();
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}