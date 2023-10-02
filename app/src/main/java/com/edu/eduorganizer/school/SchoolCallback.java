package com.edu.eduorganizer.school;

public interface SchoolCallback {
    void onSchoolRetrieved(School school);
    void onSchoolNotFound();
}
