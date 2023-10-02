package com.edu.eduorganizer.update;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class UpdateChecker {

    private static final String APK_URL = "https://eduboxf.com/app/files/com.edu.eduorganizer.apk";

    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static long downloadId;
    private static BroadcastReceiver downloadReceiver;
    private long currentTimeMillis = System.currentTimeMillis();;
    Date currDate = new Date(currentTimeMillis);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    String currentDate = sdf.format(currDate);
    public static void checkAndInstallUpdate(Context context) {
        // Check if the APK file exists
        new CheckAPKExistenceTask(context).execute(APK_URL);
    }

    public static void checkAndInstallUpdate(Context context, String APK_URL) {
        // Check if the APK file exists
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
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File apkFile = new File(directory, "edubox"+currentDate+".apk");

                if (apkFile.exists()) {
                    Toast.makeText(context, "Update delete old", Toast.LENGTH_SHORT).show();
                    apkFile.delete();
                }
                File directoryy = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File apkFilee = new File(directoryy, "edubox"+currentDate+".apk");
                if (apkFilee.exists()) {
                    Toast.makeText(context, "Update delete oldd", Toast.LENGTH_SHORT).show();
                    apkFilee.delete();
                }

                try {
                    if (apkFile.exists()) {
                        apkFile.delete();
                    }
                }catch (Exception e){
                    Toast.makeText(context, "Update error"+e, Toast.LENGTH_SHORT).show();

                }finally {
                    Toast.makeText(context, "Updated Download", Toast.LENGTH_SHORT).show();
                    startApkDownload(context);

                }


            } else {
                // The APK file does not exist
                Toast.makeText(context, "No update available", Toast.LENGTH_SHORT).show();
            }
        }

        private void startApkDownload(Context context) {
            // Check if storage permission is granted
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the permission if not granted
                ActivityCompat.requestPermissions((AppCompatActivity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                return;
            }


            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(APK_URL));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "edubox"+currentDate+".apk");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            downloadId = downloadManager.enqueue(request);

            // Register the download receiver
            registerDownloadReceiver(context);
        }

        // Register the download receiver
        private void registerDownloadReceiver(Context context) {
            downloadReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long receivedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                    if (receivedDownloadId == downloadId) {
                        // The download is complete
                        installApk(context);
                    }
                }
            };

            context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }

        // Unregister the download receiver
        private void unregisterDownloadReceiver(Context context) {
            if (downloadReceiver != null) {
                context.unregisterReceiver(downloadReceiver);
            }
        }

        // Install the downloaded APK
        private void installApk(Context context) {
            File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "edubox"+currentDate+".apk");
            Uri apkUri = FileProvider.getUriForFile(context, "com.edu.eduorganizer", apkFile);

            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);


            context.startActivity(intent);

//            if (apkFile.exists()) {
////                apkFile.delete();
//            }

        }
    }
}
