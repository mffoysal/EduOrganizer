package com.edu.eduorganizer.web;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.wifi.HotspotManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

public class WifiWeb extends AppCompatActivity {

    private WebServerTwo webServer;
    private static final int PORT = 3697;
    private ImageView qrId;
    private TextView hotspotUrlTextView;
    private WebServerTwo webServer1;
    private HotspotManager hotspotManager;
    private ToggleButton server1, server2, server3, server4, server5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_web);
        hotspotManager = new HotspotManager(this);
        hotspotUrlTextView = findViewById(R.id.sessionName);

//        getIp();

        qrId =  findViewById(R.id.recImage);
        server1 = findViewById(R.id.toggleButton);
        server2 = findViewById(R.id.toggleButton2);
        server3 = findViewById(R.id.toggleButton3);
        server4 = findViewById(R.id.toggleButton4);

        server1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startWebServer();
                    getIp();
                } else {
                    stopWebServer();
                    getIp();
                }
            }
        });

        server2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle button is in the "on" state
                    // Perform action for "on" state
                } else {
                    // Toggle button is in the "off" state
                    // Perform action for "off" state
                }
            }
        });
        server3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle button is in the "on" state
                    // Perform action for "on" state
                } else {
                    // Toggle button is in the "off" state
                    // Perform action for "off" state
                }
            }
        });
        server4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle button is in the "on" state
                    // Perform action for "on" state
                } else {
                    // Toggle button is in the "off" state
                    // Perform action for "off" state
                }
            }
        });

    }

    private void getIp() {
        String hotspotIpAddress = hotspotManager.getWifiIpAddress();
        if (hotspotIpAddress != null) {
            String url = "http://" + hotspotIpAddress + ":" + PORT + "/";
            hotspotUrlTextView.setText(""+ url);
            Log.d("Hotspot", "Hotspot URL: " + url);
        } else {
            hotspotUrlTextView.setText("Hotspot URL: Not available");
            Log.e("Hotspot", "Failed to obtain hotspot IP address.");
        }
    }


    private void startWebServer() {
//        Log.d("WebServer", "Web server started at http://localhost:" + PORT);

        try {
            webServer = new WebServerTwo(getApplicationContext(),PORT);
            webServer.start();
            Log.d("WebServer", "Web server started at http://localhost:" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("WebServer", "Error starting the web server: " + e.getMessage());
        }
    }

    private void stopWebServer() {
        if (webServer != null) {
            webServer.stop();
            Log.d("WebServer", "Web server stopped");
        }
    }

    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();

    }


    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn!= null && wifiConn.isConnected()) || (dataConn != null && dataConn.isConnected())){
            return true;
        }else {
            return false;
        }

    }

    private Bitmap generateQRCode(String textToEncode) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            BitMatrix bitMatrix = barcodeEncoder.encode(textToEncode, BarcodeFormat.QR_CODE, 300, 300);
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            Log.e("QRCodeGenerator", "Error generating QR code", e);
            return null;
        }
    }

    private Bitmap generateBarcode(String textToEncode) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            BitMatrix bitMatrix = barcodeEncoder.encode(textToEncode, BarcodeFormat.CODE_128, 1000, 600);
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            Log.e("BarcodeGenerator", "Error generating barcode", e);
            return null;
        }
    }

    private Bitmap generateCode128Barcode(String textToEncode) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            BitMatrix bitMatrix = barcodeEncoder.encode(textToEncode, BarcodeFormat.CODE_128, 100, 100);
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            Log.e("Code128BarcodeGenerator", "Error generating Code 128 barcode", e);
            return null;
        }
    }

    private Bitmap generateDisplayBarcode(String numericValue) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap barcodeBitmap = barcodeEncoder.encodeBitmap(numericValue, BarcodeFormat.CODE_128, 600, 300);
            qrId.setImageBitmap(barcodeBitmap);
            return barcodeBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void startHotspot() {
        String ssid = "edubox";
        String password = "edubox@3697";
        boolean success = hotspotManager.startHotspot(ssid, password);
        if (success) {
            String hotspotIpAddress = hotspotManager.getHotspotIpAddress();
            if (hotspotIpAddress != null) {
                String url = "http://" + hotspotIpAddress + ":" + webServer.PORT + "/";
                Log.d("Hotspot", "Hotspot URL: " + url);
            }
        } else {
            Log.e("Hotspot", "Failed to start hotspot.");
        }
    }

    private void stopHotspot() {
        hotspotManager.stopHotspot();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopHotspot();
        stopWebServer();
    }
}