package com.edu.eduorganizer.routine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.adapter.ClassScheduleAdapter;
import com.edu.eduorganizer.adapter.ScheduleAdapter;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.scanner.AutoFocus;
import com.edu.eduorganizer.scanner.SetAttendance;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;


public class RoutineScanner extends AppCompatActivity {


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

    RecyclerView recyclerView;
    List<ClassScheduleItem> dataList;
    ClassScheduleAdapter adapter;
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
    private Button saveButton;
    private MaterialButton saveAndDup;
    private String routineTempCodeNum;
    private String tempCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_routine_qr);

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


        // Initialize the barcode scanner
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE); // Set the desired barcode format (QR code)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES); // Scan all barcode formats
        integrator.setPrompt("Scan Routine Temp Code OR Num QR|BAR");
        integrator.setCaptureActivity(AutoFocus.class); // Use a custom capture activity that enables autofocus
        integrator.setOrientationLocked(false); // Allow rotation
        integrator.setBeepEnabled(true); // Enable beep sound

//        integrator.setCameraId(CameraSelector.DEFAULT_BACK_CAMERA); // Use the back camera
        integrator.setTorchEnabled(false); // Disable torch (flashlight)
//        integrator.setScanningRectangle(IntentIntegrator.ALL_CODE_TYPES); // Set the scanning rectangle, if needed


        integrator.initiateScan();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of the barcode scan
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Extracted QR code content
                String qrCodeContent = result.getContents();
                routineTempCodeNum = qrCodeContent;
                // TODO: Handle the QR code content, e.g., redirect to another activity
                Intent intent = new Intent(this, ScanRoutine.class);
                intent.putExtra("tempCodeNum", qrCodeContent);
//                startActivity(intent);

                checkRoutine(qrCodeContent);
                if (routine!=null){
                    Toast.makeText(getApplicationContext(),"Scan Completed "+routine,Toast.LENGTH_SHORT).show();
                    recCard.setVisibility(View.VISIBLE);
                }


            }
        }
    }

    private void checkRoutine(String qrCodeContent) {
        int r=0;
        RoutineD routineD = new RoutineD(getApplicationContext());
        r =  routineD.checkRoutine(qrCodeContent);

        if(r!=0){
            routine = routineD.RoutineData(qrCodeContent);
            recCard.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"This Routine Already Added in Your Routine List",Toast.LENGTH_SHORT).show();
        }else {
            getRoutineFromOnline(qrCodeContent);

        }

    }

    private void getRoutineFromOnlinee(String tempCode) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String databasePath = "routine";

        Query queryByTempCode = databaseReference.child(databasePath)
                .orderByChild("temp_code")
                .equalTo(tempCode)
                .limitToFirst(1);

        Query queryByTempNum = databaseReference.child(databasePath)
                .orderByChild("temp_num")
                .equalTo(tempCode)
                .limitToFirst(1);

        queryByTempCode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Routine routinee = snapshot.getValue(Routine.class);
                        if (routinee != null) {
                            routine =routinee;
                            return;
                        }
                    }
                }

                queryByTempNum.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Routine routinee = snapshot.getValue(Routine.class);
                                if (routinee != null) {
                                    routine = routinee;
                                    return;
                                }
                            }
                        }

                        Toast.makeText(getApplicationContext(),"Routine Template is not found in Online Database! ",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors or cancellations
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors or cancellations
            }
        });


    }

    private void getRoutineFromOnline(String tempCodee) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String databasePath = "routine";

        Query query = databaseReference.child(databasePath)
                .orderByChild("temp_code")
                .equalTo(tempCodee)
                .limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Data with matching temp_code found
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Routine routinee = snapshot.getValue(Routine.class);
                        if (routinee != null) {
                            routine = routinee;
                            androidData = routinee;
                            cardVisible(routine);

                        }
                    }
                } else {
                    getRoutineFromOnlineWithNum(tempCodee);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors or cancellations
            }
        });
    }

    private void cardVisible(Routine routine) {
        Log.e("eee","Routine Main "+routine.getTemp_num());
        tempCode = routine.getTemp_num();
        if (routine!=null){
//            Toast.makeText(getApplicationContext(),""+routine.getTemp_name(),Toast.LENGTH_SHORT).show();
//            recCard.setVisibility(View.VISIBLE);
            recTitle.setText(routine.getTemp_name());
            recDesc.setText(routine.getTemp_num()+" "+routine.getTemp_code());

            RoutineD routineD = new RoutineD(getApplicationContext());

            Routine routine1 = new Routine();
            routine1 = routine;
            routine1.setTemp_name(routine1.getTemp_name()+"_"+new Unique().getDate()+"_"+new Unique().getTime());
            routine1.setuId(user.getUniqueId());
            routine1.setUniqueId(new Unique().uId());
            routine1.setTid(user.getUniqueId());
            routine1.setSync_key(null);
            routine1.setSync_status(0);
            routine1.setTemp_code(""+new Unique().unique_id());
            routine1.setTemp_num(new Unique().generateHexCode());

            long rs = routineD.insertRoutine(routine1);
            if(rs==-1){
                Toast.makeText(this,"Schedule Data Not inserted! Try again!  "+rs,Toast.LENGTH_SHORT).show();

            }else if(rs==-3){
                Toast.makeText(this,"Schedule already created! Try again!  "+rs,Toast.LENGTH_SHORT).show();

            }else if(rs==-2){
                Toast.makeText(this,"Error Occurred! Try again!  "+rs,Toast.LENGTH_SHORT).show();

            } else {

                fetchAllSchedule(tempCode,routine1);
                recCard.setVisibility(View.VISIBLE);
//                Toast.makeText(this,"Schedule Successfully Saved, Thanks!",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void fetchAllSchedule(String tempCode, Routine routine1) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String databasePath = "classSchedule";
        Log.e("eee","routine check "+tempCode);

        Query query = databaseReference.child(databasePath).orderByChild("temp_num").equalTo(tempCode);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    dataList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ClassScheduleItem scheduleItem = snapshot.getValue(ClassScheduleItem.class);

                        if (scheduleItem != null) {
                            dataList.add(scheduleItem);
                        }
                    }

                    saveSchedules(routine1, dataList);
                } else {
                    Log.e("eee","Schedule not found with Routine");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the fetch operation
                Log.e("eee", "Database Error: " + databaseError.getMessage());
            }
        });
    }


    private void getRoutineFromOnlineWithNum(String tempCode) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String databasePath = "routine";

        Query query = databaseReference.child(databasePath)
                .orderByChild("temp_num")
                .equalTo(tempCode)
                .limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Routine routinee = snapshot.getValue(Routine.class);
                        if (routinee != null) {
                            routine = routinee;
                            androidData = routinee;
                            cardVisible(routine);
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Routine Template is not found in Online Database! ",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors or cancellations
            }
        });
    }

    private void saveSchedules(Routine routineee,List<ClassScheduleItem> dataList) {
//        Toast.makeText(getApplicationContext(),"save schedule"+routineee.getTemp_num(),Toast.LENGTH_SHORT).show();

        if (dataList.isEmpty()){
            Toast.makeText(getApplicationContext(),"Schedules Not Found",Toast.LENGTH_SHORT).show();
        }else {
            for (ClassScheduleItem item: dataList){
                item.setTId(user.getUniqueId());
                item.setUniqueId(new Unique().uId());
                item.setStdId(user.getStdId());
                item.setSync_key(null);
                item.setSync_status(0);
                item.setTemp_code(routineee.getTemp_code());
                item.setTemp_num(routineee.getTemp_num());

                CScheduleD cScheduleD = new CScheduleD(getApplicationContext());
                cScheduleD.insertSchedule(item);

            }

        }

    }


}
