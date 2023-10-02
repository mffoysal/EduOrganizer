package com.edu.eduorganizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.edu.eduorganizer.login.Login;
import com.edu.eduorganizer.login.LoginPage;
import com.edu.eduorganizer.panel.UserPanel;
import com.edu.eduorganizer.school.School;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Logout {

    private SharedPreferences sharedPreferences;
    private Context context;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "eduBoxLogin";
    private static final String INSTALLER_NAME = "eduBoxInstaller";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn", INSTALLER_ID = "isInstalled";
    private static final String USER_ID="userId", USER_PHONE="userPhone", USER_EMAIL="userEmail", USER_KEY="userKey", SCHOOL_KEY = "schoolKey";

    private String PrefName;

    private String default_valueS = "";
    private int default_valueI = 0;
    private long default_valueL = 0L;
    private float default_valueF = 0.0f;
    private boolean default_valueB = false;
    private Set<String> default_valueSet = Collections.emptySet();
    private Gson gson;


    public Logout(){
        gson = new Gson();
    }
    public Logout(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public Logout(Context context, String prefName){
        this.context = context;
        this.PrefName = prefName;
        sharedPreferences = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public Logout(Context context, String prefName, String s){
        this.context = context;
        this.PrefName = prefName;
        this.default_valueS = s;
        sharedPreferences = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public Logout(Context context, String prefName, int s){
        this.context = context;
        this.PrefName = prefName;
        this.default_valueI = s;
        sharedPreferences = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public Logout(Context context, String prefName, long s){
        this.context = context;
        this.PrefName = prefName;
        this.default_valueL = s;
        sharedPreferences = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public Logout(Context context, String prefName, float s){
        this.context = context;
        this.PrefName = prefName;
        this.default_valueF = s;
        sharedPreferences = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public Logout(Context context, String prefName, boolean s){
        this.context = context;
        this.PrefName = prefName;
        this.default_valueB = s;
        sharedPreferences = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public Logout(Context context, String prefName, Set<String> s){
        this.context = context;
        this.PrefName = prefName;
        this.default_valueSet = s;
        sharedPreferences = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Logout(Context context, String prefName, String s1, int s2, long s3, float s4, boolean s5, Set<String> s6){
        this.context = context;
        this.PrefName = prefName;
        this.default_valueS = s1;
        this.default_valueI = s2;
        this.default_valueL = s3;
        this.default_valueF = s4;
        this.default_valueB = s5;
        this.default_valueSet = s6;
        sharedPreferences = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(User user){
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(USER_KEY,userJson).apply();
    }
    public void saveUser(User user, String key){
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(key,userJson).apply();
    }
    public void saveUser(User user, String pr, String key){
        sharedPreferences = context.getSharedPreferences(pr,Context.MODE_PRIVATE);
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(key,userJson).apply();
    }
    public void saveUser(Context context, User user){
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(USER_KEY,userJson).apply();
    }
    public void saveUserWithKey(Context context, User user,String key){
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(key,userJson).apply();
    }

    public User getUser(){
        String userJson = sharedPreferences.getString(USER_KEY,null);
        if (userJson!=null){
            return gson.fromJson(userJson, User.class);
        }else {
            return null;
        }
    }

    public User getUser(String key){
        String userJson = sharedPreferences.getString(key,null);
        if (userJson!=null){
            return gson.fromJson(userJson, User.class);
        }else {
            return null;
        }
    }
    public User getUser(Context context,String pre, String key){
        sharedPreferences = context.getSharedPreferences(pre,Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(key,null);
        if (userJson!=null){
            return gson.fromJson(userJson, User.class);
        }else {
            return null;
        }
    }
    public User getUser(String pre,String key){
        sharedPreferences = context.getSharedPreferences(pre,Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(key,null);
        if (userJson!=null){
            return gson.fromJson(userJson, User.class);
        }else {
            return null;
        }
    }
    public void saveUser(Context context, User user, String prefName){
        sharedPreferences = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(USER_KEY,userJson).apply();
    }

    public void saveUserWithPreKey(Context context, User user, String prefName, String key){
        sharedPreferences = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(key,userJson).apply();
    }

    public void getOut(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        logout(context);
    }


    public void getOut(Context context,String prefe){
        sharedPreferences = context.getSharedPreferences(prefe, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        logout(context);
    }

    public void out(){
        editor.clear();
        editor.apply();
        logout();
    }
    public void getOut(){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        logout();
    }

    public void getOut(String pf){
        sharedPreferences = context.getSharedPreferences(pf, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        logout();
    }

    public void logout(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN,false);
        Intent intent;
        if (isLoggedIn){
            intent = new Intent(context.getApplicationContext(),MainActivity.class);
        }else {
            intent = new Intent(context.getApplicationContext(), LoginPage.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void logout(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN,false);
        Intent intent;
        if (isLoggedIn){
            intent = new Intent(context.getApplicationContext(), UserPanel.class);
        }else {
            intent = new Intent(context.getApplicationContext(), LoginPage.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void setPrefName(String p){
        this.PrefName = p;
    }

    public int checkLoggedIn(Context context){
       int n=0;
        SharedPreferences sharedPreferences = context.getSharedPreferences("eduBoxLogin", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);
        if (isLoggedIn){
            Intent intent = new Intent(context.getApplicationContext(),MainActivity.class);
            context.startActivity(intent);
            n=1;
        }
        return n;
    }

    public void setLoggedIn(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED_IN,isLoggedIn);
        editor.apply();
    }
    public void setInstaller(boolean isInstalled){
        sharedPreferences = context.getSharedPreferences(INSTALLER_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(INSTALLER_ID,isInstalled);
        editor.apply();
    }

    public void setLoggedUser(String bb){
        editor.putString(USER_ID,bb);
        editor.apply();
    }
    public void setLoggedUser(String id,String phone){
        editor.putString(USER_ID,id);
        editor.putString(USER_PHONE,phone);
        editor.apply();
    }
    public void setLoggedUser(String id,String phone,String email){
        editor.putString(USER_ID,id);
        editor.putString(USER_PHONE,phone);
        editor.putString(USER_EMAIL,email);
        editor.apply();
    }
    public void setLoggedUser(String id,String phone,String email,String key){
        editor.putString(USER_ID,id);
        editor.putString(USER_PHONE,phone);
        editor.putString(USER_EMAIL,email);
        editor.putString(USER_KEY,key);
        editor.apply();
    }
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN,false);
    }

    public boolean isInstalled(){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(INSTALLER_NAME,Context.MODE_PRIVATE);
        return sharedPreferences1.getBoolean(INSTALLER_ID,false);
    }


    public static void setUserData(Context context,String pref, String key, String value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString(key,value);
        editor1.apply();
    }
    public static void setUserData(Context context,String pref, String key, float value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putFloat(key,value);
        editor1.apply();
    }
    public static void setUserData(Context context,String pref, String key, int value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putInt(key,value);
        editor1.apply();
    }
    public static void setUserData(Context context,String pref, String key, long value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putLong(key,value);
        editor1.apply();
    }
    public static void setUserData(Context context,String pref, String key, boolean value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putBoolean(key,value);
        editor1.apply();
    }
    public static void setUserData(Context context,String pref, String key, Set<String> set){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putStringSet(key,set);
        editor1.apply();
    }

    public  void setUserData(String pref, String key, String value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString(key,value);
        editor1.apply();
    }
    public  void setUserData(String pref, String key, float value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putFloat(key,value);
        editor1.apply();
    }
    public  void setUserData(String pref, String key, int value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putInt(key,value);
        editor1.apply();
    }
    public  void setUserData(String pref, String key, long value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putLong(key,value);
        editor1.apply();
    }
    public  void setUserData(String pref, String key, boolean value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putBoolean(key,value);
        editor1.apply();
    }
    public  void setUserData(String pref, String key, Set<String> set){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putStringSet(key,set);
        editor1.apply();
    }

    public  void setUserData(String key, String value){
        editor.putString(key,value);
        editor.apply();
    }
    public  void setUserData(String key, float value){
        editor.putFloat(key,value);
        editor.apply();
    }
    public  void setUserData(String key, int value){
        editor.putInt(key,value);
        editor.apply();
    }
    public  void setUserData(String key, long value){
        editor.putLong(key,value);
        editor.apply();
    }
    public  void setUserData(String key, boolean value){
        editor.putBoolean(key,value);
        editor.apply();
    }
    public  void setUserData(String key, Set<String> set){
        editor.putStringSet(key,set);
        editor.apply();
    }


    public static String getStringPref(Context context,String pref,String key,String default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getString(key,default_value);
    }
    public static int getIntPref(Context context,String pref,String key,int default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getInt(key,default_value);
    }
    public static long getLongPref(Context context,String pref,String key,long default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getLong(key,default_value);
    }
    public static boolean getBooleanPref(Context context,String pref,String key,boolean default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getBoolean(key,default_value);
    }
    public static float getFloatPref(Context context,String pref,String key,float default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getFloat(key,default_value);
    }
    public static Set<String> getStringSetPref(Context context,String pref,String key,Set<String> default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getStringSet(key,default_value);
    }

    public String getStringPref(String pref,String key,String default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getString(key,default_value);
    }
    public int getIntPref(String pref,String key,int default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getInt(key,default_value);
    }
    public long getLongPref(String pref,String key,long default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getLong(key,default_value);
    }
    public boolean getBooleanPref(String pref,String key,boolean default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getBoolean(key,default_value);
    }
    public float getFloatPref(String pref,String key,float default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getFloat(key,default_value);
    }
    public Set<String> getStringSetPref(String pref,String key,Set<String> default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sharedPreferences1.getStringSet(key,default_value);
    }

    public String getStringPref(String key,String default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getString(key,default_value);
    }
    public int getIntPref(String key,int default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getInt(key,default_value);
    }
    public long getLongPref(String key,long default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getLong(key,default_value);
    }
    public boolean getBooleanPref(String key,boolean default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getBoolean(key,default_value);
    }
    public float getFloatPref(String key,float default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getFloat(key,default_value);
    }
    public Set<String> getStringSetPref(String key,Set<String> default_value){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getStringSet(key,default_value);
    }

    public String getStringPreference(String key){
        return sharedPreferences.getString(key,default_valueS);
    }
    public String getStringPref(String key){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getString(key,default_valueS);
    }
    public int getIntPref(String key){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getInt(key,default_valueI);
    }
    public long getLongPref(String key){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getLong(key,default_valueL);
    }
    public boolean getBooleanPref(String key){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getBoolean(key,default_valueB);
    }
    public float getFloatPref(String key){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getFloat(key,default_valueF);
    }
    public Set<String> getStringSetPref(String key){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getStringSet(key,default_valueSet);
    }

    public static void clearUserData(Context context){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.clear();
        editor1.apply();
    }
    public void clearUserData(){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.clear();
        editor1.apply();
    }
    public void clearUserData(Context context, String prefNamee){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(prefNamee,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.clear();
        editor1.apply();
    }
    public void clearUserData(String prefNamee){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(prefNamee,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.clear();
        editor1.apply();
    }
    public static Map<String, ?> getAllData(Context context){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences1.getAll();
    }
    public Map<String, ?> getAllData(){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        return sharedPreferences1.getAll();
    }
    public Map<String, ?> getAllData(String prefNamee){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(prefNamee,Context.MODE_PRIVATE);
        return sharedPreferences1.getAll();
    }
    public static SharedPreferences getSharedPref(Context context,String pref){
        return context.getSharedPreferences(pref,Context.MODE_PRIVATE);
    }
    public SharedPreferences getSharedPref(String pref){
        return context.getSharedPreferences(pref,Context.MODE_PRIVATE);
    }
    public SharedPreferences getSharedPref(){
        return context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
    }





    public void saveSchool(School school){
        String userJson = gson.toJson(school);
        sharedPreferences.edit().putString(SCHOOL_KEY,userJson).apply();
    }
    public void saveSchool(School school, String key){
        String userJson = gson.toJson(school);
        sharedPreferences.edit().putString(key,userJson).apply();
    }
    public void saveSchool(School school, String pr, String key){
        sharedPreferences = context.getSharedPreferences(pr,Context.MODE_PRIVATE);
        String userJson = gson.toJson(school);
        sharedPreferences.edit().putString(key,userJson).apply();
    }
    public void saveSchool(Context context, School school){
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        String userJson = gson.toJson(school);
        sharedPreferences.edit().putString(SCHOOL_KEY,userJson).apply();
    }
    public void saveSchoolWithKey(Context context, School school,String key){
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        String userJson = gson.toJson(school);
        sharedPreferences.edit().putString(key,userJson).apply();
    }

    public School getSchool(){
        String userJson = sharedPreferences.getString(SCHOOL_KEY,null);
        if (userJson!=null){
            return gson.fromJson(userJson, School.class);
        }else {
            return null;
        }
    }

    public School getSchool(String key){
        String userJson = sharedPreferences.getString(key,null);
        if (userJson!=null){
            return gson.fromJson(userJson, School.class);
        }else {
            return null;
        }
    }
    public School getSchool(Context context,String pre, String key){
        sharedPreferences = context.getSharedPreferences(pre,Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(key,null);
        if (userJson!=null){
            return gson.fromJson(userJson, School.class);
        }else {
            return null;
        }
    }
    public School getSchool(String pre,String key){
        sharedPreferences = context.getSharedPreferences(pre,Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(key,null);
        if (userJson!=null){
            return gson.fromJson(userJson, School.class);
        }else {
            return null;
        }
    }
    public void saveSchool(Context context, User user, String prefName){
        sharedPreferences = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(SCHOOL_KEY,userJson).apply();
    }

    public void saveSchoolWithPreKey(Context context, User user, String prefName, String key){
        sharedPreferences = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(key,userJson).apply();
    }




}
