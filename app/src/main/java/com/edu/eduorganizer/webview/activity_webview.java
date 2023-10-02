package com.edu.eduorganizer.webview;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.edu.eduorganizer.R;

public class activity_webview extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutt);
        webView = findViewById(R.id.webVieww);
//        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("url")) {
            String url = intent.getStringExtra("url");
            webView.loadUrl(url);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the WebView content
                webView.reload();
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Stop the refreshing animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}