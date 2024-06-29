package com.edu.eduorganizer.routine;


import com.edu.eduorganizer.schedule.ScheduleItem;

public interface RoutineCallback {
    void onUserRetrieved(Routine routine);
    void onUserNotFound();
}
