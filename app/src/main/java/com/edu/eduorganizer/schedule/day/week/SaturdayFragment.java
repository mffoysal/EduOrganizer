package com.edu.eduorganizer.schedule.day.week;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.routine.ClassScheduleItem;
import com.edu.eduorganizer.routine.Routine;
import com.edu.eduorganizer.schedule.ScheduleItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SaturdayFragment extends Fragment {
    private ScheduleItem scheduleItem;
    private Routine routine;

    public SaturdayFragment(ScheduleItem scheduleItem) {
        this.scheduleItem = scheduleItem;
        this.routine = routine;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saturday, container, false);

        TextView dateTextView = rootView.findViewById(R.id.dateTextView);
        dateTextView.setText(getFormattedDate(7)); // Pass the day of the week (2 for Monday) to get the date

        return rootView;
    }

    private String getFormattedDate(int dayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
