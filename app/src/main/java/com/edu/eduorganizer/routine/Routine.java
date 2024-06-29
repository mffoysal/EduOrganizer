package com.edu.eduorganizer.routine;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "routine")
public class Routine implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String sId, uId, stdId, temp_name, temp_code, temp_details, temp_num, uniqueId, sync_key;
    private int sync_status,aStatus;
    private String key;
    private String tId;

    public int getaStatus() {
        return aStatus;
    }

    public void setaStatus(int aStatus) {
        this.aStatus = aStatus;
    }

    public String getTemp_details() {
        return temp_details;
    }

    public void setTemp_details(String temp_details) {
        this.temp_details = temp_details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSId() {
        return sId;
    }

    public void setSId(String sId) {
        this.sId = sId;
    }


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public String getTemp_name() {
        return temp_name;
    }

    public void setTemp_name(String temp_name) {
        this.temp_name = temp_name;
    }

    public String getTemp_code() {
        return temp_code;
    }

    public void setTemp_code(String temp_code) {
        this.temp_code = temp_code;
    }

    public String getTemp_num() {
        return temp_num;
    }

    public void setTemp_num(String temp_num) {
        this.temp_num = temp_num;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSync_key() {
        return sync_key;
    }

    public void setSync_key(String sync_key) {
        this.sync_key = sync_key;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getT_id() {
        return tId;
    }

    public String getTid() {
        return tId;
    }

    public void setTid(String tId) {
        this.tId = tId;
    }

    public void setT_id(String tId) {
        this.tId = tId;
    }

    public Routine() {
    }

    public String toString(){
        return this.temp_name;
    }

}
