package com.edu.eduorganizer.sign;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginMainActivity;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Connection;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.panel.UserPanel;
import com.edu.eduorganizer.school.createSchool;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class createUser extends AppCompatActivity{

    private Logout logout;
    private SQLiteDatabase database;
    private DatabaseManager databaseManager;
    private UserDAO userDAO;
    private Internet internet;
    private FirebaseAuth mAuth;
    private BroadcastReceiver connectivityReceiver;
    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;
    private FirebaseAuth auth;

    private FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference = fdatabase.getReference("users");

    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;
    private EditText signupPhone;
    private Intent intent;
    private UserCallback userCallback;
    private DatabaseReference usersRef;
    private User user;
    private MaterialButton goToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_user);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        internet = new Internet(getApplicationContext());
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(Network.ACTION_CONNECTIVITY_CHANGE)) {
                    boolean isConnected = intent.getBooleanExtra(Network.EXTRA_CONNECTIVITY_STATUS, false);
                    if (isConnected) {

//                        Toast.makeText(getApplicationContext(),"Internet Connected"+new Admin().uId()+"  "+new Admin().userId(),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        };

        goToLogin = findViewById(R.id.goToAdminPanelId);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finish();
            }
        });

        databaseManager = new DatabaseManager(this);
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();
//        UserDAO userDAO = new UserDAO(database);
//        userDAO.insertUser(user);
//        List<User> userList = userDAO.getAllUsers();
//        int rowsAffected = userDAO.updateUser(user);
//        int rowsDeleted = userDAO.deleteUser(userId);
//        databaseManager.closeDatabase();
        logout = new Logout(getApplicationContext());

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        userCallback = new UserCallback() {
            @Override
            public void onUserRetrieved(User user) {
                // Handle the retrieved user object here
                // For example, update the UI or pass it to another method
                processUser(user);
            }

            @Override
            public void onUserNotFound() {
                // Handle the case where no user matches the given phone number
                // For example, display an error message or take appropriate action
                handleUserNotFound();
            }
        };

        
        auth = FirebaseAuth.getInstance();
        signupPhone = findViewById(R.id.signup_phone);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPhone = signupPhone.getText().toString().trim();
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                if (userPhone.isEmpty()){
                    signupPhone.setError("Phone cannot be empty");
                }
                if (user.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                } else{

                    if (internet.isInternetConnection()){
                        checkUser(userPhone,user,pass);
//                        createFirebaseUserWithRealtime(userPhone,user,pass);
                    }else {
                        createUserInLocal(userPhone,user,pass);
                    }

                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(createUser.this, LoginMainActivity.class));
            }
        });
    }

    private void handleUserNotFound() {
    }

    private void processUser(User u) {
        user = u;
        logout.saveUser(user);
        Log.e("UserFound","User  save from login "+logout.getUser().getPhone());
        startActivity(intent);
        finish();
    }

    private void createUserInLocal(String userPhone, String user, String pass) {

        User user1 = new User();
        user1.setUserId(new Unique().userId());
        user1.setPhone(userPhone);
//        user1.setId(new Admin().uniqueId());
        user1.setPass(pass);
        user1.setEmail(user);
        long rs = new UserDAO(database).insertUser(user1);
        if(rs==-1){
            Toast.makeText(this,"Data Not inserted! Try again!  "+rs+"  ",Toast.LENGTH_SHORT).show();

        }else if(rs==-3){
            Toast.makeText(this,"User already created! Try again!  "+rs+"  ",Toast.LENGTH_SHORT).show();

        }else if(rs==-2){
            Toast.makeText(this,"Error Occurred! Try again!  "+rs+"  ",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this,"User Successfully created in locally  "+rs+"  ",Toast.LENGTH_SHORT).show();
            logout.setLoggedUser(userPhone,userPhone);
            intent = new Intent(createUser.this, createSchool.class);
            intent.putExtra("user",userPhone);

            getUserData(userPhone);

            if (!internet.isInternetConnection()){
                user1 = new UserDAO(database).getUser(userPhone);
                logout.saveUser(user1);
                Log.e("UserFound","User  save from login "+logout.getUser().getPhone());
                startActivity(intent);
                finish();
            }
        }
    }
    private void createUserInOnline(String userPhone, String user, String pass) {

        User user1 = new User();
        user1.setUserId(new Unique().userId());
        user1.setPhone(userPhone);
        user1.setPass(pass);
        user1.setId(new Unique().uniqueId());
        user1.setEmail(user);
        user1.setSId(null);
        new UserDAO(database).saveUserInformationWithRealtime(new Unique().userId(),user1);

    }

    private void createFirebaseUserWithRealtime(String userPhone, String user, String pass) {

        auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(createUser.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                        createUserInOnline(userPhone,user,pass);
                        createUserInLocal(userPhone,user,pass);
                        logout.setLoggedUser(userPhone,userPhone);
                        userDAO = new UserDAO(database);
//                        logout.saveUser(userDAO.getUser(userPhone));
//                        startActivity(new Intent(createUser.this, createSchool.class));
//                        finish();

                } else {
                    Toast.makeText(createUser.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(Connection.ACTION_CONNECTIVITY_CHANGE);
        registerReceiver(connectivityReceiver,intentFilter);
        Network network = new Network();
        network.onReceive(getApplicationContext(),null);
//        Toast.makeText(this,"onStart"
//        ,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityReceiver);
//        Toast.makeText(this,"onStop",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Toast.makeText(this,"onRestart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this,"onDestroy",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void checkUser(String userPhone,String user, String pass) {
        String userUsername = signupPhone.getText().toString().trim();
        String userPassword = signupPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("phone").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    signupPhone.setError(null);
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Retrieve the child data using the userSnapshot
                        String passwordFromDB = userSnapshot.child("pass").getValue(String.class);
                        String emailFromDB = userSnapshot.child("email").getValue(String.class);
                        String uIdFromDB = userSnapshot.child("userId").getValue(String.class);
                        String sIdFromDB = userSnapshot.child("sId").getValue(String.class);
                        String stdIdFromDB = userSnapshot.child("stdId").getValue(String.class);
                        String stdNameFromDB = userSnapshot.child("stdName").getValue(String.class);

//                        Toast.makeText(getApplicationContext(), "" + sIdFromDB, Toast.LENGTH_SHORT).show();

                        // Perform actions with the retrieved data
                        if (passwordFromDB.equals(userPassword)) {
                            // Valid credentials
                            signupPhone.setError(null);

                            if (sIdFromDB == null) {
                                intent = new Intent(getApplicationContext(), createSchool.class);
                            } else {
                                intent = new Intent(getApplicationContext(), UserPanel.class);
                                intent.putExtra("sId", sIdFromDB);
                                logout.setLoggedIn(true);
                                intent.putExtra("user",userUsername);
                                logout.setLoggedUser(userUsername,userUsername);
                                logout.saveUser(userDAO.getUser(userUsername));
                            }

                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("pass", passwordFromDB);
                            intent.putExtra("uId", uIdFromDB);
                            intent.putExtra("stdId", stdIdFromDB);
                            intent.putExtra("stdName", stdNameFromDB);

                            startActivity(intent);
                            finish();
                            return; // Exit the loop and method since the user is found
                        }
                    }
                    // Invalid password
                    signupPassword.setError("User already Exist, but Invalid Credentials");
                    signupPassword.requestFocus();
                } else {
                    createFirebaseUserWithRealtime(userPhone,user,pass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the cancellation
            }
        });
    }

    public User getUserData(String userPhone) {
        Query query = usersRef.orderByChild("phone").equalTo(userPhone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User use = userSnapshot.getValue(User.class);
                    if (use != null) {
                        // User found based on the phone number
                        // Pass the user object to the callback
//                        Log.e("UserFound","User FF Found");
                        user = use;
                        userCallback.onUserRetrieved(use);
                        return;
                    }
                }
                // User not found based on the phone number
                userCallback.onUserNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });

        // Return a placeholder user object or null if needed
        return new User();
    }

}