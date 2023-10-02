package com.edu.eduorganizer.scanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.edu.eduorganizer.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ScanIDActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_id);

        // Initialize the barcode scanner
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE); // Set the desired barcode format (QR code)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES); // Scan all barcode formats
        integrator.setPrompt("Scan an ID card");
        integrator.setCaptureActivity(AutoFocus.class); // Use a custom capture activity that enables autofocus
        integrator.setOrientationLocked(false); // Allow rotation
        integrator.setBeepEnabled(true); // Enable beep sound

//        integrator.setCameraId(CameraSelector.DEFAULT_BACK_CAMERA); // Use the back camera
        integrator.setTorchEnabled(false); // Disable torch (flashlight)
//        integrator.setScanningRectangle(IntentIntegrator.ALL_CODE_TYPES); // Set the scanning rectangle, if needed


        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of the barcode scan
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Extracted QR code content
                String qrCodeContent = result.getContents();

                // TODO: Handle the QR code content, e.g., redirect to another activity
                Intent intent = new Intent(this, SetAttendance.class);
                intent.putExtra("eduboxId", qrCodeContent);
                startActivity(intent);
            }
        }
    }
}
