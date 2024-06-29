package com.edu.eduorganizer.routine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.edu.eduorganizer.adapter.CScheduleAdapter;
import com.edu.eduorganizer.adapter.CScheduleViewTouchHelper;
import com.edu.eduorganizer.adapter.ItemTouchListener;
import com.edu.eduorganizer.adapter.ViewItemTouchHelper;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.panel.UserPanel;
import com.edu.eduorganizer.schedule.ScheduleD;
import com.edu.eduorganizer.schedule.ScheduleItem;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.school.SchoolDAO;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SaveCurrentSchedule extends AppCompatActivity implements View.OnClickListener, ItemTouchListener, AdapterView.OnItemClickListener, CScheduleAdapter.OnEditClickListener, CScheduleAdapter.OnDeleteClickListener, CScheduleAdapter.OnCopyClickListener {

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
        setContentView(R.layout.activity_save_current_schedule);

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
        dataListTwo = new ArrayList<>();

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
        if (intent != null && intent.hasExtra("routine")) {
            routine = (Routine) intent.getSerializableExtra("routine");
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




        rootVieww = findViewById(R.id.allStdPage);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewTwo = findViewById(R.id.recyclerViewTwo);
        searchView = findViewById(R.id.search);
        fab = findViewById(R.id.fab);
        saveAllButton = findViewById(R.id.saveAllId);
        saveSpecificButton = findViewById(R.id.saveSpecificId);
//        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        saveAllButton.setOnClickListener(this);
        saveSpecificButton.setOnClickListener(this);



        emptyViewOne = findViewById(R.id.tvEmptyListTop);
        emptyViewTwo = findViewById(R.id.tvEmptyListBottom);


        recycleVisibility();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerTwo = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewTwo.setLayoutManager(layoutManagerTwo);

//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecoration);

        ItemTouchHelper.SimpleCallback simpleCallback = new CScheduleViewTouchHelper(ItemTouchHelper.UP | ItemTouchHelper.DOWN , ItemTouchHelper.UP | ItemTouchHelper.DOWN, this);
        ItemTouchHelper.SimpleCallback simpleCallbackTwo = new CScheduleViewTouchHelper(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.UP | ItemTouchHelper.DOWN, this);
//        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
//        new ItemTouchHelper(simpleCallbackTwo).attachToRecyclerView(recyclerViewTwo);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        GridLayoutManager gridLayoutManagerTwo = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerViewTwo.setLayoutManager(gridLayoutManagerTwo);
        dataList = new ArrayList<>();

        adapter = new CScheduleAdapter(getApplicationContext(), dataList,this, this, this,this);
        recyclerView.setAdapter(adapter);


        adapterTwo = new CScheduleAdapter(getApplicationContext(), dataListTwo,this, this, this,this);
        recyclerViewTwo.setAdapter(adapterTwo);


        // Create an ItemTouchHelper.Callback for recyclerView
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN, // Drag directions
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT // Swipe directions (set to 0 if not needed)
        ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                int sourcePosition = source.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();

                ClassScheduleItem item = adapter.getItem(sourcePosition);

//                adapter.removeAt(sourcePosition);
                adapter.notifyDataSetChanged();

//        adapterTwo.addItem(item, targetPosition);
//        adapterTwo.notifyDataSetChanged();
                dataListTwo.add(item);
                adapterTwo.notifyDataSetChanged();

                recyclerViewTwo.setAdapter(adapterTwo);
                recycleVisibility();

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ClassScheduleItem classScheduleItem = dataList.get(viewHolder.getAdapterPosition());
                int indexDelete = viewHolder.getAdapterPosition();
//                adapter.removeAt(indexDelete);
                adapter.notifyDataSetChanged();

                dataListTwo.add(classScheduleItem);
                adapterTwo.notifyDataSetChanged();

                recyclerViewTwo.setAdapter(adapterTwo);
                recycleVisibility();
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);



        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackTwo = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.UP, // Drag directions
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT // Swipe directions (set to 0 if not needed)
        ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                int sourcePosition = source.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();

                ClassScheduleItem item = adapterTwo.getItem(sourcePosition);

                adapterTwo.removeAt(sourcePosition);
                adapterTwo.notifyDataSetChanged();

//        adapterTwo.addItem(item, targetPosition);
//        adapterTwo.notifyDataSetChanged();

//                dataList.add(item);
                adapter.notifyDataSetChanged();

                recyclerViewTwo.setAdapter(adapterTwo);
                recycleVisibility();


//                adapterTwo.removeAt(sourcePosition);
//                adapterTwo.notifyDataSetChanged();
//
//                adapter.addItem(item, targetPosition);
//                adapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ClassScheduleItem classScheduleItem = dataList.get(viewHolder.getAdapterPosition());
                int indexDelete = viewHolder.getAdapterPosition();
                adapterTwo.removeAt(indexDelete);
                adapterTwo.notifyDataSetChanged();

//                dataList.add(classScheduleItem);
                adapter.notifyDataSetChanged();

                recyclerViewTwo.setAdapter(adapterTwo);
                recycleVisibility();
            }
        };

        ItemTouchHelper itemTouchHelperTwo = new ItemTouchHelper(itemTouchHelperCallbackTwo);
        itemTouchHelperTwo.attachToRecyclerView(recyclerViewTwo);




//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
//                ItemTouchHelper.UP | ItemTouchHelper.DOWN, // Drag directions
//                0 // Swipe directions (set to 0 if not needed)
//        ) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
//
//                int sourcePosition = source.getAdapterPosition();
//                int targetPosition = target.getAdapterPosition();
//
//                ClassScheduleItem item = adapter.getItem(sourcePosition);
//
//                adapter.removeAt(sourcePosition);
//                adapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(),"Drag and Drop ",Toast.LENGTH_SHORT).show();
////                adapterTwo.insert(item, targetPosition);
//                adapterTwo.addItem(item, targetPosition);
//                adapterTwo.notifyDataSetChanged();
//
//                return true;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                // Handle swipe actions if needed
//            }
//        };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//        itemTouchHelper.attachToRecyclerView(recyclerViewTwo);


//        fetchSchedulesFromLocal();
        if (internet.isInternetConnection()){
            fetchSchedulesFromLocal();
//            fetchSchedules();
        }else {
            fetchSchedulesFromLocal();
//            fetchDepsWithLocal();
//            fetchSessionsWithLocal();
        }




    }

    private void recycleVisibility() {
        if (dataList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyViewOne.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewOne.setVisibility(View.GONE);
        }

        if (dataListTwo.isEmpty()) {
            recyclerViewTwo.setVisibility(View.GONE);
            emptyViewTwo.setVisibility(View.VISIBLE);
        } else {
            recyclerViewTwo.setVisibility(View.VISIBLE);
            emptyViewTwo.setVisibility(View.GONE);
        }
    }


    private void fetchSchedulesFromLocal() {
//        Toast.makeText(getContext(), "Data Retrieving", Toast.LENGTH_SHORT).show();
        dataList.clear();
        CScheduleD scheduleD = new CScheduleD(getApplicationContext());
        dataList = scheduleD.getAllSchedule(user.getUniqueId(),routine.getTemp_code());

//        dataListTwo = dataList;

        adapter.notifyDataSetChanged();
        adapter = new CScheduleAdapter(getApplicationContext(), dataList,this, this, this,this);
        recyclerView.setAdapter(adapter);
        recycleVisibility();

//        adapterTwo.notifyDataSetChanged();
//        adapterTwo = new CScheduleAdapter(getApplicationContext(), dataListTwo,this, this, this,this);
//        recyclerViewTwo.setAdapter(adapterTwo);
    }


    private void fetchSchedules() {

        valueEventListener = studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    ClassScheduleItem scheduleItem = itemSnapshot.getValue(ClassScheduleItem.class);
                    if (scheduleItem != null && scheduleItem.getSId().equals(sId)) {
                        scheduleItem.setKey(itemSnapshot.getKey());
                        dataList.add(scheduleItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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


    private void searchList(String text){
        List<ClassScheduleItem> dataSearchList = new ArrayList<>();
        for (ClassScheduleItem data : dataList){
            if (data.getSub_name().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()){
            Toast.makeText(getApplicationContext(), "Schedule Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.saveAllId){

            showSaveAllSchedule();

        }else if (v.getId()==R.id.saveSpecificId) {

            showSaveSchedule();

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        ClassScheduleItem classScheduleItem = dataList.get(viewHolder.getAdapterPosition());
        int indexDelete = viewHolder.getAdapterPosition();
//        adapter.removeAt(indexDelete);
        adapter.notifyDataSetChanged();

        dataListTwo.add(classScheduleItem);
        adapterTwo.notifyDataSetChanged();

        adapterTwo = new CScheduleAdapter(getApplicationContext(), dataListTwo,this, this, this,this);
        recyclerViewTwo.setAdapter(adapterTwo);
        recycleVisibility();

    }

    @Override
    public void onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int sourcePosition = source.getAdapterPosition();
        int targetPosition = target.getAdapterPosition();

        ClassScheduleItem item = adapter.getItem(sourcePosition);

//        adapter.removeAt(sourcePosition);
        adapter.notifyDataSetChanged();

//        adapterTwo.addItem(item, targetPosition);
//        adapterTwo.notifyDataSetChanged();
        dataListTwo.add(item);
        adapterTwo.notifyDataSetChanged();

        adapterTwo = new CScheduleAdapter(getApplicationContext(), dataListTwo,this, this, this,this);
        recyclerViewTwo.setAdapter(adapterTwo);
        recycleVisibility();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onEditClick(ClassScheduleItem scheduleItem) {

    }

    @Override
    public void onDeleteClick(ClassScheduleItem scheduleItem) {

    }

    @Override
    public void onCopyClick(ClassScheduleItem scheduleItem) {

    }

    private void showSaveAllSchedule() {

        dialog = new Dialog(SaveCurrentSchedule.this);
        dialog.setContentView(R.layout.custom_dialog_save_schedule);
        dialog.setCancelable(true);


        Button dsave = dialog.findViewById(R.id.addTask);
        TextInputEditText routineText = dialog.findViewById(R.id.routineNameId);
        TextView saveText = dialog.findViewById(R.id.currentScheduleSaveId);
        saveText.setText("Provide a Routine Name to save All Current Schedules");

        Button currentSchedule = dialog.findViewById(R.id.submitButtonId);
        MaterialButton qrBarCode = dialog.findViewById(R.id.btnRoutineSave);

        routineName = routineText.getText().toString();
        if (routineName.isEmpty()){
            routineName = "Edu_"+new Unique().getDate()+" "+new Unique().getTime()+" schedule";

        }

        dsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        currentSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAndSaveSchedule(routineName,routine);
                
//                startActivity(new Intent(getApplicationContext(), SaveCurrentSchedule.class));
                dialog.dismiss();
            }
        });
        qrBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAndSaveSchedule(routineName,routine);
//                startActivity(new Intent(getApplicationContext(), ScanRoutine.class));
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void getAndSaveSchedule(String routineName, Routine routine) {
        itemList.clear();
        ScheduleD scheduleD = new ScheduleD(getApplicationContext());
        itemList = scheduleD.getAllSchedule();

        if (itemList.isEmpty()){
            deleteAndSaveAllSchedule(dataList);
        }else {
            saveCurrentScheduleRoutine(routineName,itemList,dataList);
        }

    }


    private void saveCurrentScheduleRoutine(String routineName, List<ScheduleItem> itemList, List<ClassScheduleItem> itemListData) {
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

            convertAndSave(routinei,itemList);
            deleteAndSaveAllSchedule(itemListData);
            Toast.makeText(this,"Routine Successfully Saved, Thanks!",Toast.LENGTH_SHORT).show();

        }

    }

    private void deleteAndSaveAllSchedule(List<ClassScheduleItem> dataList) {
        if (dataList.isEmpty()){
            Toast.makeText(this,"Your List is Empty! Firstly, You have to add at least one Schedule.",Toast.LENGTH_SHORT).show();
        }else {

            ScheduleD scheduleD = new ScheduleD(getApplicationContext());
            int r = scheduleD.deleteAllScheduleFromLocal(user.getUniqueId());

//            if (r!=0){

                for (ClassScheduleItem item:dataList){

                    ScheduleItem scheduleItem = new ScheduleItem();
                    scheduleItem.setTId(user.getUniqueId());
                    scheduleItem.setStdId(user.getStdId());
                    scheduleItem.setUniqueId(new Unique().uId());
                    scheduleItem.setT_name(item.getT_name());
                    scheduleItem.setSub_name(item.getSub_name());
                    scheduleItem.setSub_code(item.getSub_code());
                    scheduleItem.setDay(item.getDay());
                    scheduleItem.setRoom(item.getRoom());
                    scheduleItem.setSection(item.getSection());
                    scheduleItem.setSId(user.getSId());
                    scheduleItem.setsId(user.getSId());
                    scheduleItem.setTemp_code(item.getTemp_code());
                    scheduleItem.setTemp_num(new Unique().generateUniqueID());
                    scheduleItem.setStart_time(item.getStart_time());
                    scheduleItem.setEnd_time(item.getEnd_time());


                    long rs = scheduleD.insertSchedule(scheduleItem);

                }

//            }

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

        }

    }


    private void showSaveSchedule() {

        dialog = new Dialog(SaveCurrentSchedule.this);
        dialog.setContentView(R.layout.custom_dialog_save_schedule);
        dialog.setCancelable(true);


        Button dsave = dialog.findViewById(R.id.addTask);
        TextInputEditText routineText = dialog.findViewById(R.id.routineNameId);
        TextView saveText = dialog.findViewById(R.id.currentScheduleSaveId);
        saveText.setText("Provide a Routine Name to save Current Schedules List");

        Button currentSchedule = dialog.findViewById(R.id.submitButtonId);
        MaterialButton qrBarCode = dialog.findViewById(R.id.btnRoutineSave);

        routineName = routineText.getText().toString();
        if (routineName.isEmpty()){
            routineName = "Edu_"+new Unique().getDate()+" "+new Unique().getTime()+" schedule";

        }

        dsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        currentSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAndSaveScheduleTwo(routineName,routine);
//                startActivity(new Intent(getApplicationContext(), SaveCurrentSchedule.class));
                dialog.dismiss();
            }
        });
        qrBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAndSaveScheduleTwo(routineName,routine);
//                startActivity(new Intent(getApplicationContext(), ScanRoutine.class));
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void getAndSaveScheduleTwo(String routineName, Routine routineO) {

        itemList.clear();
        ScheduleD scheduleD = new ScheduleD(getApplicationContext());
        itemList = scheduleD.getAllSchedule();

        if (itemList.isEmpty()){
            deleteAndSaveAllSchedule(dataListTwo);
        }else {
            saveCurrentScheduleRoutine(routineName,itemList,dataListTwo);
        }

        saveThisScheduleRoutine(routineName,dataListTwo);

    }
    private void saveThisScheduleRoutine(String routineName, List<ClassScheduleItem> itemListData) {
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
        routinei.setTemp_name("CS_"+new Unique().getDate()+" "+new Unique().getTime());
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
            saveThisRoutineSchedule(routinei,itemListData);
            Toast.makeText(this,"Successfully Saved, Thanks!",Toast.LENGTH_SHORT).show();

        }

    }

    private void saveThisRoutineSchedule(Routine routinei, List<ClassScheduleItem> itemListData) {

        if (itemListData.isEmpty()){

        }else {
            for (ClassScheduleItem item: itemListData){
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
        }

    }


}