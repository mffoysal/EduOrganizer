package com.edu.eduorganizer.widget;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.edu.eduorganizer.R;

public class WidgetSetup extends AppCompatActivity {
    private static final int REQUEST_PICK_APPWIDGET = 1;
    private static final int REQUEST_CREATE_APPWIDGET = 2;
    private static final int APPWIDGET_HOST_ID = 3;

    private AppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;
    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_setup);

        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
        mAppWidgetHost.startListening();

        // Check if the App Widget Host ID has been set
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mAppWidgetHost.getAppWidgetIds().length == 0) {
                mAppWidgetHost.allocateAppWidgetId();
            }
        }

        mAppWidgetId = mAppWidgetHost.allocateAppWidgetId();
        selectWidget();
    }

    private void selectWidget() {
        int appWidgetId = mAppWidgetId;
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                int appWidgetId = mAppWidgetId;
                configureWidget(data, appWidgetId);
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                completeAddAppWidget(data);
            }
        }
    }

    private void configureWidget(Intent data, int appWidgetId) {
        int appWidgetInfoId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetInfoId);

        if (appWidgetInfo.configure != null) {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } else {
            completeAddAppWidget(data);
        }
    }

    private void completeAddAppWidget(Intent data) {
        int appWidgetId = mAppWidgetId;
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

        if (appWidgetInfo.configure != null) {
            // The widget needs configuration; display the configuration activity
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } else {
            // The widget doesn't need configuration; add it to the home screen
            AppWidgetHostView hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
            hostView.setAppWidget(appWidgetId, appWidgetInfo);

            // Find a layout or view in your activity where you want to add the widget
//            LinearLayout layout = findViewById(R.id.widgetContainer);
//            layout.addView(hostView);

            // Save the widget ID for future reference if needed
            // Save the appWidgetId and its configuration settings as needed
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppWidgetHost.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }
}
