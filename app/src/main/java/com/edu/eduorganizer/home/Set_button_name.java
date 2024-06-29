package com.edu.eduorganizer.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.edu.eduorganizer.R;


public class Set_button_name extends Activity {
    private View.OnClickListener bottonCancelListener = new View.OnClickListener() {
        public void onClick(View v) {
            Set_button_name.this.finish();
        }
    };
    private View.OnClickListener bottonRestartListener = new View.OnClickListener() {
        public void onClick(View v) {
            Set_button_name.this.finish();
            System.exit(0);
            Set_button_name.this.startActivity(new Intent(Set_button_name.this, BluetoothControl.class));
        }
    };
    private View.OnClickListener bottonSaveListener = new View.OnClickListener() {
        public void onClick(View v) {
            Set_button_name.this.deviceName1_on = Set_button_name.this.etDevice1_ON.getText().toString();
            Set_button_name.this.deviceName2_on = Set_button_name.this.etDevice2_ON.getText().toString();
            Set_button_name.this.deviceName3_on = Set_button_name.this.etDevice3_ON.getText().toString();
            Set_button_name.this.deviceName4_on = Set_button_name.this.etDevice4_ON.getText().toString();
            Set_button_name.this.deviceName5_on = Set_button_name.this.etDevice5_ON.getText().toString();
            Set_button_name.this.deviceName6_on = Set_button_name.this.etDevice6_ON.getText().toString();
            Set_button_name.this.deviceName7_on = Set_button_name.this.etDevice7_ON.getText().toString();
            Set_button_name.this.deviceName8_on = Set_button_name.this.etDevice8_ON.getText().toString();
            if (Set_button_name.this.deviceName1_on == null || Set_button_name.this.deviceName1_on.equals("")) {
                Set_button_name.this.deviceName1_on = Set_button_name.this.getResources().getString(R.string.device1_on);
            }
            if (Set_button_name.this.deviceName2_on == null || Set_button_name.this.deviceName2_on.equals("")) {
                Set_button_name.this.deviceName2_on = Set_button_name.this.getResources().getString(R.string.device2_on);
            }
            if (Set_button_name.this.deviceName3_on == null || Set_button_name.this.deviceName3_on.equals("")) {
                Set_button_name.this.deviceName3_on = Set_button_name.this.getResources().getString(R.string.device3_on);
            }
            if (Set_button_name.this.deviceName4_on == null || Set_button_name.this.deviceName4_on.equals("")) {
                Set_button_name.this.deviceName4_on = Set_button_name.this.getResources().getString(R.string.device4_on);
            }
            if (Set_button_name.this.deviceName5_on == null || Set_button_name.this.deviceName5_on.equals("")) {
                Set_button_name.this.deviceName5_on = Set_button_name.this.getResources().getString(R.string.device5_on);
            }
            if (Set_button_name.this.deviceName6_on == null || Set_button_name.this.deviceName6_on.equals("")) {
                Set_button_name.this.deviceName6_on = Set_button_name.this.getResources().getString(R.string.device6_on);
            }
            if (Set_button_name.this.deviceName7_on == null || Set_button_name.this.deviceName7_on.equals("")) {
                Set_button_name.this.deviceName7_on = Set_button_name.this.getResources().getString(R.string.device7_on);
            }
            if (Set_button_name.this.deviceName8_on == null || Set_button_name.this.deviceName8_on.equals("")) {
                Set_button_name.this.deviceName8_on = Set_button_name.this.getResources().getString(R.string.device8_on);
            }
            Set_button_name.this.editor = Set_button_name.this.myprefs.edit();
            Set_button_name.this.editor.putString("device_name1_on", Set_button_name.this.deviceName1_on);
            Set_button_name.this.editor.putString("device_name2_on", Set_button_name.this.deviceName2_on);
            Set_button_name.this.editor.putString("device_name3_on", Set_button_name.this.deviceName3_on);
            Set_button_name.this.editor.putString("device_name4_on", Set_button_name.this.deviceName4_on);
            Set_button_name.this.editor.putString("device_name5_on", Set_button_name.this.deviceName5_on);
            Set_button_name.this.editor.putString("device_name6_on", Set_button_name.this.deviceName6_on);
            Set_button_name.this.editor.putString("device_name7_on", Set_button_name.this.deviceName7_on);
            Set_button_name.this.editor.putString("device_name8_on", Set_button_name.this.deviceName8_on);
            Set_button_name.this.editor.commit();
            Toast.makeText(Set_button_name.this, R.string.save_device_name, Toast.LENGTH_LONG).show();
        }
    };
    Button btnCancel;
    Button btnRestart;
    Button btnSave;
    String deviceName1_off;
    String deviceName1_on;
    String deviceName2_off;
    String deviceName2_on;
    String deviceName3_off;
    String deviceName3_on;
    String deviceName4_off;
    String deviceName4_on;
    String deviceName5_off;
    String deviceName5_on;
    String deviceName6_off;
    String deviceName6_on;
    String deviceName7_off;
    String deviceName7_on;
    String deviceName8_off;
    String deviceName8_on;
    SharedPreferences.Editor editor;
    EditText etDevice1_OFF;
    EditText etDevice1_ON;
    EditText etDevice2_OFF;
    EditText etDevice2_ON;
    EditText etDevice3_OFF;
    EditText etDevice3_ON;
    EditText etDevice4_OFF;
    EditText etDevice4_ON;
    EditText etDevice5_OFF;
    EditText etDevice5_ON;
    EditText etDevice6_OFF;
    EditText etDevice6_ON;
    EditText etDevice7_OFF;
    EditText etDevice7_ON;
    EditText etDevice8_OFF;
    EditText etDevice8_ON;
    SharedPreferences myprefs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_button_name);
        this.btnRestart = (Button) findViewById(R.id.buttonRestart);
        this.btnRestart.setOnClickListener(this.bottonRestartListener);
        this.btnSave = (Button) findViewById(R.id.buttonSave);
        this.btnSave.setOnClickListener(this.bottonSaveListener);
        this.btnCancel = (Button) findViewById(R.id.buttonCancel);
        this.btnCancel.setOnClickListener(this.bottonCancelListener);
        this.etDevice1_ON = (EditText) findViewById(R.id.editTextDevice1_on);
        this.etDevice2_ON = (EditText) findViewById(R.id.EditTextDevice2_on);
        this.etDevice3_ON = (EditText) findViewById(R.id.EditTextDevice3_on);
        this.etDevice4_ON = (EditText) findViewById(R.id.EditTextDevice4_on);
        this.etDevice5_ON = (EditText) findViewById(R.id.EditTextDevice5_on);
        this.etDevice6_ON = (EditText) findViewById(R.id.EditTextDevice6_on);
        this.etDevice7_ON = (EditText) findViewById(R.id.EditTextDevice7_on);
        this.etDevice8_ON = (EditText) findViewById(R.id.EditTextDevice8_on);
        this.myprefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.deviceName1_on = this.myprefs.getString("device_name1_on", (String) null);
        this.deviceName2_on = this.myprefs.getString("device_name2_on", (String) null);
        this.deviceName3_on = this.myprefs.getString("device_name3_on", (String) null);
        this.deviceName4_on = this.myprefs.getString("device_name4_on", (String) null);
        this.deviceName5_on = this.myprefs.getString("device_name5_on", (String) null);
        this.deviceName6_on = this.myprefs.getString("device_name6_on", (String) null);
        this.deviceName7_on = this.myprefs.getString("device_name7_on", (String) null);
        this.deviceName8_on = this.myprefs.getString("device_name8_on", (String) null);
        String myString = getResources().getString(R.string.set_device_name);
        if (this.deviceName1_on == null || this.deviceName1_on.equals("")) {
            this.etDevice1_ON.setText(myString);
        } else {
            this.etDevice1_ON.setText(this.deviceName1_on);
        }
        if (this.deviceName2_on != null && !this.deviceName2_on.equals("")) {
            this.etDevice2_ON.setText(this.deviceName2_on);
        }
        if (this.deviceName3_on != null && !this.deviceName3_on.equals("")) {
            this.etDevice3_ON.setText(this.deviceName3_on);
        }
        if (this.deviceName4_on != null && !this.deviceName4_on.equals("")) {
            this.etDevice4_ON.setText(this.deviceName4_on);
        }
        if (this.deviceName5_on != null && !this.deviceName5_on.equals("")) {
            this.etDevice5_ON.setText(this.deviceName5_on);
        }
        if (this.deviceName6_on != null && !this.deviceName6_on.equals("")) {
            this.etDevice6_ON.setText(this.deviceName6_on);
        }
        if (this.deviceName7_on != null && !this.deviceName7_on.equals("")) {
            this.etDevice7_ON.setText(this.deviceName7_on);
        }
        if (this.deviceName8_on != null && !this.deviceName8_on.equals("")) {
            this.etDevice8_ON.setText(this.deviceName8_on);
        }
    }

    public void onPause() {
        super.onPause();
        finish();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
