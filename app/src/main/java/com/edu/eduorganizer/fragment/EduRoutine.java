package com.edu.eduorganizer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.adapter.ItemTouchListener;
import com.edu.eduorganizer.adapter.RoutineAdapter;
import com.edu.eduorganizer.adapter.SchoolRoutineAdapter;
import com.edu.eduorganizer.adapter.ViewRoutineTouchHelper;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.routine.CScheduleD;
import com.edu.eduorganizer.routine.ClassScheduleItem;
import com.edu.eduorganizer.routine.EditRoutine;
import com.edu.eduorganizer.routine.NewRoutine;
import com.edu.eduorganizer.routine.Routine;
import com.edu.eduorganizer.routine.RoutineD;
import com.edu.eduorganizer.routine.SaveCurrentSchedule;
import com.edu.eduorganizer.routine.ScanRoutine;
import com.edu.eduorganizer.routine.ShareRoutine;
import com.edu.eduorganizer.routine.daily.DailyRoutine;
import com.edu.eduorganizer.schedule.ScheduleItem;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.school.SchoolDAO;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


public class EduRoutine extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, ItemTouchListener, SchoolRoutineAdapter.OnEditClickListener, SchoolRoutineAdapter.OnDeleteClickListener, SchoolRoutineAdapter.OnCopyClickListener, SchoolRoutineAdapter.OnSetScheduleListener{

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
    private DatabaseReference routineRef = firebaseDatabase.getReference("routine");

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
    List<ClassScheduleItem> dataListClass;
    SchoolRoutineAdapter adapter;
    ScheduleItem androidData;
    SearchView searchView;
    private RelativeLayout rootVieww;

    private Dialog dialog;

    private static final String ARG_DAY_OFFSET = "dayOffset";
    private FloatingActionButton fab;
    private int dayOffset;

    public EduRoutine() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edu_routine, container, false);

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

        dataList = new ArrayList<>();
        dataListClass = new ArrayList<>();

        rootVieww = view.findViewById(R.id.allStdPage);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecoration);

        ItemTouchHelper.SimpleCallback simpleCallback = new ViewRoutineTouchHelper(0, ItemTouchHelper.LEFT, this);
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

        Routine routine = new Routine();
        routine.setTemp_name("temp 1");
        dataList.add(routine);

        adapter = new SchoolRoutineAdapter(getContext(), dataList,this, this, this,this, this);
        recyclerView.setAdapter(adapter);

//        fetchRoutinesFromLocal();
        if (internet.isInternetConnection()){
            fetchRoutinesFromOnline();
//            fetchRoutinesFromOnline();
//            fetchSchedules();
        }else {
            fetchRoutinesFromLocal();
//            fetchDepsWithLocal();
//            fetchSessionsWithLocal();
        }

        Button addRoutine = view.findViewById(R.id.addRoutineId);
        Button downloadRoutine = view.findViewById(R.id.downloadRoutineId);
        addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewRoutine.class));

            }
        });
        downloadRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ScanRoutine.class));

            }
        });

        return view;
    }





    private void fetchRoutinesFromOnline() {

        fetchRoutine();
        if (dataList.isEmpty()){
            fetchRoutinesFromLocal();
        }else {
            if (!dataList.isEmpty()){
                saveRoutineWithSchedule(dataList);
            }
        }
        adapter.notifyDataSetChanged();
        adapter = new SchoolRoutineAdapter(getContext(), dataList,this, this, this,this, this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchRoutine() {

        valueEventListener = routineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    Routine routine = itemSnapshot.getValue(Routine.class);
                    if (routine != null && routine.getuId().equals(sId) || routine.getT_id().equals(sId)) {
                        routine.setKey(itemSnapshot.getKey());
                        dataList.add(routine);
                    }
                }
                if (dataList.isEmpty()){
                    fetchRoutinesFromLocal();
                }else {
                    if (!dataList.isEmpty()){
                        saveRoutineWithSchedule(dataList);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void fetchRoutinesFromLocal() {
//        Toast.makeText(getContext(), "Data Retrieving", Toast.LENGTH_SHORT).show();
        dataList.clear();
        RoutineD routineD = new RoutineD(database);
        dataList = routineD.getAllRoutine(user.getSId());

        adapter.notifyDataSetChanged();
        adapter = new SchoolRoutineAdapter(getContext(), dataList,this, this, this,this,this);
        recyclerView.setAdapter(adapter);
    }


    private void fetchRoutines() {

        valueEventListener = routineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    Routine routine = itemSnapshot.getValue(Routine.class);
                    if (routine != null && routine.getSId().equals(sId)) {
                        routine.setKey(itemSnapshot.getKey());
                        dataList.add(routine);
                    }
                }
                if (dataList.isEmpty()){
                    fetchRoutinesFromLocal();
                }else {
                    if (!dataList.isEmpty()){
                        saveRoutineWithSchedule(dataList);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        if (dataList.isEmpty()){
//            fetchRoutinesFromLocal();
//        }else {
//            saveRoutineWithSchedule(dataList);
//        }
    }

    private void saveRoutineWithSchedule(List<Routine> dataList) {
        for (Routine item:dataList){
            if (item!=null){
                try {
                    RoutineD routineD = new RoutineD(database);
                    routineD.insertRoutine(item);
                }catch (Exception e){

                }

            }
        }
        getScheduleByRoutine(dataList);
    }

    private void getScheduleByRoutine(List<Routine> dataList) {
        for (Routine item: dataList){
            if (item!=null){
                fetchAllSchedule(item.getTemp_num());
            }
        }
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

                    dataListClass.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ClassScheduleItem scheduleItem = snapshot.getValue(ClassScheduleItem.class);

                        if (scheduleItem != null) {
                            dataListClass.add(scheduleItem);
                        }
                    }

                    saveSchedules(dataListClass);
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
            Toast.makeText(getContext(),"Schedules Not Found",Toast.LENGTH_SHORT).show();
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
        List<Routine> dataSearchList = new ArrayList<>();
        for (Routine data : dataList){
            if (data.getTemp_name().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()){
            Toast.makeText(getContext(), "Routine Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof RoutineAdapter.MyViewHolder3){
            String studentDelete = dataList.get(viewHolder.getAdapterPosition()).getTemp_name();
            Routine routine = dataList.get(viewHolder.getAdapterPosition());
            int indexDelete = viewHolder.getAdapterPosition();

            adapter.removeItem(indexDelete);

            Snackbar snackbar = Snackbar.make(rootVieww, studentDelete+" removed", Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.undoItem(routine, indexDelete);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

    @Override
    public void onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    @Override
    public void onEditClick(Routine routine) {
        Toast.makeText(getContext(),"Edit Button is clicked "+routine.getStdId(),Toast.LENGTH_SHORT).show();
//        showInternetDialog();

        Intent intent1 = new Intent(getContext(), EditRoutine.class);
        intent1.putExtra("routine",routine);
        startActivity(intent1);

    }

    @Override
    public void onDeleteClick(Routine routine) {
        Toast.makeText(getContext(),"Delete Button is clicked "+routine.getStdId(),Toast.LENGTH_SHORT).show();

        showCustomDialog(routine);

    }

    private void showCustomDialog(Routine routine) {
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


                if (routine.getSync_status()!=0||routine.getSync_key()!=null){

                    if (internet.isInternetConnection()){
                        deleteRoutineFromOnline(routine);
                    }else {
                        Toast.makeText(getContext(),"Please Connect Your Internet Data Connection "+routine.getTemp_name(),Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                }else {
                    deleteRoutine(routine);
                    dialog.dismiss();
                }



            }
        });

        dialog.show();
    }

    private void deleteRoutineFromOnline(Routine routine) {
        String uniqueIdToDelete = routine.getUniqueId();

        DatabaseReference routineToDeleteRef = routineRef.child(uniqueIdToDelete);

        routineToDeleteRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteRoutine(routine);
                        deleteAllSchedule(routine);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Deletion failed
                        // Handle the error here, e.g., show an error message to the user.
                    }
                });
    }

    private void deleteAllSchedule(Routine routine) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String databasePath = "classSchedule";
        Query query = databaseReference.child(databasePath).orderByChild("temp_num").equalTo(routine.getTemp_num());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    // Delete the item(s) that match the criteria
                    itemSnapshot.getRef().removeValue();
                }
                deleteAllScheduleFromLocal(routine);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors or cancellation here
            }
        });
    }

    private void deleteAllScheduleFromLocal(Routine routine) {
        CScheduleD scheduleD = new CScheduleD(getContext());
        scheduleD.deleteAllScheduleWithTempCode(routine.getTemp_code(),routine.getTemp_num());

    }

    private void deleteRoutine(Routine routine) {
        RoutineD routineD = new RoutineD(getContext());
        routineD.deleteRoutine(routine.getUniqueId());
        deleteAllScheduleFromLocal(routine);
        fetchRoutinesFromLocal();
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
        fetchRoutinesFromLocal();
        Log.d("eee", "onResume() called");
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.fab){
            startActivity(new Intent(getContext(), DailyRoutine.class));
        }
    }

    @Override
    public void onCopyClick(Routine routine) {
        Intent intent1 = new Intent(getContext(), ShareRoutine.class);
        intent1.putExtra("routine",routine);
        startActivity(intent1);
    }


    @Override
    public void onSetClick(Routine routine) {
        Intent intent1 = new Intent(getContext(), SaveCurrentSchedule.class);
        intent1.putExtra("routine",routine);
        startActivity(intent1);
    }




}