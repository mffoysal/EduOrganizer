package com.edu.eduorganizer.schedule.day.week;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.routine.ClassScheduleItem;
import com.edu.eduorganizer.routine.Routine;
import com.edu.eduorganizer.schedule.ScheduleItem;


public class EverydayFragment extends Fragment {

    private ScheduleItem scheduleItem;
    private Routine routine;

    public EverydayFragment(ScheduleItem scheduleItem) {
        this.scheduleItem = scheduleItem;
        this.routine = routine;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_everyday2, container, false);
    }
}