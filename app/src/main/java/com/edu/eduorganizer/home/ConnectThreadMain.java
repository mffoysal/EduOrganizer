package com.edu.eduorganizer.home;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

public class ConnectThreadMain extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter bluetoothAdapter;
    private final UUID uuid = UUID.fromString("0000110A-0000-1000-8000-00805F9B34FB"); // Standard SerialPortService ID


    public ConnectThreadMain(BluetoothDevice device, BluetoothAdapter adapter) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        bluetoothAdapter = adapter;

        try {
            tmp = mmDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmSocket = tmp;
    }

    public void run() {
        bluetoothAdapter.cancelDiscovery();

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
