package com.edu.eduorganizer.unique;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Format the time as needed (e.g., "HH:mm" for 24-hour format)
        String time = String.format("%02d:%02d", hour, minute);

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
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

}
