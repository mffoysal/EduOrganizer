package com.edu.eduorganizer.routine;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.adapter.CScheduleAdapter;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.schedule.ScheduleD;
import com.edu.eduorganizer.schedule.ScheduleItem;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SaveScheduleRoutine extends AppCompatActivity {

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
    private DatabaseReference routineRef = firebaseDatabase.getReference("routine");
    private DatabaseReference scheduleRef = firebaseDatabase.getReference("classSchedule");

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

    RecyclerView recyclerView, recyclerViewTwo;
    List<ClassScheduleItem> dataList;
    List<ScheduleItem> itemList;
    List<ClassScheduleItem> dataListTwo;
    CScheduleAdapter adapter, adapterTwo;
    Routine androidData, routine;
    SearchView searchView;
    private RelativeLayout rootVieww;
    ImageView recImage;
    TextView recTitle, recDesc, recLang, faculty, room;
    MaterialButton details;
    AppCompatTextView time;
    CardView recCard;
    FrameLayout notification;
    ImageView options;
    RelativeLayout foreground;
    private TextInputEditText timeFrom, timeTo;
    private int t1Hour, t2Hour, t1Minute, t2Minute;

    private EditText subTitle, subCode, sec, roomNo, teacher;
    private AppCompatSpinner spinnerDay;
    private String selectedDay;
    private Button saveAllButton, saveSpecificButton;
    private MaterialButton saveAndDup;
    private String routineTempCodeNum;
    private FloatingActionButton fab;
    private TextView emptyViewTwo, emptyViewOne;
    private Dialog dialog;
    private String routineName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_schedule_routine);

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

        dataList = new ArrayList<>();
        itemList = new ArrayList<>();

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


        recTitle = findViewById(R.id.recTitle);
        recDesc = findViewById(R.id.recDesc);
        recCard = findViewById(R.id.recCard);
        options = findViewById(R.id.options);
        recCard.setVisibility(View.GONE);
        foreground = findViewById(R.id.foregroundId);

        recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RoutineSchedule.class);
                intent.putExtra("routine",routine);
                startActivity(intent);
                finish();
            }
        });


        MaterialButton qrBarCode = findViewById(R.id.btnRoutineSave);
        TextInputEditText routineText = findViewById(R.id.routineNameId);
        routineName = routineText.getText().toString();
        if (routineName.isEmpty()){
            routineName = "CS_"+new Unique().getDate()+" "+new Unique().getTime()+" EduCS";

        }

        qrBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAndSaveScheduleTwo(routineName);

            }
        });


    }



    private void getAndSaveScheduleTwo(String routineName) {

        itemList.clear();
        ScheduleD scheduleD = new ScheduleD(getApplicationContext());
        itemList = scheduleD.getAllSchedule();

        if (itemList.isEmpty()){

            Toast.makeText(getApplicationContext(),"Current Schedule List is Empty!",Toast.LENGTH_SHORT).show();

        }else {
            saveCurrentScheduleRoutine(routineName,itemList);
        }


    }

    private void saveCurrentScheduleRoutine(String routineName, List<ScheduleItem> itemList) {
        String from_time = routineName;
        String to_time = routineName;

        Routine routinei = new Routine();
        routinei.setuId(user.getUniqueId());
        routinei.setT_id(user.getUniqueId());
        routinei.setTid(user.getUniqueId());
        routinei.setStdId(user.getStdId());
        routinei.setUniqueId(new Unique().uId());
        routinei.setSId(user.getSId());
        routinei.setTemp_code(""+new Unique().unique_id());
        routinei.setTemp_num(new Unique().generateHexCode());
        routinei.setTemp_name(from_time);
        routinei.setTemp_details(to_time);

        RoutineD routineD = new RoutineD(database);
        long rs = routineD.insertRoutine(routinei);
        if(rs==-1){
            Toast.makeText(this,"Routine Data Not inserted! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-3){
            Toast.makeText(this,"Routine already created! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-2){
            Toast.makeText(this,"Error Occurred! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        } else {
            routine = routinei;
            convertAndSave(routinei,itemList);
            Toast.makeText(this,"Routine Successfully Saved, Thanks!",Toast.LENGTH_SHORT).show();

        }

    }
    private void convertAndSave(Routine routinei, List<ScheduleItem> itemList) {

        if (itemList.isEmpty()){
            Toast.makeText(getApplicationContext(),"Current Schedule List is Empty",Toast.LENGTH_SHORT).show();
        }else {
            for (ScheduleItem item : itemList){

                ClassScheduleItem scheduleItemi = new ClassScheduleItem();
                scheduleItemi.setTId(user.getUniqueId());
                scheduleItemi.setStdId(user.getStdId());
                scheduleItemi.setUniqueId(new Unique().uId());
                scheduleItemi.setT_name(item.getT_name());
                scheduleItemi.setSub_name(item.getSub_name());
                scheduleItemi.setSub_code(item.getSub_code());
                scheduleItemi.setDay(item.getDay());
                scheduleItemi.setRoom(item.getRoom());
                scheduleItemi.setSection(item.getSection());
                scheduleItemi.setSId(user.getSId());
                scheduleItemi.setTemp_code(routinei.getTemp_code());
                scheduleItemi.setTemp_num(routinei.getTemp_num());
                scheduleItemi.setStart_time(item.getStart_time());
                scheduleItemi.setEnd_time(item.getEnd_time());

                CScheduleD scheduleD = new CScheduleD(getApplicationContext());
                long rs = scheduleD.insertSchedule(scheduleItemi);

            }

            recCard.setVisibility(View.VISIBLE);
            recTitle.setText(routine.getTemp_name());
            recDesc.setText(routine.getTemp_num());

        }

    }


}