package com.edu.eduorganizer.note;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.adapter.NoteAdapter;
import com.edu.eduorganizer.adapter.ScheduleAdapter;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.entity.Note;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.routine.Routine;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewNote extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
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


        ImageView cancelButton2 = findViewById(R.id.cancelButton2);
        Button saveDep = findViewById(R.id.signUpBtn);
        Button save = findViewById(R.id.addRoutineId);
        EditText noteTitle = findViewById(R.id.notes_title_text);
        EditText noteDetails = findViewById(R.id.notes_content_text);
        TextInputEditText noteCategory = findViewById(R.id.categoryId);
        TextInputEditText noteLocation = findViewById(R.id.categoryId);
        MaterialButton dateTime = findViewById(R.id.dateAndTimeBtn);
        TextView today = findViewById(R.id.todayId);
        dateTime.setText(new Unique().getDateTime());
        today.setText("Today: "+new Unique().getDate()+" "+new Unique().getTime()+" "+getDayNameFromDate()+" ");


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

            }
        });



    }


    private void saveNote(Note note1) {

        NoteD noteD =new NoteD(getApplicationContext());
        long rs = noteD.insertNote(note1);
        if(rs==-1){
            Toast.makeText(getApplicationContext(),"Note Data Not inserted! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-3){
            Toast.makeText(getApplicationContext(),"Note already created! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        }else if(rs==-2){
            Toast.makeText(getApplicationContext(),"Error Occurred! Try again!  "+rs,Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(),"Note Successfully Saved, Thanks!",Toast.LENGTH_SHORT).show();
            finish();
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