package com.edu.eduorganizer.schedule;


public interface ScheduleCallback {
    void onUserRetrieved(ScheduleItem user);
    void onUserNotFound();
}
