package com.edu.eduorganizer.routine;

import android.net.Uri;
import android.provider.BaseColumns;

public class RoutineCo {
    public static class RoutineEntry implements BaseColumns {
        public static final String TABLE_NAME = "routine";

        public static final String COLUMN_SYNC_STATUS = "sync_status";
        public static final String COLUMN_SYNC_KEY = "sync_key";

        // Sync status values
        public static final int SYNC_STATUS_FAILED = 0;
        public static final int SYNC_STATUS_SUCCESS = 1;
        public static final String COLUMN_UNIQUE_ID = "uniqueId";

        public static final String COLUMN_TEMP_DETAILS = "temp_details";

        public static final String COLUMN_TEMP_NUM = "temp_num";
        public static final String COLUMN_TEMP_NAME = "temp_name";
        public static final String COLUMN_TEMP_CODE = "temp_code";

        // Define the authority for your content provider
        public static final String AUTHORITY = "com.edubox.admin.routine";

        // Define the base content URI
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

        // Define the path for your specific data
        public static final String PATH_SCHEDULE = "routine";

        // Combine the base content URI with the path to create the full content URI
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SCHEDULE)
                .build();

    }
}
