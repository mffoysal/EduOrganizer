package com.edu.eduorganizer.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.MainPanelActivity;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.school.SchoolDAO;
import com.edu.eduorganizer.student.Student;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserProfile extends BaseMenu {

    //common for activity start
    private Logout logout;
    private SQLiteDatabase database;
    private DatabaseManager databaseManager;
    private UserDAO userDAO;
    private Internet internet;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference = fdatabase.getReference("users");

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference schoolRef = firebaseDatabase.getReference("school");
    private DatabaseReference majorRef = firebaseDatabase.getReference("major");
    private DatabaseReference studentRef = firebaseDatabase.getReference("students");

    private DatabaseReference usersRef;
    private UserCallback userCallback;
    private BroadcastReceiver connectivityReceiver;
    private Intent intent;
    private ActionBar actionbar;

    private User user;
    private String userPhone, sId;
    private School school;
    private SchoolCallback schoolCallback;
    private ValueEventListener valueEventListener;
    //common for all activities end

    RecyclerView recyclerView;
    List<Student> dataList;

    Student androidData;
    SearchView searchView;
    private FloatingActionButton fab,fabb;

    TextView detailDesc;
    CollapsingToolbarLayout detailTitle;
    ImageView detailImage;
    private Student student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailDesc = findViewById(R.id.stdId);
        detailTitle = findViewById(R.id.profileTitle);
//        detailImage = findViewById(R.id.detailImage);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("stdId"));
            detailTitle.setTitle(bundle.getString("stdName"));
        }


        databaseManager = new DatabaseManager(this);
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();

//        fab = findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), schoolUpload.class);
//                startActivity(intent);
//            }
//        });

        // Common for all activity Start
        logout = new Logout(this);
        if (!logout.isLoggedIn()){
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
            finish();
        }
        intent = getIntent();
        actionbar = getSupportActionBar();
        actionbar.setTitle(actionbar.getTitle()+" Student Details");
        if (intent != null && intent.hasExtra("user")) {
            String url = intent.getStringExtra("user");
//            actionbar.setTitle(actionbar.getTitle()+" "+url);
            userPhone = url;
            user = logout.getUser();
            sId=user.getSId();
//            school = logout.getSchool();
//            sId=school.getsId();


            if (user == null) {
                user = (User) intent.getSerializableExtra("userData");

            }
        }else {
            user = logout.getUser();
            sId=user.getSId();
//            school = logout.getSchool();
//            sId=school.getsId();
//            actionbar.setTitle(actionbar.getTitle()+" "+logout.getStringPreference("userId"));
        }
        if (intent != null && intent.hasExtra("eduBox")) {
            String url = intent.getStringExtra("eduBox");
            actionbar.setTitle(actionbar.getTitle()+" "+url);
        }
        internet = new Internet(getApplicationContext());
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(Network.ACTION_CONNECTIVITY_CHANGE)) {
                    boolean isConnected = intent.getBooleanExtra(Network.EXTRA_CONNECTIVITY_STATUS, false);
                    if (isConnected) {
//                        Toast.makeText(getApplicationContext(),"Internet Connected",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        };
        getSchoolData(user);
        schoolCallback = new SchoolCallback() {
            @Override
            public void onSchoolRetrieved(School school) {
                processSchool(school);
            }

            @Override
            public void onSchoolNotFound() {
                handleSchoolNotFound();
            }

        };
        //Common for all activity End



        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent put extra
                Intent intent = new Intent(getApplicationContext(), UpdateProfile.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        fabb = findViewById(R.id.fabb);
        fabb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent put extra
                Intent intent = new Intent(getApplicationContext(), MainPanelActivity.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });


        if (intent != null && intent.hasExtra("student")) {
            student = (Student) intent.getSerializableExtra("student");

            String stdName = student.getStdName();

            Toast.makeText(this,""+student.getStdId(),Toast.LENGTH_SHORT).show();

            String stdPhone = student.getstdPhone();
            String stdId = student.getStdId();
            String sId = student.getSId();

        }

        detailDesc.setText(user.getStdName()+"|"+user.getEmail());
        detailTitle.setTitle(user.getPhone());

    }

    private void handleSchoolNotFound() {
        school = new SchoolDAO(database).getSchoolBySID(user.getSId());
        logout.saveSchool(school);

    }

    private void processSchool(School s) {
        school = s;
        logout.saveSchool(s);
    }

    private void getSchoolData(User user) {
        if (internet.isInternetConnection()){
            getSchool(user.getSId());
            Log.e("school","us sid from getSchooData "+user.getSId() );
        }else {
            try {
                school = new SchoolDAO(database).getSchoolBySID(user.getSId());

                Log.e("school","error: "+user.getSId() );
            }catch (Exception e){
                Log.e("school","error: "+e+" "+user.getSId() );
            }
        }
    }

    private School getSchool(String getsId) {
        Query query = schoolRef.orderByChild("sId").equalTo(getsId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    School scl = userSnapshot.getValue(School.class);
                    if (scl != null) {
//                        Log.e("UserFound","User FF Found");
                        school = scl;
                        logout.saveSchool(scl);
                        schoolCallback.onSchoolRetrieved(scl);
                        return;
                    }
                }
                schoolCallback.onSchoolNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return new School();
    }

}