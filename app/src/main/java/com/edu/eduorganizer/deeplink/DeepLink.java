package com.edu.eduorganizer.deeplink;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.bubble.MaxBubble;
import com.edu.eduorganizer.start.Start;

import java.util.List;

public class DeepLink extends Activity {

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 123;
    private Intent intent;
    private Button bubbleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        goToPage();

        bubbleBtn = findViewById(R.id.bubbleId);
        bubbleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(DeepLink.this, MaxBubble.class);
                startService(intent);
                finish();
            }
        });
    }

    private void goToPage() {
        intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            List<String> pathSegments = data.getPathSegments();
            if (pathSegments.size() >= 3) { // Assuming at least three segments in the URL
                String value = pathSegments.get(0);
                String value1 = pathSegments.get(1);
                String value2 = pathSegments.get(2);

                TextView textView1 = findViewById(R.id.Link);
//                TextView textView2 = findViewById(R.id.textViewValue2);
                textView1.setText(value1);
//                textView2.setText(value2);
            } else if (pathSegments.size() >= 2) {

                String value1 = pathSegments.get(0);
                String value2 = pathSegments.get(1);

            }else if (pathSegments.size() >= 1) {
                String value1 = pathSegments.get(0);
                TextView textView1 = findViewById(R.id.Link);
                textView1.setText(value1);
            }else if (pathSegments.size() >= 4) {

                String value = pathSegments.get(0);
                String value1 = pathSegments.get(1);
                String value2 = pathSegments.get(2);
                String value3 = pathSegments.get(3);

            }else if (pathSegments.size() >= 5) {
                String value = pathSegments.get(0);
                String value1 = pathSegments.get(1);
                String value2 = pathSegments.get(2);
                String value3 = pathSegments.get(3);
                String value4 = pathSegments.get(4);
            }else if (pathSegments.size() >= 6) {
                String value = pathSegments.get(0);
                String value1 = pathSegments.get(1);
                String value2 = pathSegments.get(2);
                String value3 = pathSegments.get(3);
                String value4 = pathSegments.get(4);
                String value5 = pathSegments.get(5);
            }else if (pathSegments.size() >= 7) {

                String value = pathSegments.get(0);
                String value1 = pathSegments.get(1);
                String value2 = pathSegments.get(2);
                String value3 = pathSegments.get(3);
                String value4 = pathSegments.get(4);
                String value5 = pathSegments.get(5);
                String value6 = pathSegments.get(6);

            }else {
                intent = new Intent(getApplicationContext(), Start.class);
                startActivity(intent);
                finish();
            }
        }else {
            intent = new Intent(getApplicationContext(), Start.class);
            startActivity(intent);
            finish();
        }
    }


    private boolean hasOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true; // Always return true for lower Android versions
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            // Check if permission was granted
            if (hasOverlayPermission()) {
                // Permission granted, proceed with your app logic
            } else {
                // Permission not granted
                Toast.makeText(this, "Overlay permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


}