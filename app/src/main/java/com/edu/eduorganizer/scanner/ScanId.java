package com.edu.eduorganizer.scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.edu.eduorganizer.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanId extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;

    private DecoratedBarcodeView barcodeView;
    private EditText editTextResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        barcodeView = findViewById(R.id.barcode_view);
        editTextResult = findViewById(R.id.edit_text_result);

        barcodeView.setDecoderFactory(new DefaultDecoderFactory(Collections.singletonList(BarcodeFormat.QR_CODE)));
        // Configure the DecoratedBarcodeView to decode both QR codes and other barcode formats
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        formats.add(BarcodeFormat.CODE_128);
        formats.add(BarcodeFormat.CODE_39);
        formats.add(BarcodeFormat.CODE_93);
        formats.add(BarcodeFormat.PDF_417);
//        formats.addAll(BarcodeFormat.); // Add all supported barcode formats

        barcodeView.setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.setStatusText("");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            barcodeView.resume();
            startScanner();

        } else {
            requestCameraPermission();
        }
    }
    private void startScanner() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String scanResult = result.getText();
                editTextResult.setText(scanResult);
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // Handle possible result points
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeView.resume();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            barcodeView.post(new Runnable() {
                @Override
                public void run() {
                    barcodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
