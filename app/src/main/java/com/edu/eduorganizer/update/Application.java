package com.edu.eduorganizer.update;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;


import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Application extends BaseMenu {

    private static final String JSON_URL = "https://eduboxf.com/app/list_files.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find the TableLayout
        TableLayout fileTable = findViewById(R.id.fileTable);

        // Fetch and parse JSON data in a background task
        new FetchJsonDataTask(fileTable).execute(JSON_URL);
    }

    private class FetchJsonDataTask extends AsyncTask<String, Void, String> {
        private final TableLayout fileTable;

        public FetchJsonDataTask(TableLayout fileTable) {
            this.fileTable = fileTable;
        }

        @Override
        protected String doInBackground(String... urls) {
            if (urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        reader.close();
                        return stringBuilder.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            if (jsonData != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String fileName = jsonArray.getString(i);
                        addFileRow(fileName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getMimeType(String fileName) {
            String mimeType;

            // Get the file extension from the file name
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileName);

            // Check if the file extension is not empty
            if (fileExtension != null && !fileExtension.isEmpty()) {
                // Get the MIME type based on the file extension
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            } else {
                // If the file extension is not recognized, use a generic MIME type
                mimeType = "application/octet-stream";
            }

            return mimeType;
        }


        private void addFileRow(String fileName) {
            TableRow row = new TableRow(Application.this);

            // Create a TextView for the file name
            TextView fileNameTextView = new TextView(Application.this);
            fileNameTextView.setText(fileName);
            fileNameTextView.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f));

            File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
            Log.d("eee",""+apkFile);


            // Create a download button
            Button downloadButton = new Button(Application.this);

            if (apkFile.exists()) {
                downloadButton.setText("Downloaded");
            }else {
                downloadButton.setText("Download");
            }
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle download button click for this file
                    Toast.makeText(Application.this, "Downloading " + fileName, Toast.LENGTH_SHORT).show();

                    // Add your download logic here using DownloadManager
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse("https://eduboxf.com/app/files/" + fileName);

                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setTitle(fileName); // Set the title for the notification (optional)
                    request.setDescription("Downloading " + fileName); // Set the description (optional)
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                    downloadManager.enqueue(request);
                }
            });


            // Create an install button
            Button installButton = new Button(Application.this);

            if (apkFile.exists()) {
                installButton.setText("Open");
            }else {
                installButton.setText("EDU");
            }
            installButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);

                    String filePath = apkFile+"";

                    if (apkFile.exists()) {
                        Uri fileUri = FileProvider.getUriForFile(Application.this, getApplicationContext().getPackageName() + ".provider", apkFile);

                        // Create an intent to view the file
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(fileUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            // Start the intent to open the file
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {

                            Intent intentt = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intentt.addCategory(Intent.CATEGORY_OPENABLE);
                            intentt.setType("*/*"); // This specifies the type of files to display (all file types)

                            try {
                                // Start the intent to open the file manager
                                startActivity(intentt);
                            } catch (ActivityNotFoundException ee) {
                                // Handle the case where no file manager app is available
                                Toast.makeText(getApplicationContext(), "No file manager app found.", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getApplicationContext(), "No app can open this file type.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the case where the APK file does not exist
                        Toast.makeText(getApplicationContext(), "APK file not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            });



            // Add the TextView, download button, and install button to the TableRow
            row.addView(fileNameTextView);
            row.addView(downloadButton);
            row.addView(installButton);

            // Add the TableRow to the TableLayout
            fileTable.addView(row);
        }
    }
}
