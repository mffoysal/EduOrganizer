package com.edu.eduorganizer.widget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.bubble.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class WidgetConfig extends AppCompatActivity {

    private SwitchCompat widgetSwitch;
    private SharedPreferences sharedPreferences;
    private int appWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        widgetSwitch = findViewById(R.id.widgetSwitch); // Replace with your UI element
        sharedPreferences = getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE);

        boolean isWidgetEnabled = sharedPreferences.getBoolean("widgetEnabled", false);
        widgetSwitch.setChecked(isWidgetEnabled);

        widgetSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            enableOrDisableWidget(isChecked);
        });

        // Retrieve the App Widget ID from the intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Check if the widget ID is valid
        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            // The widget ID is valid, enable the widget here
            enableWidget(appWidgetId);
        }
    }

    private void enableWidget(int widgetId) {
        // Get the AppWidgetManager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        // Perform widget configuration or enable logic here
        RemoteViews yourRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_enabled);

        // Update the widget
        appWidgetManager.updateAppWidget(widgetId, yourRemoteViews);

        // Finish the configuration activity
        finish();
    }

    private void enableOrDisableWidget(boolean isEnabled) {
        // Save the enabled/disabled state to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("widgetEnabled", isEnabled);
        editor.apply();

        // Get the AppWidgetManager and the ComponentName of your widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName widgetProvider = new ComponentName(this, Widget.class);

        // Enable or disable the widget based on the isChecked state
        if (isEnabled) {
            // Update the widget's layout for the enabled state
            RemoteViews widgetRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_enabled);
            appWidgetManager.updateAppWidget(widgetProvider, widgetRemoteViews);
        } else {
            // Update the widget's layout for the disabled state
            RemoteViews widgetRemoteViews = new RemoteViews(getPackageName(), R.layout.widget_disabled);
            appWidgetManager.updateAppWidget(widgetProvider, widgetRemoteViews);
        }
    }

}
