package com.edu.eduorganizer.update;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.edu.eduorganizer.BaseMenu;
import com.edu.eduorganizer.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Applications extends BaseMenu {

    private static final String URL = "https://eduboxf.com/app/files/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find the TableLayout
        TableLayout fileTable = findViewById(R.id.fileTable);

        // Fetch the list of files in a background task
        new FetchFilesTask(fileTable).execute();
    }

    private class FetchFilesTask extends AsyncTask<Void, Void, Void> {
        private final TableLayout fileTable;

        public FetchFilesTask(TableLayout fileTable) {
            this.fileTable = fileTable;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Connect to the URL and get the HTML page
                Document doc = Jsoup.connect(URL).get();

                // Find all anchor tags (links)
                Elements links = doc.select("a[href]");

                // Loop through the links and add them to the TableLayout
                for (Element link : links) {
                    final String fileName = link.attr("href");
                    if (!fileName.equals("../")) { // Skip parent directory link
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("eee",fileName);
                                addFileRow(fileName);
                            }
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void addFileRow(String fileName) {
            TableRow row = new TableRow(Applications.this);

            // Create a TextView for the file name
            TextView fileNameTextView = new TextView(Applications.this);
            fileNameTextView.setText(fileName);
            fileNameTextView.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f));

            // Create a Button for downloading the file
            Button downloadButton = new Button(Applications.this);
            downloadButton.setText("Download");
            downloadButton.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // Add an OnClickListener to the download button (implement file download logic here)
            downloadButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    // Implement file download logic here
                }
            });

            // Add the TextView and Button to the TableRow
            row.addView(fileNameTextView);
            row.addView(downloadButton);

            // Add the TableRow to the TableLayout
            fileTable.addView(row);
        }
    }
}
