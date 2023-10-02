package com.edu.eduorganizer.bubble;

import android.net.Uri;
import android.provider.BaseColumns;

public class ScheduleContract {
    public static class ScheduleEntry implements BaseColumns {
        public static final String TABLE_NAME = "classSchedule";

        public static final String COLUMN_SYNC_STATUS = "sync_status";
        public static final String COLUMN_SYNC_KEY = "sync_key";

        // Sync status values
        public static final int SYNC_STATUS_FAILED = 0;
        public static final int SYNC_STATUS_SUCCESS = 1;
        public static final String COLUMN_TIME = "dateTime";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_ROOM = "room";
        public static final String COLUMN_SECTION = "section";
        public static final String COLUMN_T_NAME = "t_name";
        public static final String COLUMN_SUBJECT = "sub_name";
        public static final String COLUMN_SUBJECT_CODE = "sub_code";
        public static final String COLUMN_DAY = "day";
        // Define the authority for your content provider
        public static final String AUTHORITY = "com.edubox.admin.bubble";

        // Define the base content URI
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

        // Define the path for your specific data
        public static final String PATH_SCHEDULE = "schedule";

        // Combine the base content URI with the path to create the full content URI
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SCHEDULE)
                .build();

    }
}
