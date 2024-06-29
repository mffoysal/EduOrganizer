package com.edu.eduorganizer.unique;

import android.graphics.Color;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class Unique {


    public String userId() {
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString().replaceAll("-", "");
        Random dice = new Random();
        int num = dice.nextInt(10);
        SecureRandom sec = new SecureRandom();
        int nu = sec.nextInt(9);
        Timestamp uidTime = new Timestamp(System.currentTimeMillis());
        String id = uidTime.toString().replaceAll("[^\\d]", "");
        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.now();
        }
        int year = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            year = date.getYear();
        }
        int m = (int) (Math.random() * year) + 1;
        String mm = String.valueOf(m);
        SimpleDateFormat da = new SimpleDateFormat("yyyyMMddHHmmss");
        String uId = da.format(new Timestamp(System.currentTimeMillis()));
        return s + mm + id + uId;
    }

    public String uId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

    public String generateUniqueID() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault());
        String dateTime = dateFormat.format(new Date());

        String hexCode = generateHexCode(); // Generate a hexadecimal code

        return dateTime + "_" + hexCode;
    }

    public String getCurrentTime() {
        LocalDateTime currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
        }
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm");
        }

        String time = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            time = currentTime.format(formatter);
        }

        return time;
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int amPm = calendar.get(Calendar.AM_PM);

        String amPmString = (amPm == Calendar.AM) ? "AM" : "PM";

        String time = String.format("%02d:%02d %s", hour, minute, amPmString);

        return time;
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0, so add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Format the date as needed
        String date = String.format("%04d-%02d-%02d", year, month, day);

        return date;
    }

    public String getCurrentDate() {
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        String date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = currentDate.format(formatter);
        }

        return date;
    }

    public String generateHexCode() {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits();
        String hexCode = Long.toHexString(mostSignificantBits);

        return hexCode;
    }

    public String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0, so add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // Format the date and time as needed
        String dateTime = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);

        return dateTime;
    }
    public String getDateTime() {
        LocalDateTime currentDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateTime = LocalDateTime.now();
        }
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        }

        String dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = currentDateTime.format(formatter);
        }

        return dateTime;
    }

    public static int uniqueId() {
        long currentTime = System.currentTimeMillis();
        int uniqueId = (int) (currentTime & 0xffffffff);
        return uniqueId;
    }

    public  int unique_id() {
        long currentTime = System.currentTimeMillis();
        int uniqueId = (int) (currentTime & 0xffffffff);
        return uniqueId;
    }

    private String getFormattedDate(int dayOffset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, dayOffset);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    private String getFormattedDay(int dayOffset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, dayOffset);
        Date date = cal.getTime();

        // Get the three-letter day name (e.g., Mon, Tue, etc.)
//        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dayNameFormat.format(date);
    }
    private String getFormattedDay(Date date) {

        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        String dayName = dayNameFormat.format(date);

        return dayName;
    }


    public String getToday(){
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        DayOfWeek dayOfWeek = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dayOfWeek = currentDate.getDayOfWeek();
        }

        String dayName = dayOfWeek.toString();
        return dayName;
    }

    public static Date convertStringToDate(String dateString, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(dateString);
    }

    public static String getDayNameFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(date);
    }

    public static String getDayNameFromDate() {
        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(currentDate);
    }

    private int getRandomColor() {
        Random random = new Random();

        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        // Create a color from the RGB values
        return Color.rgb(red, green, blue);
    }

    public static String getDayMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = sdf.parse(date);
        SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd");
        return dayOfMonthFormat.format(parsedDate);
    }

    public static String getMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = sdf.parse(date);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        return monthFormat.format(parsedDate);
    }

    public static String getMonthN(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = sdf.parse(date);

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");

        return monthFormat.format(parsedDate);
    }

    public static String getDayOfMonth(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        Date date = sdf.parse(dateTime);
        SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd");
        return dayOfMonthFormat.format(date);
    }

    public static String getDayName(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
        Date date = sdf.parse(dateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String[] daysOfWeek = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        return daysOfWeek[dayOfWeek];
    }

    public static String getMonthName(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
        Date date = sdf.parse(dateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);

        String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month];
    }

    public static String getTimeWithAMPM(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
        Date date = sdf.parse(dateTime);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        return timeFormat.format(date);
    }

}
