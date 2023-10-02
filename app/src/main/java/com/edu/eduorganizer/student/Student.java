package com.edu.eduorganizer.student;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Student implements Serializable {

	private int id,program;
	private String studentId, uId, sId, stdId, stdName, stdPhone, stdEmail, homePhone, stdReligion, address;
	private String dob, nidBirth, country, UnionWord, fatherName, motherName, fNid, mNid, gName;
	private String gAddress, gPhone, gEmail, stdImg, major, sMajor, stdPass, gender,addDate;
	private int aStatus;
	private Image image;
	private File file;
	private byte[] imgData;
	private Bitmap profilePic;

	private String sync_key, key;
	private int sync_status= 0;
	private String uniqueId;
	private String currSessId;

	public String toString(){
		return  stdName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public int getProgram() {
		return program;
	}
	public void setProgram(int id) {
		this.program = id;
	}

	public void setUniqueId(String uniqueId){
		this.uniqueId=uniqueId;
	}public String getUniqueId(){
		return uniqueId;
	}

	public void setCurrSessId(String subId){
		this.currSessId = subId;
	}public String getCurrSessId(){
		return currSessId;
	}
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

	public Student(){

	}

	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}


	public String getKey() {
		return key;
	}
	public void setKey(String sId) {
		this.key = sId;
	}

	public void setFile(File file) {
		this.file = file;
	}
	public void setaStatus(int aStatus) {
		this.aStatus = aStatus;
	}
	public void setUId(String uId) {
		this.uId = uId;
	}public void setgender(String gender) {
		this.gender = gender;
	}
	public void setSId(String sId) {
		this.sId = sId;
	}	public void setstdName(String stdName) {
		this.stdName = stdName;
	}	public void setstdPhone(String stdPhone) {
		this.stdPhone = stdPhone;
	}	public void setstdEmail(String stdEmail) {
		this.stdEmail = stdEmail;
	}	public void sethomePhone(String homePhone) {
		this.homePhone = homePhone;
	}	public void setstdReligion(String stdReligion) {
		this.stdReligion = stdReligion;
	}	public void setAddress(String address) {
		this.address = address;
	}	public void setdob(String dob) {
		this.dob = dob;
	}	public void setnidBirth(String nidBirth) {
		this.nidBirth = nidBirth;
	}	public void setcountry(String country) {
		this.country = country;
	}	public void setUnionWord(String UnionWord) {
		this.UnionWord = UnionWord;
	}	public void setfatherName(String fatherName) {
		this.fatherName = fatherName;
	}	public void setmotherName(String motherName) {
		this.motherName = motherName;
	}	public void setfNid(String fNid) {
		this.fNid = fNid;
	}	public void setmNid(String mNid) {
		this.mNid = mNid;
	}
	public void setgName(String gName) {
		this.gName = gName;
	}	public void setgAddress(String gAddress) {
		this.gAddress = gAddress;
	}	public void setgPhone(String gPhone) {
		this.gPhone = gPhone;
	}	public void setgEmail(String gEmail) {
		this.gEmail = gEmail;
	}	public void setstdImg(String stdImg) {
		this.stdImg = stdImg;
	}	public void setsMajor(String sMajor) {
		this.sMajor = sMajor;
	}	public void setstdPass(String stdPass) {
		this.stdPass = stdPass;
	}	public void setstdId(String stdId) {
		this.stdId = stdId;
	}	public void setAdmissionDate(String addDate)
	{
		this.addDate=addDate;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public File getFile() {
		return file;
	}


	public String generateAdmissionDate() {
		Calendar calendar = Calendar.getInstance();
		Date currentTime = calendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedTime = dateFormat.format(currentTime);
		return formattedTime;
	}
	public String getAdmissionDate()
	{
		return addDate;

	}
	public String getgender() {
		return gender;
	}
	public int getaStatus() {
		return aStatus;
	}
	public String getUId() {
		return uId;
	}public String getSId() {
		return sId;
	}public String getStdId() {
		return stdId;
	}public String getStdName() {
		return stdName;
	}public String getstdPhone() {
		return stdPhone;
	}public String getstdEmail() {
		return stdEmail;
	}public String gethomePhone() {
		return homePhone;
	}public String getstdReligion() {
		return stdReligion;
	}public String getdob() {
		return dob;
	}
	public String getAddress() {
		return address;
	}public String getcountry() {
		return country;
	}public String getUnionWord() {
		return UnionWord;
	}public String getfatherName() {
		return fatherName;
	}public String getmotherName() {
		return motherName;
	}public String getfNid() {
		return fNid;
	}public String getmNid() {
		return mNid;
	}public String getgName() {
		return gName;
	}public String getgAddress() {
		return gAddress;
	}public String getgPhone() {
		return gPhone;
	}public String getgEmail() {
		return gEmail;
	}public String getstdImg() {
		return stdImg;
	}public String getsMajor() {
		return sMajor;
	}public String getstdPass() {
		return stdPass;
	}public String getnidBirth() {
		return nidBirth;
	}



	public void setProfilePic(byte[] imageData) {
		this.profilePic = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
	}

	public Bitmap getProfilePic() {
		return profilePic;
	}



	public Date getBirthDateInDateFormat()
	{
		Date date=null;
		try {
			date=new SimpleDateFormat("dd-MM-yyyy").parse(this.dob);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}



	public static void main(String[] args) {


	}

}
