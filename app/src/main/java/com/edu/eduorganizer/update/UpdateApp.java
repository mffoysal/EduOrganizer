package com.edu.eduorganizer.update;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class UpdateApp {

    private static final String APK_URL = "https://eduboxf.com/app/files/com.edu.eduorganizer.apk";
    private long currentTimeMillis = System.currentTimeMillis();;
    Date currDate = new Date(currentTimeMillis);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    String currentDate = sdf.format(currDate);
    public static void checkAndInstallUpdate(Context context) {
        new CheckAPKExistenceTask(context).execute(APK_URL);
    }

    private static class CheckAPKExistenceTask extends AsyncTask<String, Void, Boolean> {
        private Context context;
        private long currentTimeMillis = System.currentTimeMillis();;
        Date currDate = new Date(currentTimeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String currentDate = sdf.format(currDate);

        public CheckAPKExistenceTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if (urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("HEAD");
                    int responseCode = httpURLConnection.getResponseCode();
                    return responseCode == HttpURLConnection.HTTP_OK;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean exists) {
            if (exists) {
                // The APK file exists, proceed with downloading and installing it
                new DownloadAPKTask(context).execute(APK_URL);
            } else {
                // The APK file does not exist
                Toast.makeText(context, "No update available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class DownloadAPKTask extends AsyncTask<String, Void, File> {
        private Context context;
        private long currentTimeMillis = System.currentTimeMillis();;
        Date currDate = new Date(currentTimeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String currentDate = sdf.format(currDate);

        public DownloadAPKTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... urls) {
            if (urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.connect();

                    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File apkFile = new File(directory, "edubox"+currentDate+".apk");

                    if (apkFile.exists()) {
                        apkFile.delete(); // Delete the existing file if it exists
                    }

                    FileOutputStream outputStream = new FileOutputStream(apkFile);
                    InputStream inputStream = connection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                    outputStream.close();
                    inputStream.close();

                    return apkFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(File apkFile) {
            if (apkFile != null) {
                // Install the APK file
                Uri apkUri = Uri.fromFile(apkFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/com.edu.eduorganizer");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                // Delete the downloaded APK file after successful installation
                if (apkFile.exists()) {
//                    apkFile.delete();
                }
            } else {
                // Download failed
                Toast.makeText(context, "Failed to download the update", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
