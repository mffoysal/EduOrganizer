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
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.R;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.File;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AppUpdate extends BaseMenu {

    private static final String APK_DOWNLOAD_URL = "https://eduboxf.com/app/files/com.edu.eduorganizer.apk";
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private long downloadId;
    private BroadcastReceiver downloadReceiver;
    private long currentTimeMillis = System.currentTimeMillis();;
    Date currDate = new Date(currentTimeMillis);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    String currentDate = sdf.format(currDate);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = findViewById(R.id.textView);
        Button updateButton = findViewById(R.id.updateButton);
        Button updateButton2 = findViewById(R.id.updateButton2);

        findViewById(R.id.updateButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),Application.class));

            }
        });

//        findViewById(R.id.updateButton2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Call the checkAndInstallUpdate method from InAppUpdateChecker
//                UpdateChecker.checkAndInstallUpdate(AppUpdate.this);
//            }
//        });


        new CheckAPKExistence(getApplicationContext(),textView).execute(APK_DOWNLOAD_URL);

        // Register the download receiver
        registerDownloadReceiver();

        // Check if the app has storage permission
        if (hasStoragePermission()) {
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AppUpdate.this,"Update",Toast.LENGTH_SHORT).show();
                    startApkDownload();
                }
            });
            updateButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AppUpdate.this,"Update Alternate",Toast.LENGTH_SHORT).show();

                    UpdateChecker.checkAndInstallUpdate(AppUpdate.this);

                }
            });

        } else {
            // Request storage permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
    }












    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the download receiver
        unregisterDownloadReceiver();
    }

    // Check if the app has storage permission
    private boolean hasStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    // Start the APK download
    private void startApkDownload() {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File apkFile = new File(directory, "edubox.apk");

        if (apkFile.exists()) {
            apkFile.delete();
        }

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(APK_DOWNLOAD_URL));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "edubox"+currentDate+".apk");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        downloadId = downloadManager.enqueue(request);
    }

    // Register the download receiver
    private void registerDownloadReceiver() {
        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long receivedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if (receivedDownloadId == downloadId) {
                    // The download is complete
                    installApk();
                }
            }
        };

        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    // Unregister the download receiver
    private void unregisterDownloadReceiver() {
        if (downloadReceiver != null) {
            unregisterReceiver(downloadReceiver);
        }
    }

    // Install the downloaded APK
    private void installApk() {
        File apkFile = new File(Environment.getExternalStorageDirectory() + "/Download/edubox"+currentDate+".apk");
        Uri apkUri = FileProvider.getUriForFile(this, "com.edu.eduorganizer", apkFile);

        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(apkUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        startActivity(intent);

//        if (apkFile.exists()) {
////            apkFile.delete();
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Storage permission granted, enable the update button
                Button updateButton = findViewById(R.id.updateButton);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Start the APK download
                        startApkDownload();
                    }
                });
            } else {
                // Storage permission denied
                Toast.makeText(this, "Storage permission required to update the app.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private static class CheckAPKExistence extends AsyncTask<String, Void, Boolean> {
        private Context context;
        private TextView updateStatusTextView;

        public CheckAPKExistence(Context context, TextView updateStatusTextView) {
            this.context = context;
            this.updateStatusTextView = updateStatusTextView;
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

                    // Return true if the response code is HTTP_OK (APK exists)
                    return responseCode == HttpURLConnection.HTTP_OK;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Return false by default (APK doesn't exist or an error occurred)
            return false;
        }

        @Override
        protected void onPostExecute(Boolean exists) {
            if (exists) {
                // The APK file exists, proceed with downloading and installing it
                // ...

                // Set the text in the TextView to indicate that an update is available
                updateStatusTextView.setText("Update Available");
            } else {
                // The APK file does not exist

                // Set the text in the TextView to indicate that no update is available
                updateStatusTextView.setText("No Update Available");
            }
        }
    }



}
