package com.edu.eduorganizer.user;


import com.edu.eduorganizer.User;

public interface UserCallback {
    void onUserRetrieved(User user);
    void onUserNotFound();
}
