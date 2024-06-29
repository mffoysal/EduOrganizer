package com.edu.eduorganizer.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.adapter.ItemTouchListener;
import com.edu.eduorganizer.adapter.NoteAdapter;
import com.edu.eduorganizer.adapter.RoutineAdapter;
import com.edu.eduorganizer.adapter.ViewNoteTouchHelper;
import com.edu.eduorganizer.adapter.ViewRoutineTouchHelper;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.entity.Note;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.note.NoteD;
import com.edu.eduorganizer.routine.CScheduleD;
import com.edu.eduorganizer.routine.NewRoutine;
import com.edu.eduorganizer.routine.Routine;
import com.edu.eduorganizer.routine.RoutineD;
import com.edu.eduorganizer.routine.ScanRoutine;
import com.edu.eduorganizer.schedule.ScheduleItem;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.task.TaskD;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.edu.eduorganizer.workspace.WorkSpace;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Notes extends Fragment implements AdapterView.OnItemClickListener, NoteAdapter.OnEditClickListener, NoteAdapter.OnDeleteClickListener, NoteAdapter.OnCopyClickListener, NoteAdapter.OnSetScheduleListener, NoteAdapter.OnDoneClickListener, NoteAdapter.OnRemindClickListener, ItemTouchListener {

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
    private DatabaseReference noteRef = firebaseDatabase.getReference("note");

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
    List<Note> dataList;
    NoteAdapter adapter;
    Note androidData, note;
    SearchView searchView;
    private RelativeLayout rootVieww;

    private Dialog dialog;

    private static final String ARG_DAY_OFFSET = "dayOffset";
    private FloatingActionButton fab;
    private int dayOffset;

    public Notes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_note, container, false);

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


        rootVieww = view.findViewById(R.id.allStdPage);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecoration);

        ItemTouchHelper.SimpleCallback simpleCallback = new ViewNoteTouchHelper(0, ItemTouchHelper.LEFT, this);
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

        Note routine = new Note();
        routine.setTask_name("Farhad Foysal");
        routine.setDone(1);
        dataList.add(routine);

        adapter = new NoteAdapter(getContext(), dataList,this, this, this,this, this,this,this);
        recyclerView.setAdapter(adapter);

//        fetchRoutinesFromLocal();
        if (internet.isInternetConnection()){
            fetchNotesFromLocal();
//            fetchRoutinesFromOnline();
//            fetchSchedules();
        }else {
            fetchNotesFromLocal();
//            fetchDepsWithLocal();
//            fetchSessionsWithLocal();
        }

        Button addRoutine = view.findViewById(R.id.addRoutineId);
        Button downloadRoutine = view.findViewById(R.id.downloadRoutineId);
        addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), NewRoutine.class));

                showBottomDialog();

            }
        });
        downloadRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WorkSpace.class));

            }
        });


        return view;
    }


    private void fetchNotesFromLocal() {
//        Toast.makeText(getContext(), "Data Retrieving", Toast.LENGTH_SHORT).show();
        dataList.clear();
        NoteD routineD = new NoteD(getContext());
        dataList = routineD.getAllNote();

        adapter.notifyDataSetChanged();
        adapter = new NoteAdapter(getContext(), dataList,this, this, this,this,this,this,this);
        recyclerView.setAdapter(adapter);
    }


    private void fetchNotes() {

        valueEventListener = noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    Note routine = itemSnapshot.getValue(Note.class);
                    if (routine != null && routine.getSId().equals(sId)) {
                        routine.setKey(itemSnapshot.getKey());
                        dataList.add(routine);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onEditClick(Note note) {

    }

    @Override
    public void onDeleteClick(Note note) {
        NoteD noteD = new NoteD(getContext());
        noteD.deleteNote(note.getUniqueId());

        fetchNotesFromLocal();
    }

    @Override
    public void onCopyClick(Note note) {

    }

    @Override
    public void onSetClick(Note note) {

    }

    @Override
    public void onDoneClick(Note note) {

        NoteD noteD = new NoteD(getContext());
        if (note.getDone()!=0){
            note.setDone(0);
        }else {
            note.setDone(1);
        }
        noteD.updateNote(note);
        fetchNotesFromLocal();
    }

    @Override
    public void onRemindClick(Note note) {

    }


    @Override
    public void onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {

    }
    private void searchList(String text){
        List<Note> dataSearchList = new ArrayList<>();
        for (Note data : dataList){
            if (data.getTask_name().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()){
            Toast.makeText(getContext(), "Note Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }

    private void deleteNote(Note routine) {
        NoteD routineD = new NoteD(getContext());
        routineD.deleteNote(routine.getUniqueId());
//        deleteAllTaskFromLocal(routine);
        fetchNotesFromLocal();
    }
    private void deleteAllNotesFromLocal(Note routine) {
        TaskD scheduleD = new TaskD(getContext());
//        scheduleD.deleteAllScheduleWithTempCode(routine.getTemp_code(),routine.getTemp_num());

    }
    @Override
    public void onResume() {
        super.onResume();
        fetchNotesFromLocal();
        Log.d("eee", "onResume() called");
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof RoutineAdapter.MyViewHolder3){
            String studentDelete = dataList.get(viewHolder.getAdapterPosition()).getTask_name();
            Note routine = dataList.get(viewHolder.getAdapterPosition());
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


    private void showBottomDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.session_bottom_sheet);

        ImageView cancelButton2 = dialog.findViewById(R.id.cancelButton2);
        Button saveDep = dialog.findViewById(R.id.signUpBtn);
        Button save = dialog.findViewById(R.id.addRoutineId);
        EditText noteTitle = dialog.findViewById(R.id.notes_title_text);
        EditText noteDetails = dialog.findViewById(R.id.notes_content_text);
        TextInputEditText noteCategory = dialog.findViewById(R.id.categoryId);
        TextInputEditText noteLocation = dialog.findViewById(R.id.categoryId);
        MaterialButton dateTime = dialog.findViewById(R.id.dateAndTimeBtn);
        TextView today = dialog.findViewById(R.id.todayId);
        dateTime.setText(new Unique().getDateTime());
        today.setText("Today: "+new Unique().getDate()+" "+new Unique().getTime()+" "+getDayNameFromDate()+" ");

        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        saveDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = noteTitle.getText().toString();
                String location = noteLocation.getText().toString();
                String category = noteCategory.getText().toString().trim();
                String details = noteDetails.getText().toString().trim();
                String date = dateTime.getText().toString().trim();

                Note note1 = new Note();
                note1.setUniqueId(new Unique().uId());
                note1.setUser_id(user.getUserId());
                note1.setuId(new Unique().userId());
                note1.setDone(0);
                note1.setTask_id(new Unique().generateHexCode());
                note1.setTask_code(""+new Unique().unique_id());
                note1.setTask_name(name);
                note1.setTask_details(details);
                note1.setTask_location(location);
                note1.setLink(category);
                note1.setUrl("");
                note1.setCalendar(new Unique().getDate());
                note1.setDateTime(date);
                note1.setDay(getDayNameFromDate());
                note1.setTime(new Unique().getTime());
                note1.setStdId(user.getStdId());
                note1.setSId(user.getSId());
                note1.setScheduleId("");

                if (name.isEmpty()){
                    noteTitle.setError("Password cannot be empty");
                    noteTitle.requestFocus();
                } else{

                    if (internet.isInternetConnection()){
                        saveNote(note1);
                    }else {
                        saveNote(note1);

                    }

                }

                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = noteTitle.getText().toString();
                String location = noteLocation.getText().toString();
                String category = noteCategory.getText().toString().trim();
                String details = noteDetails.getText().toString().trim();
                String date = dateTime.getText().toString().trim();

                Note note1 = new Note();
                note1.setUniqueId(new Unique().uId());
                note1.setUser_id(user.getUserId());
                note1.setuId(new Unique().userId());
                note1.setDone(0);
                note1.setTask_id(new Unique().generateHexCode());
                note1.setTask_code(""+new Unique().unique_id());
                note1.setTask_name(name);
                note1.setTask_details(details);
                note1.setTask_location(location);
                note1.setLink(category);
                note1.setUrl("");
                note1.setCalendar(new Unique().getDate());
                note1.setDateTime(date);
                note1.setDay(getDayNameFromDate());
                note1.setTime(new Unique().getTime());
                note1.setStdId(user.getStdId());
                note1.setSId(user.getSId());
                note1.setScheduleId("");

                if (name.isEmpty()){
                    noteTitle.setError("Password cannot be empty");
                    noteTitle.requestFocus();
                } else{

                    if (internet.isInternetConnection()){
                        saveNote(note1);
                    }else {
                        saveNote(note1);

                    }

                }

                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);



    }

    private void saveNote(Note note1) {

        NoteD noteD =new NoteD(getContext());
        long rs = noteD.insertNote(note1);
        if(rs==-1){
            Toast.makeText(getContext(),"Note Data Not inserted! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-3){
            Toast.makeText(getContext(),"Note already created! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-2){
            Toast.makeText(getContext(),"Error Occurred! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        } else {
            fetchNotesFromLocal();

            Toast.makeText(getContext(),"Note Successfully Saved, Thanks!",Toast.LENGTH_SHORT).show();
        }

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

    public static Date convertStringToDate(String dateString, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(dateString);
    }

    public static String getDayNameFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(date);
    }

    public static String getDayNameFromDate() {
        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(currentDate);
    }
}