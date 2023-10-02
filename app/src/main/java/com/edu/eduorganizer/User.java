package com.edu.eduorganizer;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String sId=null;
    private String stdId;
    private String stdName=null;

    private String email=null;
    private String phone=null;
    private String pass;
    private String address=null;

    private int status= 1;

    private int u_type=1;
    private String major=null;
    private String userId=null;
    private String picture=null;
    private byte[] photo;
    private byte[] fingerData;
    private String sync_key=null;
    private int sync_status= 0;
    private String uniqueId;
    private String currSessId;
    private String designation;


    public void setUniqueId(String uniqueId){
        this.uniqueId=uniqueId;
    }public String getUniqueId(){
        return uniqueId;
    }

    public void setCurrSessId(String maxSec){
        this.currSessId = maxSec;
    }public String getCurrSessId(){
        return currSessId;
    }

    public void setDesignation(String maxSec){
        this.designation = maxSec;
    }public String getDesignation(){
        return designation;
    }


    public User(){

    }

    public int getId() {
        return id;
    }
    public int getSync_status() {
        return sync_status;
    }
    public int getU_type() {
        return u_type;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setSync_status(int id) {
        this.sync_status = id;
    }
    public void setU_type(int id) {
        this.u_type = id;
    }

    public String getSId() {
        return sId;
    }
    public String getSync_key() {
        return sync_key;
    }

    public void setSId(String sId) {
        this.sId = sId;
    }
    public void setSync_key(String sId) {
        this.sync_key = sId;
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String stdName) {
        this.email = stdName;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getFingerData() {
        return fingerData;
    }

    public void setFingerData(byte[] fingerData) {
        this.fingerData = fingerData;
    }
}