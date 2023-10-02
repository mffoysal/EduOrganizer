package com.edu.eduorganizer.school;

import java.io.Serializable;

public class School implements Serializable {
	
	private String sId, sName, sPhone, sEmail,sPass, sAdrs, sEiin, sFundsBal;
	private String sAYear, sVerification, currSessId;
	private String sItp1, sItp2, sItEmail, sWeb, sFundsBank, sFundsAN, sLogo;
	private int id, sActivate, sEmpl, sCourse, sClass, sSec, sUser, sTeacher, sStudent;

	private String sync_key;
	private int sync_status= 0;
	private String key;

	public int getSync_status() {
		return sync_status;
	}
	public void setSync_status(int id) {
		this.sync_status = id;
	}
	public String getSync_key() {
		return sync_key;
	}
	public void setSync_key(String sId) {
		this.sync_key = sId;
	}
	private String uniqueId;
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

	public School(){

	}
	
	public void setsId(String sId) {
		this.sId = sId;
	}public void setsLogo(String sLogo) {
		this.sLogo = sLogo;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}public void setsPhone(String sPhone) {
		this.sPhone = sPhone;
	}public void setsEmail(String sEmail) {
		this.sEmail = sEmail;
	}public void setsPass(String sPass) {
		this.sPass = sPass;
	}public void setsAdrs(String sAdrs) {
		this.sAdrs = sAdrs;
	}public void setsEiin(String sEiin) {
		this.sEiin = sEiin;
	}public void setsFundsBal(String sFundsBal) {
		this.sFundsBal = sFundsBal;
	}public void setsTeacher(int sTeacher) {
		this.sTeacher = sTeacher;
	}public void setId(int sTeacher) {
		this.id = sTeacher;
	}public void setsStudent(int sStudent) {
		this.sStudent = sStudent;
	}public void setsUser(int sUser) {
		this.sUser = sUser;
	}public void setsEmpl(int sEmpl) {
		this.sEmpl = sEmpl;
	}public void setsActivate(int sActivate) {
		this.sActivate = sActivate;
	}public void setsCourse(int sCourse) {
		this.sCourse = sCourse;
	}public void setsClass(int sClass) {
		this.sClass = sClass;
	}public void setsSec(int sSec) {
		this.sSec = sSec;
	}public void setsAYear(String sAYear) {
		this.sAYear = sAYear;
	}public void setsItp1(String sItp1) {
		this.sItp1 = sItp1;
	}public void setsItp2(String sItp2) {
		this.sItp2 = sItp2;
	}public void setsItEmail(String sItEmail) {
		this.sItEmail = sItEmail;
	}public void setsWeb(String sWeb) {
		this.sWeb = sWeb;
	}public void setsFundsBank(String sFundsBank) {
		this.sFundsBank = sFundsBank;
	}public void setsFundsAN(String sFundsAN) {
		this.sFundsAN = sFundsAN;
	}public void setsVerification(String sVerification) {
		this.sVerification = sVerification;
	}
	
	
	
	public String getsVerification() {
		return sVerification;
	}public String getsLogo() {
		return sLogo;
	}
	public String getsId() {
		return sId;
	}
	public int getId() {
		return id;
	}
	public String getsName() {
		return sName;
	}public String getsPhone() {
		return sPhone;
	}public String getsEmail() {
		return sEmail;
	}public String getsPass() {
		return sPass;
	}public String getsAdrs() {
		return sAdrs;
	}public String getsEiin() {
		return sEiin;
	}public String getsFundsBal() {
		return sFundsBal;
	}public int getsTeacher() {
		return sTeacher;
	}public int getsStudent() {
		return sStudent;
	}public int getsUser() {
		return sUser;
	}public int getsEmpl() {
		return sEmpl;
	}public int getsActivate() {
		return sActivate;
	}public int getsCourse() {
		return sCourse;
	}public int getsClass() {
		return sClass;
	}public int getsSec() {
		return sSec;
	}public String getsAYear() {
		return sAYear;
	}public String getsItp1() {
		return sItp1;
	}public String getsItp2() {
		return sItp2;
	}public String getsItEmail() {
		return sItEmail;
	}public String getsWeb() {
		return sWeb;
	}public String getsFundsBank() {
		return sFundsBank;
	}public String getsFundsAN() {
		return sFundsAN;
	}
	
	
	
	public static void main(String args[]) {
		
	}

	public void setKey(String key) {
		this.key = key;
	}
	public String toString(){
		return sName;
	}
}
