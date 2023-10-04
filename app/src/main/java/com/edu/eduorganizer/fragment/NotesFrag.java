package com.edu.eduorganizer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.MainPanelActivity;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.student.Student;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NotesFrag extends Fragment {

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
    private DatabaseReference sessionRef = firebaseDatabase.getReference("session");
    private DatabaseReference classRef = firebaseDatabase.getReference("class");
    private DatabaseReference subjectRef = firebaseDatabase.getReference("subject");

    private DatabaseReference usersRef;
    private UserCallback userCallback;
    private BroadcastReceiver connectivityReceiver;
    private Intent intent;
    private ActionBar actionbar;

    private User user;
    private String userPhone, sId;
    private School school;
    private SchoolCallback schoolCallback;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText editText1, editText2;
    private Button bt1,bt2;
    private TextView textView1,textView2;

    private Context context;

    private String userId;
    private Student student;

    public NotesFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);


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


        // Remove the Title Bar
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DatabaseManager databaseManager = new DatabaseManager(getContext());
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();
//        UserDAO userDAO = new UserDAO(database);
//        userDAO.insertUser(user);
//        List<User> userList = userDAO.getAllUsers();
//        int rowsAffected = userDAO.updateUser(user);
//        int rowsDeleted = userDAO.deleteUser(userId);
//        databaseManager.closeDatabase();

        tabLayout = view.findViewById(R.id.tabLayId);
        viewPager = view.findViewById(R.id.viewPagerId);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setAdapter(new NotesFrag.myPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);


        intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("student")) {
            student = (Student) intent.getSerializableExtra("student");

            String stdName = student.getStdName();
            String stdPhone = student.getstdPhone();
            String stdId = student.getStdId();
            String sId = student.getSId();

        }







        return view;
    }



    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn!= null && wifiConn.isConnected()) || (dataConn != null && dataConn.isConnected())){
            return true;
        }else {
            return false;
        }

    }

    class myPagerAdapter extends FragmentPagerAdapter {

        String[] text = {"NOTES","TASKS","OTHERS"};

        public myPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            if(position==0){
                return new Note();
            } else if (position==1) {
                return new Task();
            } else if (position==2) {
                return new NoteOther();
            }

            return null;
        }

        @Override
        public int getCount() {
            return text.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return text[position];
        }
    }


}