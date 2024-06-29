package com.edu.eduorganizer.routine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.adapter.ScheduleAdapter;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.school.SchoolDAO;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

public class NewRoutine extends AppCompatActivity implements View.OnClickListener {


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
    List<Routine> dataList;
    ScheduleAdapter adapter;
    Routine androidData;
    SearchView searchView;
    private RelativeLayout rootVieww;

    private TextInputEditText timeFrom, timeTo;
    private int t1Hour, t2Hour, t1Minute, t2Minute;

    private EditText subTitle, subCode, sec, roomNo, teacher;
    private AppCompatSpinner spinnerDay;
    private String selectedDay;
    private Button saveButton;
    private MaterialButton saveAndDup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_routine);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button (back button)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set a click listener on the toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the toolbar click (back button)
                onBackPressed();
            }
        });

        timeFrom = findViewById(R.id.timeFromId);
        timeTo=findViewById(R.id.timeToId);
        timeFrom.setOnClickListener(this);
        timeTo.setOnClickListener(this);


        databaseManager = new DatabaseManager(getApplicationContext());
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
        logout = new Logout(getApplicationContext());
        if (!logout.isLoggedIn()){
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
            finish();
        }
        intent = getIntent();
        actionbar = getSupportActionBar();
        actionbar.setTitle(actionbar.getTitle()+" Students");
        if (intent != null && intent.hasExtra("user")) {
            String url = intent.getStringExtra("user");
//            actionbar.setTitle(actionbar.getTitle()+" "+url);
            userPhone = url;
            user = logout.getUser();
            sId=user.getSId();
//            school = logout.getSchool();
//            sId=school.getsId();
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



        saveButton = findViewById(R.id.saveRoutine);
        saveButton.setOnClickListener(this);



    }


    private void handleSchoolNotFound() {
        school = new SchoolDAO(database).getSchoolBySID(user.getSId());
    }

    private void processSchool(School s) {
        school = s;
        logout.saveSchool(school);
        Log.e("school","school sId from processSchool "+s.getsId());
    }

    private void getSchoolData(User user) {
        if (internet.isInternetConnection()){
            getSchool(user.getSId());
            Log.e("school","us sid from getSchooData "+user.getSId() );
        }else {
            try {
                school = new SchoolDAO(database).getSchoolBySID(user.getSId());
                logout.saveSchool(school);
                Log.e("school","us "+user.getSId() );
            }catch (Exception e){
                Log.e("school","us "+e+" "+user.getSId() );
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


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.timeFromId){


        } else if (v.getId()==R.id.timeToId) {


        } else if (v.getId()==R.id.saveRoutine) {
            saveSchedule();
        } else if (v.getId()==R.id.saveBtnId) {
            saveScheduleAndDuplicate();
        }
    }

    private void saveScheduleAndDuplicate() {

        String from_time = String.valueOf(timeFrom.getText());
        String to_time = String.valueOf(timeTo.getText());


        Routine routine = new Routine();
        routine.setuId(user.getUniqueId());
        routine.setT_id(user.getUniqueId());
        routine.setTid(user.getUniqueId());
        routine.setStdId(user.getStdId());
        routine.setUniqueId(new Unique().uId());
        routine.setSId(user.getSId());
        routine.setTemp_code(""+new Unique().unique_id());
        routine.setTemp_num(new Unique().generateHexCode());
        routine.setTemp_name(from_time);
        routine.setTemp_details(to_time);

        RoutineD routineD = new RoutineD(database);
        long rs = routineD.insertRoutine(routine);
        if(rs==-1){
            Toast.makeText(this,"Schedule Data Not inserted! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-3){
            Toast.makeText(this,"Schedule already created! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-2){
            Toast.makeText(this,"Error Occurred! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"Routine Successfully Saved, Thanks!",Toast.LENGTH_SHORT).show();
        }

    }

    private void saveSchedule() {
        String from_time = String.valueOf(timeFrom.getText());
        String to_time = String.valueOf(timeTo.getText());


        Routine routine = new Routine();
        routine.setuId(user.getUniqueId());
        routine.setT_id(user.getUniqueId());
        routine.setTid(user.getUniqueId());
        routine.setStdId(user.getStdId());
        routine.setUniqueId(new Unique().uId());
        routine.setSId(user.getSId());
        routine.setTemp_code(""+new Unique().unique_id());
        routine.setTemp_num(new Unique().generateHexCode());
        routine.setTemp_name(from_time);
        routine.setTemp_details(to_time);

        RoutineD routineD = new RoutineD(database);
        long rs = routineD.insertRoutine(routine);
        if(rs==-1){
            Toast.makeText(this,"Schedule Data Not inserted! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-3){
            Toast.makeText(this,"Schedule already created! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-2){
            Toast.makeText(this,"Error Occurred! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"Routine Successfully Saved, Thanks!",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void showTimePickerDialogTwo() {
        Calendar calendar = Calendar.getInstance();
        t2Hour = calendar.get(Calendar.HOUR_OF_DAY);
        t2Minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NewRoutine.this, android.R.style.Theme_Holo_Dialog_MinWidth ,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                t2Hour = hourOfDay;
                t2Minute = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,t2Hour,t2Minute);
                timeTo.setText(DateFormat.format("hh:mm aa",calendar));

            }
        },12,0,false);
        timePickerDialog.updateTime(t2Hour,t2Minute);
        timePickerDialog.show();
    }

    private void showTimePickerDialogOne() {
        Calendar calendar = Calendar.getInstance();
        t1Hour = calendar.get(Calendar.HOUR_OF_DAY);
        t1Minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NewRoutine.this, android.R.style.Theme_Holo_Dialog_MinWidth ,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                t1Hour = hourOfDay;
                t1Minute = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,t1Hour,t1Minute);
                timeFrom.setText(DateFormat.format("hh:mm aa",calendar));

            }
        },12,0,false);
        timePickerDialog.updateTime(t1Hour,t1Minute);
        timePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        t1Hour = calendar.get(Calendar.HOUR_OF_DAY);
        t1Minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NewRoutine.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                t1Hour = hourOfDay;
                t1Minute = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,t1Hour,t1Minute);
                timeFrom.setText(DateFormat.format("hh:mm aa",calendar));

            }
        },12,0,false);
        timePickerDialog.updateTime(t1Hour,t1Minute);
        timePickerDialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}