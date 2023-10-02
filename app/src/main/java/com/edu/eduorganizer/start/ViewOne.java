package com.edu.eduorganizer.start;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.login.LoginMainActivity;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.login.ui.login.LoginActivity;
import com.edu.eduorganizer.panel.MainPanel;
import com.edu.eduorganizer.panel.PanelUser;
import com.edu.eduorganizer.panel.UserPanel;
import com.edu.eduorganizer.splash.GetStart;
import com.edu.eduorganizer.onboard.ViewPagerAdapter;

public class ViewOne extends AppCompatActivity {

    ViewPager slideViewPager;
    LinearLayout dotIndicator;
    Button backButton, nextButton, skipButton;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;


    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {

            setDotIndicator(position);

            if (position > 0) {
                backButton.setVisibility(View.VISIBLE);
            } else {
                backButton.setVisibility(View.INVISIBLE);
            }
            if (position == 2){
                nextButton.setText("Finish");
            } else {
                nextButton.setText("Next");
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private Logout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one);

        logout = new Logout(getApplicationContext());
        if (logout.isInstalled()){
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

//            Intent intent = new Intent(getApplicationContext(), createSchool.class);

//            Intent intent = new Intent(getApplicationContext(), FM.class);

//            Intent intent = new Intent(getApplicationContext(), AllDepartment.class);
//            Intent intent = new Intent(getApplicationContext(), DepartmentPanel.class);

//            Intent intent = new Intent(getApplicationContext(), LoginMainActivity.class);
//            Intent intent = new Intent(getApplicationContext(), MainPanel.class);
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
//            Intent intent = new Intent(getApplicationContext(), UserPanel.class);
//            Intent intent = new Intent(getApplicationContext(), PanelUser.class);

//            Intent intent = new Intent(getApplicationContext(), RoutinMain.class);
//            Intent intent = new Intent(getApplicationContext(), DailyRoutine.class);
//            Intent intent = new Intent(getApplicationContext(), MonthlyRoutine.class);
//            Intent intent = new Intent(getApplicationContext(), CommonAllStudentPanel.class);

//            Intent intent = new Intent(getApplicationContext(), AllSession.class);
//            Intent intent = new Intent(getApplicationContext(), AddStudentPanel.class);
//            Intent intent = new Intent(getApplicationContext(), StudentPanel.class);
//            Intent intent = new Intent(getApplicationContext(), LoginUser.class);
//            Intent intent = new Intent(getApplicationContext(), StudentDashboard.class);
//            Intent intent = new Intent(getApplicationContext(), StudentProfilePanel.class);
//            Intent intent = new Intent(getApplicationContext(), GenerateClasses.class);
            startActivity(intent);
            finish();
        }

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(0) > 0) {
                    slideViewPager.setCurrentItem(getItem(-1), true);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(0) < 2)
                    slideViewPager.setCurrentItem(getItem(1), true);
                else {
                    Intent i = new Intent(getApplicationContext(), GetStart.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setInstaller(true);
                Intent i = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(i);
                finish();
            }
        });

        slideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        dotIndicator = (LinearLayout) findViewById(R.id.dotIndicator);

        viewPagerAdapter = new ViewPagerAdapter(this);
        slideViewPager.setAdapter(viewPagerAdapter);

        setDotIndicator(0);
        slideViewPager.addOnPageChangeListener(viewPagerListener);
    }

    public void setDotIndicator(int position) {

        dots = new TextView[3];
        dotIndicator.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].setText(Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY));
            }
            dots[i].setTextSize(35);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dots[i].setTextColor(getResources().getColor(R.color.gray, getApplicationContext().getTheme()));
            }
            dotIndicator.addView(dots[i]);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dots[position].setTextColor(getResources().getColor(R.color.lavender, getApplicationContext().getTheme()));
        }
    }

    private int getItem(int i) {
        return slideViewPager.getCurrentItem() + i;
    }
}
