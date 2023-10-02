package com.edu.eduorganizer.routine;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import com.edu.eduorganizer.db.DatabaseHelper;
import com.edu.eduorganizer.bubble.ScheduleContract;

public class ScheduleProvider extends ContentProvider {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;

    // Define your URI matcher
    private static final int SCHEDULE_ITEMS = 100;
    private static final int SCHEDULE_ITEM_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(ScheduleContract.ScheduleEntry.AUTHORITY, ScheduleContract.ScheduleEntry.PATH_SCHEDULE, SCHEDULE_ITEMS);
        uriMatcher.addURI(ScheduleContract.ScheduleEntry.AUTHORITY, ScheduleContract.ScheduleEntry.PATH_SCHEDULE + "/#", SCHEDULE_ITEM_ID);
    }

    private Context context;
    private ContentValues appWidgetManager;
    private Bundle appWidgetIds;


    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();



        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case SCHEDULE_ITEMS:
                cursor = db.query(ScheduleContract.ScheduleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SCHEDULE_ITEM_ID:
                // Handle individual item query
                // ...
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(ScheduleContract.ScheduleEntry.TABLE_NAME, null, values);
        if (id == -1) {
            throw new SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = db.update(ScheduleContract.ScheduleEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = db.delete(ScheduleContract.ScheduleEntry.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
