package com.edu.eduorganizer.home;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.edu.eduorganizer.R;

public class Preferences extends PreferenceActivity {
    public static final String KEY_TIMER1 = "timer_1";
    public static final String KEY_TIMER2 = "timer_2";
    public static final String KEY_TIMER3 = "timer_3";
    public static final String KEY_TIMER4 = "timer_4";
    public static final String KEY_TIMER5 = "timer_5";
    public static final String KEY_TIMER6 = "timer_6";
    public static final String KEY_TIMER7 = "timer_7";
    public static final String KEY_TIMER8 = "timer_8";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferrences);
    }
}
