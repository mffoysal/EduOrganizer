package com.edu.eduorganizer.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.eduorganizer.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    private RecyclerView recyclerView;
    private int REQUEST_CAMERA_PERMISSION = 107;
    private int REQUEST_LOCATION_PERMISSION = 108;
    private static final int REQUEST_CALL_PERMISSION = 101;
    private static final int REQUEST_SMS_PERMISSION = 102;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 104;

    private static final int REQUEST_WIFI_PERMISSION = 103;
    private static final int REQUEST_NFC_PERMISSION = 105;
    private static final int REQUEST_HOTSPOT_PERMISSION = 106;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);

        SwitchPreference drawOverAppsPreference = findPreference("draw_over_apps");
        if (drawOverAppsPreference != null) {
            drawOverAppsPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean enabled = (boolean) newValue;
                    if (enabled) {
                        requestOverlayPermission();
                    }
                    return true; // Allow the preference to change its state
                }
            });
        }

        CheckBoxPreference cameraPermission = findPreference("permission_camera");
        CheckBoxPreference locationPermission = findPreference("permission_location");

        cameraPermission.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean enabled = (boolean) newValue;
                if (enabled) {
                    requestCameraPermission();
                }
                return true;
            }
        });

        locationPermission.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean enabled = (boolean) newValue;
                if (enabled) {
                    requestLocationPermission();
                }
                return true;
            }
        });

        SwitchPreferenceCompat callPermission = findPreference("permission_call");
        SwitchPreferenceCompat smsPermission = findPreference("permission_sms");
        SwitchPreferenceCompat bluetoothPermission = findPreference("permission_bluetooth");

        callPermission.setOnPreferenceChangeListener(createPermissionChangeListener(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION));
        smsPermission.setOnPreferenceChangeListener(createPermissionChangeListener(Manifest.permission.SEND_SMS, REQUEST_SMS_PERMISSION));
        bluetoothPermission.setOnPreferenceChangeListener(createBluetoothPermissionChangeListener());

        SwitchPreferenceCompat wifiPermission = findPreference("permission_wifi");
        SwitchPreferenceCompat nfcPermission = findPreference("permission_nfc");
        SwitchPreferenceCompat hotspotPermission = findPreference("permission_hotspot");

        wifiPermission.setOnPreferenceChangeListener(createPermissionChangeListener(
                Manifest.permission.CHANGE_WIFI_STATE, REQUEST_WIFI_PERMISSION));

        nfcPermission.setOnPreferenceChangeListener(createPermissionChangeListener(
                Manifest.permission.NFC, REQUEST_NFC_PERMISSION));

        hotspotPermission.setOnPreferenceChangeListener(createHotspotPermissionChangeListener());



    }

    private Preference.OnPreferenceChangeListener createHotspotPermissionChangeListener() {
        return new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean enabled = (boolean) newValue;
                if (enabled) {
                    requestHotspotPermission();
                }
                return true;
            }
        };
    }

    private void requestHotspotPermission() {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    private Preference.OnPreferenceChangeListener createPermissionChangeListener(final String permission, final int requestCode) {
        return new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean enabled = (boolean) newValue;
                if (enabled) {
                    requestPermission(permission, requestCode);
                }
                return true;
            }
        };
    }

    private Preference.OnPreferenceChangeListener createBluetoothPermissionChangeListener() {
        return new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean enabled = (boolean) newValue;
                if (enabled) {
                    requestBluetoothPermission();
                }
                return true;
            }
        };
    }

    private void requestPermission(String permission, int requestCode) {
        if (ActivityCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{permission},
                    requestCode);
        }  else {
            Toast.makeText(getContext(),"Permission already granted!",Toast.LENGTH_SHORT).show();

        }
    }

    private void requestBluetoothPermission() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled, prompt the user to enable it
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }
    }



    private void requestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),"Permissions are granted!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),"Permissions are not granted!",Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),"Permissions are granted!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),"Permissions are not granted!",Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),"Permissions are granted!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),"Permissions are not granted!",Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),"Permissions are granted!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),"Permissions are not granted!",Toast.LENGTH_SHORT).show();
            }
        } else         if (requestCode == REQUEST_WIFI_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),"Permissions are granted!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),"Permissions are not granted!",Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_NFC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),"Permissions are granted!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),"Permissions are not granted!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + requireContext().getPackageName()));
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the preference screen layout
//        addPreferencesFromResource(R.xml.preference);

        // Initialize RecyclerView
//        recyclerView = rootView.findViewById(R.id.recyclerview_id); // Replace with your RecyclerView's ID
//        adapter = new YourAdapter(); // Replace with your adapter
//        recyclerView.setAdapter(adapter);

        return rootView;


    }
}