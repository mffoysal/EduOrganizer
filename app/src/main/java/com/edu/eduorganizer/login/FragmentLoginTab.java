package com.edu.eduorganizer.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.edu.eduorganizer.ErrorPanel;
import com.edu.eduorganizer.Logout;
import com.edu.eduorganizer.R;
import com.edu.eduorganizer.User;
import com.edu.eduorganizer.db.DatabaseManager;
import com.edu.eduorganizer.internet.Internet;
import com.edu.eduorganizer.network.Connection;
import com.edu.eduorganizer.network.Network;
import com.edu.eduorganizer.panel.UserPanel;
import com.edu.eduorganizer.unique.Unique;
import com.edu.eduorganizer.user.UserCallback;
import com.edu.eduorganizer.user.UserDAO;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FragmentLoginTab extends Fragment {

    private Logout logout;
    private SQLiteDatabase database;
    private DatabaseManager databaseManager;
    private UserDAO userDAO;
    private Internet internet;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference = fdatabase.getReference("users");

    private DatabaseReference usersRef;
    private UserCallback userCallback;
    private BroadcastReceiver connectivityReceiver;
    private Intent intent;


    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;
    private FirebaseAuth auth;
    TextView forgotPassword;

    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    private User user;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private LoginPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_tab, container, false);

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

        databaseManager = new DatabaseManager(getContext());
        databaseManager.openDatabase();
        database = databaseManager.getDatabase();
//        UserDAO userDAO = new UserDAO(database);
//        userDAO.insertUser(user);
//        List<User> userList = userDAO.getAllUsers();
//        int rowsAffected = userDAO.updateUser(user);
//        int rowsDeleted = userDAO.deleteUser(userId);
//        databaseManager.closeDatabase();
        logout = new Logout(getContext());
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        userCallback = new UserCallback() {
            @Override
            public void onUserRetrieved(User user) {
                // Handle the retrieved user object here
                // For example, update the UI or pass it to another method
                processUser(user);
            }

            @Override
            public void onUserNotFound() {
                // Handle the case where no user matches the given phone number
                // For example, display an error message or take appropriate action
                handleUserNotFound();
            }
        };

        loginEmail = view.findViewById(R.id.login_email);
        loginPassword = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.login_button);
        signupRedirectText = view.findViewById(R.id.signUpRedirectText);
        forgotPassword = view.findViewById(R.id.forgot_password);
        auth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                if (!validatePhone() | !validatePassword()) {
                } else {
                    if (internet.isInternetConnection()){
//                        loginMethod(email,pass);
                        checkUser();
                    }else {
                        loginWithLocal(email,pass);
                    }
                }

            }
        });
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), createUser.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.activity_password_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(getContext(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(), "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            try {
                                task.getResult(ApiException.class);
                                getActivity().finish();
                                Intent intent = new Intent(getContext(), UserPanel.class);
                                startActivity(intent);
                            } catch (ApiException e){
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


        return view;
    }

    private void handleUserNotFound() {

    }

    private void processUser(User u) {
        user = u;
        logout.saveUser(user);
        Log.e("UserFound","User  save from login "+logout.getUser().getPhone());
        startActivity(intent);
        getActivity().finish();

//        Log.e("UserFound","User  Found here "+logout.getUser().getPhone());
    }

    private void loginWithLocal(String a, String b) {

        int r;
        userDAO = new UserDAO(database);
        r=userDAO.checkUser(a,b);
        if (r!=0){
            user = userDAO.getUser(a);
            if (user.getSId()==null){
//                intent = new Intent(getApplicationContext(), createSchool.class);
            }else {
                intent = new Intent(getContext(), UserPanel.class);
            }
            Log.e("UserFound","User Succesfully Login");
            logout.setLoggedIn(true);
            logout.setLoggedUser(a,a);
            user = userDAO.getUser(a);

            logout.saveUser(user);
            intent.putExtra("user",a);
            startActivity(intent);
            getActivity().finish();
        }else {
            Toast.makeText(getContext(),"User not Found",Toast.LENGTH_SHORT).show();
        }

    }

    private void loginMethod(String email, String pass) {

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                if (!email.isEmpty()) {
            if (!pass.isEmpty()) {
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), UserPanel.class));
                                getActivity().finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                loginPassword.setError("Empty fields are not allowed");
            }
        } else if (email.isEmpty()) {
            loginEmail.setError("Empty fields are not allowed");
        } else {
            loginEmail.setError("Please enter correct email");
        }

    }





    public Boolean validatePhone() {
        String val = loginEmail.getText().toString();
        if (val.isEmpty()) {
            loginEmail.setError("Username cannot be empty");
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginEmail.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("phone").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginEmail.setError(null);
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Retrieve the child data using the userSnapshot
                        String passwordFromDB = userSnapshot.child("pass").getValue(String.class);
                        String emailFromDB = userSnapshot.child("email").getValue(String.class);
                        String uIdFromDB = userSnapshot.child("userId").getValue(String.class);
                        String sIdFromDB = userSnapshot.child("sid").getValue(String.class);
                        String stdIdFromDB = userSnapshot.child("stdId").getValue(String.class);
                        String stdNameFromDB = userSnapshot.child("stdName").getValue(String.class);

//                        Toast.makeText(getApplicationContext(), "" + sIdFromDB, Toast.LENGTH_SHORT).show();

                        // Perform actions with the retrieved data
                        if (passwordFromDB.equals(userPassword)) {
                            // Valid credentials
                            loginEmail.setError(null);

                            if (sIdFromDB == null) {
                                intent = new Intent(getContext(), ErrorPanel.class);
                            } else {
                                intent = new Intent(getContext(), UserPanel.class);
                                intent.putExtra("sId", sIdFromDB);
                            }
                            userDAO = new UserDAO(database);
                            logout.setLoggedIn(true);
                            intent.putExtra("user",userUsername);
                            logout.setLoggedUser(userUsername,userUsername);

                            getUserData(userUsername);
                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("pass", passwordFromDB);
                            intent.putExtra("uId", uIdFromDB);
                            intent.putExtra("stdId", stdIdFromDB);
                            intent.putExtra("stdName", stdNameFromDB);

//                            startActivity(intent);
//                            finish();

                            return; // Exit the loop and method since the user is found
                        }
                    }
                    // Invalid password
                    loginPassword.setError("Invalid Credentials");
                    loginPassword.requestFocus();
                } else {
                    // User does not exist
                    loginWithLocal(userUsername,userPassword);
                    loginEmail.setError("User does not exist"+new Unique().uniqueId());
                    loginEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the cancellation
            }
        });
    }


    public void checkUserrr(){
        String userUsername = loginEmail.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("phone").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginEmail.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("pass").getValue(String.class);
                    //                        String nameFromDB = snapshot.child(userUsername).child("stdName").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
//                        String stdIdFromDB = snapshot.child(userUsername).child("stdId").getValue(String.class);
                    String uIdFromDB = snapshot.child(userUsername).child("userId").getValue(String.class);
                    String sIdFromDB = snapshot.child(userUsername).child("sId").getValue(String.class);

                    Toast.makeText(getContext(),""+uIdFromDB,Toast.LENGTH_SHORT).show();

                    if (passwordFromDB.equals(userPassword)) {
                        loginEmail.setError(null);

                        if ("1".equals(sIdFromDB)) {
                            intent = new Intent(getContext(), ErrorPanel.class);
                        } else {
                            intent = new Intent(getContext(), UserPanel.class);
                            intent.putExtra("sId", sIdFromDB);
                        }

//                        intent = new Intent(LoginMainActivity.this, UserPanel.class);
//                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
//                        intent.putExtra("stdId", stdIdFromDB);
                        intent.putExtra("pass", passwordFromDB);
                        intent.putExtra("uId", uIdFromDB);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginEmail.setError("User does not exist"+new Unique().uniqueId());
                    loginEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    public void checkUserr(){
        String userUsername = loginEmail.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("phone").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginEmail.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("pass").getValue(String.class);
                    //                        String nameFromDB = snapshot.child(userUsername).child("stdName").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
//                        String stdIdFromDB = snapshot.child(userUsername).child("stdId").getValue(String.class);
                    String uIdFromDB = snapshot.child(userUsername).child("userId").getValue(String.class);
                    String sIdFromDB = snapshot.child(userUsername).child("sId").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)) {
                        loginEmail.setError(null);

                        if ("1".equals(sIdFromDB)) {
                            intent = new Intent(getContext(), ErrorPanel.class);
                        } else {
                            intent = new Intent(getContext(), UserPanel.class);
                            intent.putExtra("sId", sIdFromDB);
                        }

//                        intent = new Intent(LoginMainActivity.this, UserPanel.class);
//                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
//                        intent.putExtra("stdId", stdIdFromDB);
                        intent.putExtra("pass", passwordFromDB);
                        intent.putExtra("uId", uIdFromDB);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginEmail.setError("User does not exist");
                    loginEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }



    public User getUserData(String userPhone) {
        Query query = usersRef.orderByChild("phone").equalTo(userPhone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        // User found based on the phone number
                        // Pass the user object to the callback
//                        Log.e("UserFound","User FF Found");
                        user = user;
                        userCallback.onUserRetrieved(user);
                        return;
                    }
                }
                // User not found based on the phone number
                userCallback.onUserNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });

        // Return a placeholder user object or null if needed
        return new User();
    }
}