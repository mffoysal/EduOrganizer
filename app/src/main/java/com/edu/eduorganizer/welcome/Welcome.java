package com.edu.eduorganizer.welcome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.login.ui.login.LoginActivity;
import com.edu.eduorganizer.start.Start;
import com.edu.eduorganizer.start.ViewOne;

public class Welcome extends BaseMenu implements View.OnClickListener {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Set the status bar color
            getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
        }

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                intent = new Intent(getApplicationContext(), LoginActivity.class);
//                intent = new Intent(getApplicationContext(), Start.class);
                intent = new Intent(getApplicationContext(), ViewOne.class);
                startActivity(intent);
                finish();
            }
        }, 2000);


    }

    @Override
    public void onClick(View view) {

    }
}