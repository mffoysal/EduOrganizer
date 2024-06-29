package com.edu.eduorganizer.routine;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.adapter.ClassScheduleAdapter;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.routine.daily.weekly.admin.EverydayFrag;
import com.edu.eduorganizer.routine.daily.weekly.admin.FridayFragment;
import com.edu.eduorganizer.routine.daily.weekly.admin.MondayFragment;
import com.edu.eduorganizer.routine.daily.weekly.admin.SaturdayFragment;
import com.edu.eduorganizer.routine.daily.weekly.admin.SundayFragment;
import com.edu.eduorganizer.routine.daily.weekly.admin.ThursdayFragment;
import com.edu.eduorganizer.routine.daily.weekly.admin.TuesdayFragment;
import com.edu.eduorganizer.routine.daily.weekly.admin.WednesdayFragment;
import com.edu.eduorganizer.schedule.ScheduleItem;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.school.SchoolDAO;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class EduRoutineSchedule extends AppCompatActivity {

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
    private MaterialButton saveAndDup;
    private ClassScheduleItem scheduleItem;

    private TextView subName, subNamee, subCodee, timee, faculty, roome, sub_code, time, section, teacherFaculty, room, designation, day, phone;

    private Routine routine = new Routine();
    private ViewPager viewPager;

    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_schedule);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

//        setupViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);

        int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int defaultTabIndex = currentDayOfWeek - 1;
        viewPager.setCurrentItem(defaultTabIndex);


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

        dataList = new ArrayList<>();
        if (internet.isInternetConnection()){
            fetchAllSchedule(routine.getTemp_num());

            fetchSchedules();
        }else {
            fetchSchedulesFromLocal();
        }

        setupViewPager(viewPager,routine,dataList);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(defaultTabIndex);

    }

    private void fetchSchedulesFromLocal() {
        //        Toast.makeText(getContext(), "Data Retrieving", Toast.LENGTH_SHORT).show();
        dataList.clear();
        CScheduleD scheduleD = new CScheduleD(database);
        dataList = scheduleD.getAllScheduleByRoutine(routine.getTemp_num(),routine.getTemp_code());

        if (dataList.isEmpty()){
            fetchAllSchedule(routine.getTemp_num());
        }

//        adapter.notifyDataSetChanged();
//        adapter = new ClassScheduleAdapter(getApplicationContext(), dataList,this, this, this,this);
//        recyclerView.setAdapter(adapter);
    }

    private void fetchAllSchedule(String tempCode) {
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

                    saveSchedules(dataList);
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

    private void saveSchedules(List<ClassScheduleItem> dataList) {
//        Toast.makeText(getApplicationContext(),"save schedule"+routineee.getTemp_num(),Toast.LENGTH_SHORT).show();

        if (dataList.isEmpty()){
            Toast.makeText(getApplicationContext(),"Schedules Not Found",Toast.LENGTH_SHORT).show();
        }else {
            for (ClassScheduleItem item: dataList){
//                item.setTId(user.getUniqueId());
//                item.setUniqueId(new Unique().uId());
//                item.setStdId(user.getStdId());
//                item.setSync_key(null);
//                item.setSync_status(0);
//                item.setTemp_code(routineee.getTemp_code());
//                item.setTemp_num(routineee.getTemp_num());

                try {
                    CScheduleD cScheduleD = new CScheduleD(database);
                    cScheduleD.insertSchedule(item);
                }catch (Exception e){

                }

            }

        }

    }

    private void fetchSchedules() {

        valueEventListener = studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    ClassScheduleItem scheduleItem = itemSnapshot.getValue(ClassScheduleItem.class);
                    if (scheduleItem != null && scheduleItem.getUniqueId().equals(sId) && scheduleItem.getTemp_code().equals(routine.getTemp_code()) || scheduleItem.getTemp_num().equals(routine.getTemp_num())) {
                        scheduleItem.setKey(itemSnapshot.getKey());
                        dataList.add(scheduleItem);
                    }
                }
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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


    private void showTimePickerDialogTwo() {
        Calendar calendar = Calendar.getInstance();
        t2Hour = calendar.get(Calendar.HOUR_OF_DAY);
        t2Minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(EduRoutineSchedule.this, android.R.style.Theme_Holo_Dialog_MinWidth ,new TimePickerDialog.OnTimeSetListener() {
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(EduRoutineSchedule.this, android.R.style.Theme_Holo_Dialog_MinWidth ,new TimePickerDialog.OnTimeSetListener() {
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(EduRoutineSchedule.this, new TimePickerDialog.OnTimeSetListener() {
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


    private void setupViewPager(ViewPager viewPager,Routine routine, List<ClassScheduleItem> dataList) {
        EduRoutineViewPagerAdapter adapter = new EduRoutineViewPagerAdapter(getSupportFragmentManager());
//        Log.d("eee",routine.getTemp_code());
        adapter.addFragment(new SundayFragment(routine, dataList), getDayName(1));
        adapter.addFragment(new MondayFragment(routine, dataList), getDayName(2));
        adapter.addFragment(new TuesdayFragment(routine, dataList), getDayName(3));
        adapter.addFragment(new WednesdayFragment(routine, dataList), getDayName(4));
        adapter.addFragment(new ThursdayFragment(routine, dataList), getDayName(5));
        adapter.addFragment(new FridayFragment(routine, dataList), getDayName(6));
        adapter.addFragment(new SaturdayFragment(routine, dataList), getDayName(7));
        adapter.addFragment(new EverydayFrag(routine, dataList), getDayName(8));
        viewPager.setAdapter(adapter);
    }

    private String getDayName(int dayOfWeek) {
        String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Everyday"};
        return dayNames[dayOfWeek - 1];
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchSchedulesFromLocal();
        Log.d("eee", "onResume() called");
    }

}

class EduRoutineViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public EduRoutineViewPagerAdapter(FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
