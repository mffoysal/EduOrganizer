package com.edu.eduorganizer.home;


import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class Pair_device implements Parcelable {
    public static final Parcelable.Creator<Pair_device> CREATOR = new Parcelable.Creator<Pair_device>() {
        public Pair_device createFromParcel(Parcel parcel) {
            return new Pair_device(parcel);
        }

        public Pair_device[] newArray(int i) {
            return new Pair_device[i];
        }
    };
    BluetoothDevice device;

    public int describeContents() {
        return 0;
    }

    public Pair_device() {
    }

    protected Pair_device(Parcel parcel) {
        this.device = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public void setDevice(BluetoothDevice bluetoothDevice) {
        this.device = bluetoothDevice;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.device, i);
    }
}

