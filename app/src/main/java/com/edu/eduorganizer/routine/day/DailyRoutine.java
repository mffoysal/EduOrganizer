package com.edu.eduorganizer.routine.day;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.routine.day.week.FridayFragment;
import com.edu.eduorganizer.routine.day.week.MondayFragment;
import com.edu.eduorganizer.routine.day.week.SaturdayFragment;
import com.edu.eduorganizer.routine.day.week.SundayFragment;
import com.edu.eduorganizer.routine.day.week.ThursdayFragment;
import com.edu.eduorganizer.routine.day.week.TuesdayFragment;
import com.edu.eduorganizer.routine.day.week.WednesdayFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyRoutine extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_routine);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int defaultTabIndex = currentDayOfWeek - 1;
        viewPager.setCurrentItem(defaultTabIndex);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MondayFragment(), getDayName(1));
        adapter.addFragment(new TuesdayFragment(), getDayName(2));
        adapter.addFragment(new WednesdayFragment(), getDayName(3));
        adapter.addFragment(new ThursdayFragment(), getDayName(4));
        adapter.addFragment(new FridayFragment(), getDayName(5));
        adapter.addFragment(new SaturdayFragment(), getDayName(6));
        adapter.addFragment(new SundayFragment(), getDayName(7));
        viewPager.setAdapter(adapter);
    }

    private String getDayName(int dayOfWeek) {
        String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return dayNames[dayOfWeek - 1];
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
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
