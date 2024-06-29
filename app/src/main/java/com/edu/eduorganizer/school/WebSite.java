package com.edu.eduorganizer.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.fragment.EduCampus;
import com.edu.eduorganizer.mess.SecondHome;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebSite extends AppCompatActivity {
    private static String file_type     = "*/*";
    private String cam_file_data = null;
    private ValueCallback<Uri> file_data;
    private ValueCallback<Uri[]> file_path;
    private final static int file_req_code = 1;
    private static final int FILE_CHOOSER_REQUEST_CODE = 1;
    private WebView webView;
    private ValueCallback<Uri[]> mFilePathCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_site);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }



        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.endsWith(".pdf")) {
                    // If the URL ends with .pdf, open it using an external PDF viewer
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false; // Load the URL in the WebView
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // This is called when the webpage finishes loading
            }

        });
        webView.getSettings().setJavaScriptEnabled(true);

//        WebSettings webSettings = webView.getSettings();


        String desktopUserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
        webView.getSettings().setUserAgentString(desktopUserAgent);
        webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(true);

        webView.addJavascriptInterface(new JSInterface(), "JSInterface");
        webView.loadUrl("https://edu.orbund.com");
        webView.loadUrl("javascript:(function() { " +
                "var pdfContent = generatePDF(); " +
                "window.JSInterface.handlePDFContent(pdfContent); " +
                "})();");


        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    // Handle file download for HTTP/HTTPS URIs
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "eduOrganizerFF");
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                } else if (mimetype.equalsIgnoreCase("application/pdf")) {



//                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                    request.setMimeType(mimetype);
//                    request.allowScanningByMediaScanner();
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                    // Set the destination directory and file name
//                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "eduOrganizerFF.pdf");
//                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                    dm.enqueue(request);
                } else {
                    // Handle non-HTTP/HTTPS URIs differently if needed
                    // You can show an error message or handle them according to your requirements
                    // For example, you can display a Toast message to inform the user
                    Toast.makeText(getApplicationContext(), "Unsupported download URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the WebView content
                webView.reload();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (file_permission() && Build.VERSION.SDK_INT >= 21) {
                    file_path = filePathCallback;

                    Intent intent = fileChooserParams.createIntent();
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);

                    return true;
                } else {
                    return false;
                }
            }
        });


//        webView.setWebChromeClient(new WebChromeClient(){
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//
//                if(file_permission() && Build.VERSION.SDK_INT >= 21) {
//                    file_path = filePathCallback;
//                    Intent takePictureIntent = null;
//                    Intent takeVideoIntent = null;
//
//                    boolean includeVideo = false;
//                    boolean includePhoto = false;
//
//                    /*-- checking the accept parameter to determine which intent(s) to include --*/
//
//                    paramCheck:
//                    for (String acceptTypes : fileChooserParams.getAcceptTypes()) {
//                        String[] splitTypes = acceptTypes.split(", ?+");
//                        /*-- although it's an array, it still seems to be the whole value; split it out into chunks so that we can detect multiple values --*/
//                        for (String acceptType : splitTypes) {
//                            switch (acceptType) {
//                                case "*/*":
//                                    includePhoto = true;
//                                    includeVideo = true;
//                                    break paramCheck;
//                                case "image/*":
//                                    includePhoto = true;
//                                    break;
//                                case "video/*":
//                                    includeVideo = true;
//                                    break;
//                            }
//                        }
//                    }
//
//                    if (fileChooserParams.getAcceptTypes().length == 0) {
//
//                        /*-- no `accept` parameter was specified, allow both photo and video --*/
//
//                        includePhoto = true;
//                        includeVideo = true;
//                    }
//
//                    if (includePhoto) {
//                        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        if (takePictureIntent.resolveActivity(WebSite.this.getPackageManager()) != null) {
//                            File photoFile = null;
//                            try {
//                                photoFile = create_image();
//                                takePictureIntent.putExtra("PhotoPath", cam_file_data);
//                            } catch (IOException ex) {
//                                Log.e("eee", "Image file creation failed", ex);
//                            }
//                            if (photoFile != null) {
//                                cam_file_data = "file:" + photoFile.getAbsolutePath();
//                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                            } else {
//                                cam_file_data = null;
//                                takePictureIntent = null;
//                            }
//                        }
//                    }
//
//                    if (includeVideo) {
//                        takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                        if (takeVideoIntent.resolveActivity(WebSite.this.getPackageManager()) != null) {
//                            File videoFile = null;
//                            try {
//                                videoFile = create_video();
//                            } catch (IOException ex) {
//                                Log.e("eee", "Video file creation failed", ex);
//                            }
//                            if (videoFile != null) {
//                                cam_file_data = "file:" + videoFile.getAbsolutePath();
//                                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
//                            } else {
//                                cam_file_data = null;
//                                takeVideoIntent = null;
//                            }
//                        }
//                    }
//
//                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    contentSelectionIntent.setType(file_type);
//
//
//                    Intent[] intentArray;
//                    if (takePictureIntent != null && takeVideoIntent != null) {
//                        intentArray = new Intent[]{takePictureIntent, takeVideoIntent};
//                    } else if (takePictureIntent != null) {
//                        intentArray = new Intent[]{takePictureIntent};
//                    } else if (takeVideoIntent != null) {
//                        intentArray = new Intent[]{takeVideoIntent};
//                    } else {
//                        intentArray = new Intent[0];
//                    }
//
//                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "File chooser");
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
//                    startActivityForResult(chooserIntent, FILE_CHOOSER_REQUEST_CODE);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Stop the refreshing animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

//    private class CustomWebViewClient extends WebViewClient implements com.edu.eduorganizer.school.CustomWebViewClient {
//        @Override
//        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
//            // Handle file upload here.
//            // For example, you can open a file picker dialog.
//            openFilePicker(filePathCallback);
//            return true; // Return true to prevent the default file chooser dialog.
//        }
//    }

//    private void openFilePicker(ValueCallback<Uri[]> filePathCallback) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*"); // Specify the file types you want to allow
//        startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
//        // Store the callback for later use
//        mFilePathCallback = filePathCallback;
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
//            if (mFilePathCallback != null) {
//                Uri[] results = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
//                mFilePathCallback.onReceiveValue(results);
//                mFilePathCallback = null;
//            }
//        }
//    }


    public class threeWeb extends AppCompatActivity {
        @Override
        public void onBackPressed() {
            if(webView.canGoBack()){
                webView.goBack();
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if(webView.canGoBack()){
//                    webView.goBack();
//                }else {
//                    onBackPressed();
//                }
//            }
//        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            // Handle file upload here.
            // For example, you can open a file picker dialog.
            openFilePicker(filePathCallback);
            return true;
        }
    }

    private void openFilePicker(ValueCallback<Uri[]> filePathCallback) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // Specify the file types you want to allow
        startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_CHOOSER_REQUEST_CODE);
        // Store the callback for later use
        mFilePathCallback = filePathCallback;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
//            if (mFilePathCallback != null) {
//                Uri[] results = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
//                mFilePathCallback.onReceiveValue(results);
//                mFilePathCallback = null;
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;

            /*-- if file request cancelled; exited camera. we need to send null value to make future attempts workable --*/
            if (resultCode == Activity.RESULT_CANCELED) {
                file_path.onReceiveValue(null);
                return;
            }

            /*-- continue if response is positive --*/
            if(resultCode== Activity.RESULT_OK){
                if(null == file_path){
                    return;
                }
                ClipData clipData;
                String stringData;

                try {
                    clipData = intent.getClipData();
                    stringData = intent.getDataString();
                }catch (Exception e){
                    clipData = null;
                    stringData = null;
                }
                if (clipData == null && stringData == null && cam_file_data != null) {
                    results = new Uri[]{Uri.parse(cam_file_data)};
                }else{
                    if (clipData != null) {
                        final int numSelectedFiles = clipData.getItemCount();
                        results = new Uri[numSelectedFiles];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            results[i] = clipData.getItemAt(i).getUri();
                        }
                    } else {
                        try {
                            Bitmap cam_photo = (Bitmap) intent.getExtras().get("data");
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            cam_photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            stringData = MediaStore.Images.Media.insertImage(this.getContentResolver(), cam_photo, null, null);
                        }catch (Exception ignored){}

                        results = new Uri[]{Uri.parse(stringData)};
                    }
                }
            }

            file_path.onReceiveValue(results);
            file_path = null;
        }else{
            if(requestCode == file_req_code){
                if(null == file_data) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                file_data.onReceiveValue(result);
                file_data = null;
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }


    public boolean file_permission(){
        if(Build.VERSION.SDK_INT >=23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(WebSite.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }else{
            return true;
        }
    }

    private File create_image() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    private File create_video() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String file_name    = new SimpleDateFormat("yyyy_mm_ss").format(new Date());
        String new_name     = "file_"+file_name+"_";
        File sd_directory   = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(new_name, ".3gp", sd_directory);
    }

    public class JSInterface {
        @JavascriptInterface
        public void handlePDFContent(String pdfContent) {
            // Handle the PDF content here
            savePDFToFile(pdfContent);
        }
        private void savePDFToFile(String pdfContent) {
            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "example.pdf");

            try {
                FileOutputStream fos = new FileOutputStream(pdfFile);
                fos.write(pdfContent.getBytes());
                fos.close();

                // Notify the user that the PDF has been saved

                Toast.makeText(getApplicationContext(), "PDF saved successfully", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                // Handle exceptions, e.g., file I/O errors
            }
        }

    }

}