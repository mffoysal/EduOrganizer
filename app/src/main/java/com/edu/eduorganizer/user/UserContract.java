package com.edu.eduorganizer.user;

import android.provider.BaseColumns;

public class UserContract {
    private UserContract() {
        // Private constructor to prevent instantiation
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_USERS = "users";

        public static final String COLUMN_SYNC_STATUS = "sync_status";

        // Sync status values
        public static final int SYNC_STATUS_FAILED = 0;
        public static final int SYNC_STATUS_SUCCESS = 1;
    }
}
