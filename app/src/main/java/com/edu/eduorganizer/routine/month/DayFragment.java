package com.edu.eduorganizer.routine.month;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.adapter.ItemTouchListener;
import com.edu.eduorganizer.adapter.ScheduleAdapter;
import com.edu.eduorganizer.adapter.ViewItemTouchHelper;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.schedule.ScheduleD;
import com.edu.eduorganizer.schedule.ScheduleItem;
import com.edu.eduorganizer.routine.day.DailyRoutine;
import com.edu.eduorganizer.schedule.DuplicateSchedule;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.school.SchoolDAO;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, ItemTouchListener, ScheduleAdapter.OnEditClickListener, ScheduleAdapter.OnDeleteClickListener, ScheduleAdapter.OnCopyClickListener {



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
    List<ScheduleItem> dataList;
    ScheduleAdapter adapter;
    ScheduleItem androidData;
    SearchView searchView;
    private RelativeLayout rootVieww;

    private Dialog dialog;

    private static final String ARG_DAY_OFFSET = "dayOffset";
    private FloatingActionButton fab;
    private int dayOffset;

    public DayFragment() {
    }

    public static DayFragment newInstance(int dayOffset) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY_OFFSET, dayOffset);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day, container, false);

        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        TextView dateTextView = rootView.findViewById(R.id.dateTextView);
        dayOffset = getArguments().getInt(ARG_DAY_OFFSET);
        dateTextView.setText(getFormattedDate(dayOffset)+" "+getFormattedDay(dayOffset));

        databaseManager = new DatabaseManager(getContext());
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
        logout = new Logout(getContext());
        if (!logout.isLoggedIn()){
            Intent intent = new Intent(getContext(), LoginPage.class);
            startActivity(intent);
            getActivity().finish();
        }
        intent = getActivity().getIntent();
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
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
        internet = new Internet(getContext());
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(Network.ACTION_CONNECTIVITY_CHANGE)) {
                    boolean isConnected = intent.getBooleanExtra(Network.EXTRA_CONNECTIVITY_STATUS, false);
                    if (isConnected) {
//                        Toast.makeText(getApplicationContext(),"Internet Connected",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();

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



        rootVieww = rootView.findViewById(R.id.allStdPage);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        searchView = rootView.findViewById(R.id.search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecoration);

        ItemTouchHelper.SimpleCallback simpleCallback = new ViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();

        androidData = new ScheduleItem();
        androidData.setSub_name("Farhad Foysal 1");
        androidData.setStdId("221005312");
        androidData.setSub_code("cse313");
        androidData.setStart_time("10:30 am");
        androidData.setEnd_time("11:30 am");
//        dataList.add(androidData);

        ScheduleItem androidData2 = new ScheduleItem();
        androidData2.setSub_name("Moni");
        androidData2.setStdId("12221");
        androidData2.setSub_code("cse312");
        androidData2.setStart_time("11:30 am");
        androidData2.setEnd_time("12:30 am");
//        dataList.add(androidData2);

//        androidData = new student();
//        androidData.setstdName("Farhad Foysal 2");
//        androidData.setstdId("221005313");
//        dataList.add(androidData);
//
//        androidData = new student();
//        androidData.setstdName("Farhad Foysal 3");
//        androidData.setstdId("221005314");
//        dataList.add(androidData);
//
//        androidData = new student();
//        androidData.setstdName("Farhad Foysal 4");
//        androidData.setstdId("221005315");
//        dataList.add(androidData);

        adapter = new ScheduleAdapter(getContext(), dataList,this, this, this,this);
        recyclerView.setAdapter(adapter);

//        fetchSchedulesFromLocal();
        if (internet.isInternetConnection()){
            fetchSchedulesFromLocal();
//            fetchSchedules();
        }else {
            fetchSchedulesFromLocal();
//            fetchDepsWithLocal();
//            fetchSessionsWithLocal();
        }





        return rootView;
    }

    private void fetchSchedulesFromLocal() {
//        Toast.makeText(getContext(), "Data Retrieving", Toast.LENGTH_SHORT).show();
        dataList.clear();
        ScheduleD scheduleD = new ScheduleD(getContext());
        dataList = scheduleD.getAllSchedule(getFormattedDay(dayOffset));

        adapter.notifyDataSetChanged();
        adapter = new ScheduleAdapter(getContext(), dataList,this, this, this,this);
        recyclerView.setAdapter(adapter);
    }


    private void fetchSchedules() {

        valueEventListener = studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    ScheduleItem scheduleItem = itemSnapshot.getValue(ScheduleItem.class);
                    if (scheduleItem != null && scheduleItem.getsId().equals(sId)) {
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
        List<ScheduleItem> dataSearchList = new ArrayList<>();
        for (ScheduleItem data : dataList){
            if (data.getSub_name().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()){
            Toast.makeText(getContext(), "Schedule Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ScheduleAdapter.MyViewHolder3){
            String studentDelete = dataList.get(viewHolder.getAdapterPosition()).getSub_name();
            ScheduleItem scheduleItem = dataList.get(viewHolder.getAdapterPosition());
            int indexDelete = viewHolder.getAdapterPosition();

            adapter.removeItem(indexDelete);

            Snackbar snackbar = Snackbar.make(rootVieww, studentDelete+" removed", Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.undoItem(scheduleItem, indexDelete);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    @Override
    public void onEditClick(ScheduleItem scheduleItem) {
        Toast.makeText(getContext(),"Edit Button is clicked "+scheduleItem.getStdId(),Toast.LENGTH_SHORT).show();
        showInternetDialog();
    }

    @Override
    public void onDeleteClick(ScheduleItem scheduleItem) {
        Toast.makeText(getContext(),"Delete Button is clicked "+scheduleItem.getStdId(),Toast.LENGTH_SHORT).show();

        showCustomDialog(scheduleItem);

    }

    private void showCustomDialog(ScheduleItem scheduleItem) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.warning_alert_dialog);
        dialog.setCancelable(true);


        TextView wText = dialog.findViewById(R.id.textTitle);
        wText.setText("Are You Sure!");
        TextView mText = dialog.findViewById(R.id.textMessage);
        mText.setText("Please Press Yes to Proceed Delete");
        Button delete = dialog.findViewById(R.id.alertButtonYes);
        Button cancel = dialog.findViewById(R.id.alertButtonNo);
        delete.setText("YES DEL");
        cancel.setText("CANCEL");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteSchedule(scheduleItem);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void deleteSchedule(ScheduleItem scheduleItem) {
        ScheduleD scheduleD = new ScheduleD(getContext());
        scheduleD.deleteSchedule(scheduleItem.getUniqueId());
        fetchSchedulesFromLocal();
    }

    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }



    private String getFormattedDate(int dayOffset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, dayOffset);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    private String getFormattedDay(int dayOffset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, dayOffset);
        Date date = cal.getTime();

        // Get the three-letter day name (e.g., Mon, Tue, etc.)
//        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dayNameFormat.format(date);
    }
    private String getFormattedDay(Date date) {

        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        String dayName = dayNameFormat.format(date);

        return dayName;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchSchedulesFromLocal();
        Log.d("eee", "onResume() called");
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.fab){
            startActivity(new Intent(getContext(), DailyRoutine.class));
        }
    }

    @Override
    public void onCopyClick(ScheduleItem scheduleItem) {
        startActivity(new Intent(getContext(), DuplicateSchedule.class));
    }
}
