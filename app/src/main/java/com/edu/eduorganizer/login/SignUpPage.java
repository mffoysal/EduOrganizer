package com.edu.eduorganizer.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.user.UserCallback;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {


    private Logout logout;
    private SQLiteDatabase database;
    private DatabaseManager databaseManager;

    private Internet internet;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference = fdatabase.getReference("users");

    private DatabaseReference usersRef;
    private UserCallback userCallback;
    private BroadcastReceiver connectivityReceiver;
    private Intent intent;


    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;
    private FirebaseAuth auth;
    TextView forgotPassword;

    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    private User user;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private LoginPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
    }
}