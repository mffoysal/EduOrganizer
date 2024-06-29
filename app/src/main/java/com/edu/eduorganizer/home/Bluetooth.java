package com.edu.eduorganizer.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Bluetooth extends BaseMenu {

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter<String> devicesArrayAdapter;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private OutputStream outputStream;
    private UUID uid;
    private final UUID uuid = UUID.fromString("0000111F-0000-1000-8000-00805F9B34FB"); // Standard SerialPortService ID
    private ListView listView;
    private ConnectThread connectThread;

    private TextInputEditText code;
    private String userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        uid = UUID.randomUUID();

        listView = findViewById(R.id.listView);
        devicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(devicesArrayAdapter);
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not Supported", Toast.LENGTH_SHORT).show();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            devicesArrayAdapter.clear();
            if (pairedDevices != null && pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    devicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else {
                Toast.makeText(getApplicationContext(), "No paired Bluetooth devices found", Toast.LENGTH_SHORT).show();
            }
        }else {
            pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            devicesArrayAdapter.clear();
            if (pairedDevices != null && pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    devicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else {
                Toast.makeText(getApplicationContext(), "No paired Bluetooth devices found", Toast.LENGTH_SHORT).show();
            }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deviceNameAddress = (String) parent.getItemAtPosition(position);
                String[] parts = deviceNameAddress.split("\n");
                String deviceName = parts[0];
                connectToDevice(deviceName);
            }
        });

        Button searchButton = findViewById(R.id.searchButton);
        code = findViewById(R.id.inputCode);
        userCode = code.getText().toString().trim();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBluetoothDevices();
            }
        });

        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                connectToDevice("HC-05");
//                connectToDevice("MF-FOYSAL-PC2");
                startActivity(new Intent(getApplicationContext(),BluetoothControl.class));
            }

        });

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCode = code.getText().toString().trim();
                sendData(userCode);
            }
        });

    }

    private void searchBluetoothDevices() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }
        pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        devicesArrayAdapter.clear();
        if (pairedDevices != null && pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No paired Bluetooth devices found", Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(devicesArrayAdapter);

    }

    private void connectToDevicee(String deviceName) {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported or not enabled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mmSocket != null && mmSocket.isConnected()) {
            Toast.makeText(getApplicationContext(), "Already connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
            return;
        }
        pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals(deviceName)) {
                try {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }

                    mmDevice = device;
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                    mmSocket.connect();

                    outputStream = mmSocket.getOutputStream();

                    // Connection successful
                    Toast.makeText(getApplicationContext(), "Connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Failed to connect to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    try {
                        if (mmSocket != null) {
                            mmSocket.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "Device " + deviceName + " not found in paired devices", Toast.LENGTH_SHORT).show();
    }


    private void connectToDevice(String deviceName) {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported or not enabled", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mmSocket != null && mmSocket.isConnected()) {
            Toast.makeText(getApplicationContext(), "Already connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
            return;
        }

        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals(deviceName)) {
                try {
                    mmDevice = device;
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                    mmSocket.connect();
                    Toast.makeText(getApplicationContext(), "Connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
                    outputStream = mmSocket.getOutputStream();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    connectThread = new ConnectThread(device);
                    connectThread.start();
                }
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "Device " + deviceName + " not found in paired devices", Toast.LENGTH_SHORT).show();
    }
    private void connectAgainDevice(String deviceName) {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported or not enabled", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mmSocket != null && mmSocket.isConnected()) {
            Toast.makeText(getApplicationContext(), "Already connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
            return;
        }

        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals(deviceName)) {
                try {
                    mmDevice = device;
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                    mmSocket.connect();
                    Toast.makeText(getApplicationContext(), "Connected to " + mmDevice.getName(), Toast.LENGTH_SHORT).show();
                    outputStream = mmSocket.getOutputStream();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    connectThread = new ConnectThread(device);
                    connectThread.start();
                }
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "Device " + deviceName + " not found in paired devices", Toast.LENGTH_SHORT).show();
    }

    private void sendData(String message) {
        if (outputStream == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth connection not established", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            outputStream.write(message.getBytes());
            outputStream.flush();
            Toast.makeText(getApplicationContext(), "Data sent: " + message, Toast.LENGTH_SHORT).show();
            resetOutputStream();
        } catch (IOException e) {
            resetOutputStream();
            Toast.makeText(getApplicationContext(), "Failed to send data"+e.getMessage(), Toast.LENGTH_LONG).show();
            reconnect(mmDevice.getName());
            e.printStackTrace();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mmSocket != null) {
                mmSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void reconnect(String deviceName) {
        // Close the existing socket and output stream
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (mmSocket != null) {
                mmSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream = null;
            mmSocket = null;

            connectAgainDevice(deviceName);
        }
    }
    public class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
//            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            try {
                mmSocket.connect();
                // If connection successful, you can perform further operations here
                // For example, you can start a separate thread to manage data communication
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
                return;
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cancelConnection() {
        if (connectThread != null) {
            connectThread.cancel();
        }
    }

    private void closeOutputStream() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void resetOutputStream() {
//        closeOutputStream();
        outputStream = null;
        if (mmSocket != null && mmSocket.isConnected()) {
            try {
                outputStream = mmSocket.getOutputStream();
            } catch (IOException e) {
                reconnect(mmDevice.getName());
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported or not enabled", Toast.LENGTH_SHORT).show();
            reconnect(mmDevice.getName());
        }
    }


}