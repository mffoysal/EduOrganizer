package com.edu.eduorganizer.routine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.adapter.ClassScheduleAdapter;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.schedule.ScheduleItem;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class
ShareRoutine extends AppCompatActivity {


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
    private DatabaseReference studentRef = firebaseDatabase.getReference("classSchedule");

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
    List<ClassScheduleItem> dataList = new ArrayList<>();
    ClassScheduleAdapter adapter;
    ScheduleItem androidData;
    SearchView searchView;
    private RelativeLayout rootVieww;

    private TextInputEditText timeFrom, timeTo;
    private int t1Hour, t2Hour, t1Minute, t2Minute;

    private EditText subTitle, subCode, sec, roomNo, teacher;
    private AppCompatSpinner spinnerDay;
    private String selectedDay;
    private Button saveButton;
    private MaterialButton saveAndDup, syncButton;
    private ClassScheduleItem scheduleItem;

    private TextView qrId, barcodeTextId, titleId, descriptionId, subName, subNamee, subCodee, timee, faculty, roome, sub_code, time, section, teacherFaculty, room, designation, day, phone;

    private ImageView qrImage, barcodeImage;
    private Routine routine = new Routine();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_routine);


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
//            actionbar.setTitle(actionbar.getTitle()+" "+url);
        }
        if (intent != null && intent.hasExtra("routine")) {
            routine = (Routine) intent.getSerializableExtra("routine");
//                    Log.d("eee",routine.getTemp_name()+" "+routine.getTemp_details());
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
        dataList = new ArrayList<>();

        qrImage = findViewById(R.id.qrImageView);
        barcodeImage = findViewById(R.id.barcodeImageId);
        barcodeTextId = findViewById(R.id.barcodeTextId);
        qrId = findViewById(R.id.qrTextId);
        barcodeTextId.setText(""+routine.getTemp_code());
        qrId.setText(""+routine.getTemp_num());


        Bitmap qrBitmap1 = generateQRCode(user.getUserId());
        Bitmap qrBitmap = generateQRCode(""+routine.getTemp_num());
        qrImage.setImageBitmap(qrBitmap);

        Bitmap barcodeBitmap = generateBarcode(""+routine.getTemp_code());
        barcodeImage.setImageBitmap(barcodeBitmap);
//        generateDisplayBarcode("221005312");

        syncButton = findViewById(R.id.btnStatusId);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllSchedule(routine);
            }
        });

        if (routine.getSync_key()!=null){
            syncButton.setText("Routine Synchronized, Update More");
            syncButton.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_light));
            syncButton.setStrokeColorResource(android.R.color.holo_green_light);
        }else {

            if (internet.isInternetConnection()){
                routineAdd(routine);
            }else {
                Toast.makeText(getApplicationContext(),"Please, Connect your Data Connection, Then Try again!",Toast.LENGTH_SHORT).show();
            }

        }

        if (routine.getSync_status()!=0){
            syncButton.setText("Routine Synchronized, Update More");
            syncButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.lavenda));
            syncButton.setStrokeColorResource(android.R.color.holo_green_light);
        }else {

        }

    }

    private void updateAllSchedule(Routine routine) {
        dataList.clear();
        CScheduleD scheduleD = new CScheduleD(getApplicationContext());
        dataList = scheduleD.getAllSchedule(user.getUniqueId(),routine.getTemp_num());

//        List<Map<String, Object>> scheduleItemMaps = new ArrayList<>();
//        scheduleItemMaps = convertSchedule(dataList);

        if (dataList.isEmpty()) {
            Toast.makeText(this,"Schedule List is Empty!  ",Toast.LENGTH_SHORT).show();
        } else {
            updateOrAddItemInDatabase(dataList);
        }
    }

    private void routineScheduleUpdating(List<Map<String, Object>> scheduleItemMaps) {
    }
    private void updateOrAddItemInDatabase(List<ClassScheduleItem> dataList) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String databasePath = "classSchedule";

        for (ClassScheduleItem newItem : dataList){
            String itemKey = newItem.getUniqueId();

            databaseReference.child(databasePath).child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Item already exists, update its data
                        databaseReference.child(databasePath).child(itemKey).setValue(newItem)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Data updated successfully
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Failed to update data
                                    }
                                });
                    } else {
                        // Item doesn't exist, add it as a new item
                        databaseReference.child(databasePath).child(itemKey).setValue(newItem)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Data added successfully
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Failed to add data
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }

    }

    private void routineAdd(Routine routine) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference routineRef = firebaseDatabase.getReference("routine");

        String key = routineRef.push().getKey();

        Routine routine1 = new Routine();
        routine1 = routine;
        routine1.setKey(key);
        routine1.setSync_key(routine.getUniqueId());
        routine1.setSync_status(1);

        Routine finalRoutine = routine1;
        routineRef.child(routine1.getUniqueId())
                .setValue(routine1)
                .addOnSuccessListener(aVoid -> {
                    // User information saved successfully
                    routineUpdate(finalRoutine);
                    routineScheduleAdd(finalRoutine);
//                    Toast.makeText(get, "User created successfully", Toast.LENGTH_SHORT).show();
//                    finish(); // Finish sign-up activity and return to login screen
                })
                .addOnFailureListener(e -> {
                    // Failed to save user information
//                    Toast.makeText(this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void routineUpdate(Routine finalRoutine) {
        RoutineD routineD = new RoutineD(getApplicationContext());
        long rs = routineD.updateRoutine(finalRoutine);
        if(rs==-1){
            Toast.makeText(this,"Schedule Data Not inserted! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-3){
            Toast.makeText(this,"Schedule already created! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-2){
            Toast.makeText(this,"Error Occurred! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"Schedule Successfully Saved, Thanks!",Toast.LENGTH_SHORT).show();
        }
    }

    private void routineScheduleAdd(Routine routine1) {
        dataList.clear();
        CScheduleD scheduleD = new CScheduleD(getApplicationContext());
        dataList = scheduleD.getAllSchedule(user.getUniqueId(),routine.getTemp_num());

        List<Map<String, Object>> scheduleItemMaps = new ArrayList<>();
        scheduleItemMaps = convertSchedule(dataList);

        if (scheduleItemMaps.isEmpty()) {
            scheduleItemMaps = convertSchedule(dataList);
        } else {
//            routineScheduleAdding(scheduleItemMaps);
            updateOrAddItemInDatabase(dataList);
        }


    }

    private void routineScheduleAdding(List<Map<String, Object>> scheduleItemMaps) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String databasePath = "classSchedule";

//        for (Map<String, Object> scheduleItemMap : scheduleItemMaps) {
//            // Determine the unique identifier for each item (e.g., using item.get("uniqueId"))
//            String itemKey = (String) scheduleItemMap.get("uniqueId");
//
//            // Update or add the item in the database
//            databaseReference.child(databasePath).child(itemKey).updateChildren(scheduleItemMap)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            // Data updated successfully for this item
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // Failed to update data for this item
//                        }
//                    });
//        }

        databaseReference.child(databasePath).setValue(scheduleItemMaps)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data successfully added to the database
                        syncButton.setText("Routine Synchronized, Update More");
                        syncButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.lavenda));
                        syncButton.setStrokeColorResource(android.R.color.holo_green_light);
                        Toast.makeText(getApplicationContext(), "Data added to Database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data to the database
                        Toast.makeText(getApplicationContext(), "Failed to add data to Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List<Map<String, Object>> convertSchedule(List<ClassScheduleItem> dataList) {
        List<Map<String, Object>> scheduleItemMaps = new ArrayList<>();
        for (ClassScheduleItem item : dataList) {
            Map<String, Object> scheduleItemMap = new HashMap<>();
            scheduleItemMap.put("id", item.getId());
            scheduleItemMap.put("sId", item.getSId());
            scheduleItemMap.put("uniqueId", item.getUniqueId());
            scheduleItemMap.put("t_id", item.getT_id());
            scheduleItemMap.put("t_name", item.getT_name());
            scheduleItemMap.put("stdId", item.getStdId());
            scheduleItemMap.put("room", item.getRoom());
            scheduleItemMap.put("day", item.getDay());
            scheduleItemMap.put("campus", item.getCampus());
            scheduleItemMap.put("start_time", item.getStart_time());
            scheduleItemMap.put("end_time", item.getEnd_time());
            scheduleItemMap.put("section", item.getSection());
            scheduleItemMap.put("tId", item.getTId());
            scheduleItemMap.put("dateTime", item.getDateTime());
            scheduleItemMap.put("sub_name", item.getSub_name());
            scheduleItemMap.put("sub_code", item.getSub_code());
            scheduleItemMap.put("temp_code", item.getTemp_code());
            scheduleItemMap.put("temp_num", item.getTemp_num());
            scheduleItemMap.put("sync_key", item.getSync_key());
            scheduleItemMap.put("sync_status", item.getSync_status());
            scheduleItemMap.put("min", item.getMin());
            scheduleItemMaps.add(scheduleItemMap);
        }
        return scheduleItemMaps;
    }

    private List<ClassScheduleItem> convertToClassScheduleItems(List<Map<String, String>> scheduleItemMaps) {
        List<ClassScheduleItem> dataList = new ArrayList<>();
        for (Map<String, String> scheduleItemMap : scheduleItemMaps) {
            ClassScheduleItem item = new ClassScheduleItem();
            item.setId(Integer.parseInt(scheduleItemMap.get("id")));
            item.setSId(scheduleItemMap.get("sId"));
            item.setUniqueId(scheduleItemMap.get("uniqueId"));
            item.setT_id(scheduleItemMap.get("t_id"));
            item.setT_name(scheduleItemMap.get("t_name"));
            item.setStdId(scheduleItemMap.get("stdId"));
            item.setRoom(scheduleItemMap.get("room"));
            item.setDay(scheduleItemMap.get("day"));
            item.setCampus(scheduleItemMap.get("campus"));
            item.setStart_time(scheduleItemMap.get("start_time"));
            item.setEnd_time(scheduleItemMap.get("end_time"));
            item.setSection(scheduleItemMap.get("section"));
            item.setTId(scheduleItemMap.get("tId"));
            Calendar dateTime = Calendar.getInstance();
            dateTime.setTimeInMillis(Long.parseLong(scheduleItemMap.get("dateTime")));
            item.setDateTime(dateTime);
            item.setSub_name(scheduleItemMap.get("sub_name"));
            item.setSub_code(scheduleItemMap.get("sub_code"));
            item.setTemp_code(scheduleItemMap.get("temp_code"));
            item.setTemp_num(scheduleItemMap.get("temp_num"));
            item.setSync_key(scheduleItemMap.get("sync_key"));
            item.setSync_status(Integer.parseInt(scheduleItemMap.get("sync_status")));
//            item.setaStatus(Integer.parseInt(scheduleItemMap.get("aStatus")));
            item.setMin(Integer.parseInt(scheduleItemMap.get("min")));
            dataList.add(item);
        }
        return dataList;
    }


    private Bitmap generateQRCode(String textToEncode) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            BitMatrix bitMatrix = barcodeEncoder.encode(textToEncode, BarcodeFormat.QR_CODE, 1080, 1080);
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            Log.e("QRCodeGenerator", "Error generating QR code", e);
            return null;
        }
    }

    private Bitmap generateBarcode(String textToEncode) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            BitMatrix bitMatrix = barcodeEncoder.encode(textToEncode, BarcodeFormat.CODE_128, 1000, 600);
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            Log.e("BarcodeGenerator", "Error generating barcode", e);
            return null;
        }
    }

    private Bitmap generateCode128Barcode(String textToEncode) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            BitMatrix bitMatrix = barcodeEncoder.encode(textToEncode, BarcodeFormat.CODE_128, 100, 100);
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            Log.e("Code128BarcodeGenerator", "Error generating Code 128 barcode", e);
            return null;
        }
    }

    private Bitmap generateDisplayBarcode(String numericValue) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap barcodeBitmap = barcodeEncoder.encodeBitmap(numericValue, BarcodeFormat.CODE_128, 600, 300);
            qrImage.setImageBitmap(barcodeBitmap);
            return barcodeBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkRoutine(String qrCodeContent) {
        int r=0;
        RoutineD routineD = new RoutineD(getApplicationContext());
        r =  routineD.checkRoutine(qrCodeContent);

        if(r!=0){
            routine = routineD.RoutineData(qrCodeContent);
            Toast.makeText(getApplicationContext(),"This Routine Already Added in Your Routine List",Toast.LENGTH_SHORT).show();
        }else {
            getRoutineFromOnline(qrCodeContent);
        }

    }

    private void getRoutineFromOnline(String tempCode) {

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

}