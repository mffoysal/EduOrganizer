package com.edu.eduorganizer.web;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {
    private static final String INDEX_HTML_PATH = "index.html";
    private Context context;

    public WebServer(Context context, int port) {
        super(port);
        this.context = context;
    }

    public WebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String htmlContent = readHtmlContentFromAssets(context.getApplicationContext());
        return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, htmlContent);
    }

    private String readHtmlContentFromAssets(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(INDEX_HTML_PATH);
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading HTML content";
        }
    }
}
