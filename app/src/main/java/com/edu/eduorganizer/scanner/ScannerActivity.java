package com.edu.eduorganizer.scanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.webview.activity_webview;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScannerActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private CameraSource cameraSource;

    private static final int CAMERA_PERMISSION_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        surfaceView = findViewById(R.id.surfaceView);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                if (ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(ScannerActivity.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    String url = barcodes.valueAt(0).displayValue;
//                    cameraSource.stop();
                    openUrlInWebView(url);
                }else {
                    Toast.makeText(getApplicationContext(),"Hello Farhad Foysal",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openUrlInWebView(final String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                surfaceView.setVisibility(View.GONE);
//                WebView webView = findViewById(R.id.webVieww);
//                webView.setVisibility(View.VISIBLE);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.loadUrl(url);

                // Optional: You can also open the URL in the default browser
                // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                // startActivity(intent);
                Intent intent = new Intent(getApplicationContext(), activity_webview.class);
                intent.putExtra("url",url);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}