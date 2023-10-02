package com.edu.eduorganizer.routine.month;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.edu.eduorganizer.R;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthlyRoutine extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_routine);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set the current day's tab as the default tab
        int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int defaultTabIndex = currentDayOfWeek - 1;
        viewPager.setCurrentItem(defaultTabIndex);

        // Add tabs for current day and the next 6 days
        for (int i = 0; i < 31; i++) {
            adapter.addFragment(DayFragment.newInstance(i), getFormattedDate(i));
        }
    }

    private String getFormattedDatee(int dayOffset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, dayOffset);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        return sdf.format(date);
    }
    private String getFormattedDate(int dayOffset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, dayOffset);
        Date date = cal.getTime();

        // Get the day name with three letters (e.g., Mon, Tue, etc.)
        SimpleDateFormat dayNameFormat = new SimpleDateFormat("E", Locale.getDefault());
        String dayName = dayNameFormat.format(date);

        // Get the day of the month with two digits (e.g., 01, 02, etc.)
        SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String dayOfMonth = dayOfMonthFormat.format(date);

        // Get the month with three letters (e.g., Jan, Feb, etc.)
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        String month = monthFormat.format(date);

        // Combine the three components and return the formatted date
        return dayName + ", " + dayOfMonth + " " + month;
    }

}

class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
        notifyDataSetChanged(); // Notify the adapter about the change
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}

