package com.edu.eduorganizer.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.login.ui.login.LoginActivity;

public class WelcomeScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private int setp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_screen);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        progressBar = findViewById(R.id.welcomeProgress);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
            }
        });
        thread.start();

    }

    private void startApp() {
        Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void doWork() {
        for (setp=20; setp<=100; setp = setp+20){
            try {
                Thread.sleep(1000);
                progressBar.setProgress(setp);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}