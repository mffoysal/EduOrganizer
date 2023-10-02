package com.edu.eduorganizer.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.login.Login;
import com.edu.eduorganizer.login.LoginPage;

public class GetStart extends AppCompatActivity {

    Button startButton;
    private Logout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);

        logout = new Logout(getApplicationContext());
        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout.setInstaller(true);
                Intent i = new Intent(GetStart.this, LoginPage.class);
                startActivity(i);
                finish();
            }
        });

    }
}