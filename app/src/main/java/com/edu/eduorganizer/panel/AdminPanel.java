package com.edu.eduorganizer.panel;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.databinding.ActivityUserPanelBinding;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.fragment.EduFrag;
import com.edu.eduorganizer.fragment.EduFragAdmin;
import com.edu.eduorganizer.fragment.NotesFrag;
import com.edu.eduorganizer.fragment.RoutineFrag;
import com.edu.eduorganizer.fragment.ScheduleFrag;
import com.edu.eduorganizer.fragment.docsFrag;
import com.edu.eduorganizer.fragment.homeFrag;
import com.edu.eduorganizer.fragment.pdfFrag;
import com.edu.eduorganizer.fragment.phoneFrag;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.note.NewNote;
import com.edu.eduorganizer.routine.NewRoutine;
import com.edu.eduorganizer.routine.SaveScheduleRoutine;
import com.edu.eduorganizer.routine.ScanRoutine;
import com.edu.eduorganizer.schedule.NewSchedule;
import com.edu.eduorganizer.school.School;
import com.edu.eduorganizer.school.SchoolCallback;
import com.edu.eduorganizer.school.SchoolDAO;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.edu.eduorganizer.user.UserDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminPanel extends BaseMenu implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

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

    private DatabaseReference usersRef;
    private UserCallback userCallback;
    private BroadcastReceiver connectivityReceiver;
    private Intent intent;
    private ActionBar actionbar;
    private ActivityUserPanelBinding binding;
    private User user;
    private String userPhone, sId;
    private School school;
    private SchoolCallback schoolCallback;
    private DrawerLayout drawerLayout;

    private Animation fabOpen,fabClose,rotateForward, rotateBackward;
    boolean isOpen = false;
    private FloatingActionButton fabb, fabb1, fabb2, fabb3, fabb4;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPanelBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_user_panel);
        setContentView(binding.getRoot());

        fabb = findViewById(R.id.navAddId);
        fabb1 = findViewById(R.id.fabb1);
        fabb2 = findViewById(R.id.fabb2);
        fabb3 = findViewById(R.id.fabb3);
        fabb4 = findViewById(R.id.fabb4);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open_animation);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close_animation);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fabb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        fabb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoutineDialogue();
            }
        });
        fabb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewNote.class));

            }
        });
        fabb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewSchedule.class));
            }
        });


        replaceFragment(new ScheduleFrag());
        binding.userPanelNavViewId.setBackground(null);
        binding.userPanelNavViewId.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.menu_homeId){
                replaceFragment(new ScheduleFrag());
            } else if (item.getItemId()==R.id.menu_addId) {
                replaceFragment(new homeFrag());
            } else if (item.getItemId()==R.id.menu_callId) {
                replaceFragment(new EduFragAdmin());
            } else if (item.getItemId()==R.id.menu_pdfId) {
                replaceFragment(new RoutineFrag());
            } else if (item.getItemId()==R.id.menu_documentId) {
                replaceFragment(new NotesFrag());
            } else if (item.getItemId()==R.id.navAddId) {
                replaceFragment(new phoneFrag());
            }
            return false;
        });


        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleFrag()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }



        databaseManager = new DatabaseManager(this);
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();

        logout = new Logout(this);
        if (!logout.isLoggedIn()){
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
            finish();
        }
        intent = getIntent();
        actionbar = getSupportActionBar();
        if (intent != null && intent.hasExtra("user")) {
            String url = intent.getStringExtra("user");
            actionbar.setTitle(actionbar.getTitle()+" "+url);
            userPhone = url;
            user = logout.getUser();
            sId=user.getSId();
        }else {
            user = logout.getUser();
            sId=user.getSId();
            actionbar.setTitle(actionbar.getTitle()+" "+logout.getStringPreference("userId"));
        }
        if (intent != null && intent.hasExtra("eduBox")) {
            String url = intent.getStringExtra("eduBox");
            actionbar.setTitle(actionbar.getTitle()+" "+url);
        }
        internet = new Internet(getApplicationContext());
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


        View headerView = navigationView.getHeaderView(0);
        TextView nameView = headerView.findViewById(R.id.navName);
        TextView phoneView = headerView.findViewById(R.id.navEmail);
        CardView cardView = headerView.findViewById(R.id.imgCardId);
        cardView.setOnClickListener(this);
        nameView.setText(user.getStdName());
        phoneView.setText(user.getPhone()+" | "+user.getEmail());


    }

    private void showRoutineDialogue() {

        dialog = new Dialog(AdminPanel.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(true);


        Button dsave = dialog.findViewById(R.id.addTask);
        Button currentSchedule = dialog.findViewById(R.id.submitButtonId);
        Button qrBarCode = dialog.findViewById(R.id.submitButton1Id);
        Button createRoutine = dialog.findViewById(R.id.submitButton3Id);

        dsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        currentSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AdminPanel.this, SaveScheduleRoutine.class));
                dialog.dismiss();
            }
        });
        qrBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AdminPanel.this, ScanRoutine.class));
                dialog.dismiss();
            }
        });
        createRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AdminPanel.this, NewRoutine.class));
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void animateFab(){
        if (isOpen){
            fabb.startAnimation(rotateBackward);
            fabb3.startAnimation(fabClose);
            fabb2.startAnimation(fabClose);
            fabb4.startAnimation(fabClose);
            fabb3.setClickable(false);
            fabb2.setClickable(false);
            fabb4.setClickable(false);
            isOpen=false;
        }else {
            fabb.startAnimation(rotateForward);
            fabb3.startAnimation(fabOpen);
            fabb2.startAnimation(fabOpen);
            fabb4.startAnimation(fabOpen);
            fabb3.setClickable(true);
            fabb2.setClickable(true);
            fabb4.setClickable(true);
            isOpen=true;
        }
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

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.userPanelMainFrameId, fragment);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new homeFrag()).commit();
        } else if (itemId == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new docsFrag()).commit();
        } else if (itemId == R.id.nav_share) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new pdfFrag()).commit();
        } else if (itemId == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new phoneFrag()).commit();
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
            Logout logout = new Logout(getApplicationContext());
            logout.getOut();
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.imgCardId){
            Intent intent = new Intent(getApplicationContext(), UserDetails.class);
//            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra("profile","Farhad Foysal\n+8801585855075");
            intent.putExtra("userData",user);
            startActivity(intent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }



}