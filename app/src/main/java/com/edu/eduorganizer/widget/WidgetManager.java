package com.edu.eduorganizer.widget;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.edu.eduorganizer.R;

public class WidgetManager extends AppCompatActivity {

    private static final int REQUEST_PICK_APPWIDGET = 101;
    private static final int APPWIDGET_PICK = 102;
    private static final int YOUR_REQUEST_CODE = 123;
    private static final int APPWIDGET_HOST_ID = 1;
    private AppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;
    private int mAppWidgetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_manager);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button (back button)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set a click listener on the toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the toolbar click (back button)
                onBackPressed();
            }
        });


        AppWidgetHost mAppWidgetHost = new AppWidgetHost(getApplicationContext(), APPWIDGET_HOST_ID);
        int appWidgetId = mAppWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        startActivityForResult(pickIntent, APPWIDGET_PICK);

//        AppWidgetHostView hostView = mAppWidgetHost.createView(getApplicationContext(), appWidgetId, widgetInfo);
//        hostView.setAppWidget(appWidgetId, widgetInfo);
//        hostView.updateAppWidgetOptions(options);
//        appWidgetHostViews.put(appWidgetId, hostView);
//        widgetContainer.addView(hostView);


        appWidgetId = mAppWidgetHost.allocateAppWidgetId();
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        AppWidgetHostView hostView = mAppWidgetHost.createView(getApplicationContext(), appWidgetId, appWidgetInfo);

        mAppWidgetHost.deleteAppWidgetId(appWidgetId);
        AppWidgetHostView hostView2 = mAppWidgetHost.createView(getApplicationContext(), appWidgetId, appWidgetInfo);





        mAppWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
        mAppWidgetHost.startListening();
        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetId = mAppWidgetHost.allocateAppWidgetId();





    }


    private void addWidget() {
        // Start the widget picker activity
        int appWidgetId = mAppWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
    }



    private void configureWidget(AppWidgetProviderInfo appWidgetInfo) {
        AppWidgetHostView hostView = mAppWidgetHost.createView(this, mAppWidgetId, appWidgetInfo);
        // Configure and add the hostView to your layout
        // For example, you might use a `FrameLayout` in your layout XML.
//        FrameLayout widgetContainer = findViewById(R.id.widgetContainer);
//        widgetContainer.addView(hostView);
//        mAppWidgetHost.startAppWidget(appWidgetId);
    }
    private void removeWidget() {
        // Perform the necessary operations to remove a widget.
        mAppWidgetHost.deleteAppWidgetId(mAppWidgetId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppWidgetHost.stopListening();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == RESULT_OK) {
            int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                AppWidgetHost appWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
                appWidgetHost.startListening();

//                AppWidgetHostView hostView = appWidgetHost.createView(this, widgetId, YOUR_APPWIDGET_PROVIDER);

//                yourLayout.addView(hostView);

                appWidgetHost.stopListening();

            }
        }
    }

}