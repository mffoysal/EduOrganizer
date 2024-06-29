package com.edu.eduorganizer.routine;


import com.edu.eduorganizer.schedule.ScheduleItem;

public interface CScheduleCallback {
    void onUserRetrieved(ClassScheduleItem user);
    void onUserNotFound();
}
