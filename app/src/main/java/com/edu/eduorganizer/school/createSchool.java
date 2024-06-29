package com.edu.eduorganizer.school;

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
import androidx.viewpager2.widget.ViewPager2;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.network.Connection;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.panel.UserPanel;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createSchool extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
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
    private DatabaseReference reference = fdatabase.getReference("school");

    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;
    private EditText signupPhone;

    private EditText editText, sname, semail, snumber, seiin, spass, sclAdrs, sT, sS, sC, sSec, sU, sClass, sItp1, sItp2, sItemail, sweb, sbalance, sbank, saccount, sverification;
    private Button button, sclSave;
    private School school;
    private Intent intent;
    private String sId;
    private User user;

    private String userPhone;
    private School s;
    private SchoolDAO schoolDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_school);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        databaseManager = new DatabaseManager(this);
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();

        logout = new Logout(this);
        intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            userPhone = intent.getStringExtra("user");
//            user = new UserDAO(database).getUser(userPhone);
            logout = new Logout(this);
            user = logout.getUser();
            Log.e("UserFound","User from shared "+user.getPhone());
        }else {
//            logout.setLoggedUser("015858550755","01816444372");
            logout = new Logout(getApplicationContext());
            logout.getOut();
            finish();
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
        schoolDAO = new SchoolDAO(database);
//        logout.saveUser(userDAO.getUser(userPhone));

        sname = findViewById(R.id.snameId);
        semail = findViewById(R.id.semailId);
        snumber = findViewById(R.id.sphoneId);
        spass = findViewById(R.id.spassId);
        seiin = findViewById(R.id.sEiinId);
        sclAdrs = findViewById(R.id.sadrsId);
        sItemail = findViewById(R.id.sItemailId);
        sItp1 = findViewById(R.id.sitphone1);
        sItp2 = findViewById(R.id.sitphone2);
        sweb = findViewById(R.id.sWebId);
        sbalance = findViewById(R.id.sfundBal);
        sbank = findViewById(R.id.sfundBank);
        saccount = findViewById(R.id.sBankAccount);
        sverification = findViewById(R.id.sVerification);
        sclSave = findViewById(R.id.saveSchoolButtonId);
        sclSave.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        String sclname = sname.getText().toString();
        String sclemail = semail.getText().toString().trim();
        String sclphone = snumber.getText().toString().trim();
        String sclpass = spass.getText().toString().trim();
        String scleiin = seiin.getText().toString().trim();
        String scladdress = sclAdrs.getText().toString();
        String sclItemail = sItemail.getText().toString();
        String sclItp1 = sItp1.getText().toString().trim();
        String sclItp2 = sItp1.getText().toString().trim();
        String sclweb = sweb.getText().toString().trim();
        String sclbal = sbalance.getText().toString().trim();
        String sclbank = sbank.getText().toString();
        String sclbankaccount = saccount.getText().toString().trim();
        String sclverfication = sverification.getText().toString().trim();

        if (view.getId()==R.id.saveSchoolButtonId){

            school = new School();
            school.setId(new Unique().uniqueId());
//            school.setsId(school.getId()+"-eduFF-"+new Admin().uniqueId());
            school.setsId("EduBox-"+school.getId()+"-"+sclphone);
            school.setsEmail(sclemail);
            school.setsName(sclname);
            school.setsPhone(sclphone);
            school.setsPass(sclpass);
            school.setUniqueId(new Unique().uId());
            school.setsItEmail(sclItemail);
            school.setsItp1(sclItp1);
            school.setsItp1(sclItp2);
            school.setsAdrs(scladdress);
            school.setsFundsBank(sclbank);
            school.setsFundsAN(sclbankaccount);
            school.setsFundsBal(sclbal);
            school.setsVerification(sclverfication);
            school.setsWeb(sclweb);
            school.setsEiin(scleiin);

            if (sclphone.isEmpty()){
                snumber.setError("Phone cannot be empty");
            }
            if (sclemail.isEmpty()){
                semail.setError("Email cannot be empty");
            }
            if (sclpass.isEmpty()){
                spass.setError("Password cannot be empty");
            } else{

                if (internet.isInternetConnection()){
                    checkSchool(school);
//                        createFirebaseUserWithRealtime(userPhone,user,pass);
                }else {
                    createSchooInLocal(school);
                }

            }
        }
    }

    private void createSchooInLocal(School school) {

        School s = new School();
        s = school;

        SchoolDAO userDAO = new SchoolDAO(database);
        int r=0;
        r = userDAO.checkSchool(s.getsPhone());
        if(r!=0){
            Toast.makeText(this,"School already created! Try again!  ",Toast.LENGTH_SHORT).show();
            Log.e("UserFound","School already have with this number");
        }
        long rs = userDAO.insertSchool(s);

        if(rs==-1){
            Toast.makeText(this,"Data Not inserted! Try again!  "+rs+"  ",Toast.LENGTH_SHORT).show();

        }else if(rs==-3){
            Toast.makeText(this,"User already created! Try again!  "+rs+"  ",Toast.LENGTH_SHORT).show();

        }else if(rs==-2){
            Toast.makeText(this,"Error occurred! Try again!  "+rs+"  ",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"School Data Successfully Inserted, Thanks!",Toast.LENGTH_SHORT).show();

            if (!internet.isInternetConnection()){
                UserDAO userd = new UserDAO(database);
                user.setSId(s.getsId());
                userd.updateUser(user);
            }

            logout.setLoggedIn(true);
            intent = new Intent(getApplicationContext(), UserPanel.class);
            intent.putExtra("user",user.getPhone());
            logout.setLoggedUser(user.getPhone(),user.getPhone());
            schoolDAO = new SchoolDAO(database);
            user = new UserDAO(database).getUser(userPhone);
            Log.e("school","us user phone  "+user.getPhone() );
            Log.e("school","us sid 1 "+user.getSId() );
            Log.e("school","us school sid 2 "+school.getsId() );
            startActivity(intent);
            finish();

        }

    }

    private void checkSchool(School s) {

        String userUsername = snumber.getText().toString().trim();
        String userPassword = spass.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("school");
        Query checkUserDatabase = reference.orderByChild("sPhone").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    snumber.setError(null);
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Retrieve the child data using the userSnapshot
                        String passwordFromDB = userSnapshot.child("sPass").getValue(String.class);
                        String emailFromDB = userSnapshot.child("sEmail").getValue(String.class);
                        String IdFromDB = userSnapshot.child("id").getValue(String.class);
                        String sIdFromDB = userSnapshot.child("sId").getValue(String.class);
                        String sEiinFromDB = userSnapshot.child("sEiin").getValue(String.class);
                        String sNameFromDB = userSnapshot.child("sName").getValue(String.class);

//                        Toast.makeText(getApplicationContext(), "" + sIdFromDB, Toast.LENGTH_SHORT).show();

                        // Perform actions with the retrieved data
                        if (passwordFromDB.equals(userPassword)) {
                            // Valid credentials
                            snumber.setError(null);

                            if (sIdFromDB == null) {
                                intent = new Intent(getApplicationContext(), createSchool.class);
                            } else {
                                intent = new Intent(getApplicationContext(), UserPanel.class);
                            }
                            userDAO = new UserDAO(database);
                            intent.putExtra("sId", sIdFromDB);
                            logout.setLoggedIn(true);
                            intent.putExtra("school",userUsername);
                            logout.setLoggedUser(userUsername,userUsername);
                            logout.saveUser(userDAO.getUser(userUsername));

                            intent.putExtra("sEmail", emailFromDB);
                            intent.putExtra("sPass", passwordFromDB);
                            intent.putExtra("uId", IdFromDB);
                            intent.putExtra("sEiin", sEiinFromDB);
                            intent.putExtra("sName", sNameFromDB);

                            startActivity(intent);
                            finish();
                            return; // Exit the loop and method since the user is found
                        }
                    }
                    // Invalid password
                    spass.setError("Invalid Credentials");
                    spass.requestFocus();
                } else {
                    saveSchoolInformationWithRealtime(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the cancellation
            }
        });

    }


    public void saveSchoolInformationWithRealtime(School school) {
        // Save additional user information in Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDatabase.getReference("school");

        String key = usersRef.push().getKey();

        String sId = school.getsId();

        s = new School();
        s = school;
//        s.setUniqueId(key);
        s.setSync_key(key);
        s.setSync_status(1);

        usersRef.child(sId)
                .setValue(s)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
                    user.setSId(s.getsId());
                    updateUserData(user,s);
                    createSchooInLocal(s);
                    Toast.makeText(getApplicationContext(), "School User created successfully", Toast.LENGTH_SHORT).show();
//                    finish(); // Finish sign-up activity and return to login screen
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
                    Toast.makeText(getApplicationContext(), "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserData(User u, School s) {
//        Log.e("UserFound","User data "+user.getPhone());
            userDAO = new UserDAO(database);
            user = u;
            logout = new Logout(this);
            logout.saveUser(u);
            logout.saveSchool(s);
            Log.e("UserSId","sid from update user, sid "+u.getSId()+"  "+s.getsId());
            userDAO.updateUserData(u,s);
    }

    private void saveSchoolInformation(String userId, String phoneNumber, String password, String sclName, String sclEmail) {
        // Save additional user information in Firestore or Realtime Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("school");

        Map<String, Object> user = new HashMap<>();
        user.put("sync_key", userId);
        user.put("id", new Unique().uniqueId());
        user.put("sync_status", 1);
        user.put("sPhone", phoneNumber);
        user.put("sPass", password);
        user.put("sName", sclName);
        user.put("sEmail", sclEmail);
        user.put("sId", sId);
        user.put("sLogo","");
        user.put("sAdrs","");
        user.put("sEiin","");
        user.put("sItp1","");
        user.put("sItp2","");
        user.put("sItEmail","");
        user.put("sWeb","");
        user.put("sFundsBal","");
        user.put("sFundsBank","");
        user.put("sFundsAN","");
        user.put("sActivate",0);
        user.put("sVerification","");
        user.put("sStudent",0);
        user.put("sTeacher",0);
        user.put("sClass",0);
        user.put("sUser",0);
        user.put("sSec",0);
        user.put("sCourse",0);
        user.put("sEmpl",0);

        usersCollection.document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
                    Toast.makeText(getApplicationContext(), "School User created successfully", Toast.LENGTH_SHORT).show();
//                    finish(); // Finish sign-up activity and return to login screen
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
                    Toast.makeText(getApplicationContext(), "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

}