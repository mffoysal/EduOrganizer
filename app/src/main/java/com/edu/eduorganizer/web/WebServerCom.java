package com.edu.eduorganizer.web;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import fi.iki.elonen.NanoHTTPD;

public class WebServerCom extends NanoHTTPD {
    private static final String INDEX_HTML_PATH = "indexx.html";
    private static final String PROFILE_HTML_PATH = "profile.html";
    public int PORT;
    private Map<String, String> sessionMap = new HashMap<>();
    private Context context;
    private IHTTPSession session;
    private String cookieHeader;
    private Map<String, String> cookies;
    private Map<String, List<String>> queryParams;
    private List<String> values;
    private StringBuilder postRequestData;

    public WebServerCom(Context context, int port) {
        super(port);
        this.PORT=port;
        this.context = context;
    }

    public WebServerCom(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String htmlContent = readHtmlContentFromAssets(context.getApplicationContext());
//        return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, htmlContent);

        cookieHeader = session.getHeaders().get("cookie");
        if (cookieHeader != null) {
            cookies = parseCookies(cookieHeader);

            // Now you can access individual cookies by their names
            String sessionToken = cookies.get("sessionToken");
            // ... (other cookie processing)
        }

//        getRequest(session);
//        postRequestData = postRequest(session);
        this.session = session;
        String uri = session.getUri();
        Method method = session.getMethod();
        switch (method) {
            case GET:
                return handleGetRequest(uri,session);
            case POST:
                return handlePostRequest(session);
            default:
                return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, NanoHTTPD.MIME_PLAINTEXT, "Method not allowed.");
        }

    }

    private void getRequest(IHTTPSession session) {
        queryParams = session.getParameters();

        // Access individual URL parameters
        values = queryParams.get("edu");
        if (values != null && !values.isEmpty()) {
            String value = values.get(0);
            // Process the parameter value
        }

        if (queryParams.containsKey("edu")) {
            // Get the value of the "edu" parameter
            List<String> eduValues = queryParams.get("edu");
            if (!eduValues.isEmpty()) {
                String eduValue = eduValues.get(0);
                if ("desiredValue".equals(eduValue)) {
                    // Perform actions based on the value of "edu"
                    // For example, if "edu" equals "desiredValue", do something
                }
            }
        }
    }

    private StringBuilder postRequest(IHTTPSession session) {
        try {
            // Get input stream to read data from the request body
            InputStream inputStream = session.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the data from the input stream
            StringBuilder requestData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestData.append(line);
            }

            // Process the request data
            String data = requestData.toString();
            // ... (process data)
            return requestData;

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
            return null;
        }


    }

    private String loadFileContent(String filePath) {
        Log.e("web",filePath);
        try {
            InputStream inputStream = context.getAssets().open(filePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMimeTypeForFile(String uri) {
        if (uri.endsWith(".css")) {
            return "text/css";
        } else if (uri.endsWith(".js")) {
            return "application/javascript";
        }
        return "text/plain";
    }

    private Response handleGetRequest(String uri,IHTTPSession session) {
//        if ("/".equals(uri)) {
//            // Serve the index.html page
//            return serveStaticFile(INDEX_HTML_PATH);
//        } else if ("/edu".equals(uri)) {
//            // Handle a custom endpoint for "hello" route
//            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "Hello, from the server!");
//        } else {
//            // Return 404 if the requested resource is not found
//            return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Not Found");
//        }

        if ("/".equals(uri)) {
            // Serve index.html with linked CSS and JS
//            String htmlResponse = loadFileContent(""+INDEX_HTML_PATH);
            String htmlResponse = loadAndFillTemplate(""+INDEX_HTML_PATH);

            return newFixedLengthResponse(Response.Status.OK, "text/html", htmlResponse);
        } else if ("/edu".equals(uri)) {
            // Handle a custom endpoint for "hello" route
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "Hello, from the server!");
        } else if ("/copy".equals(uri)) {
            // Handle a custom endpoint for "hello" route
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "Hello, from the server!");
        }else if ("/login".equals(uri)) {
            // Simulate a successful login

            String sessionToken = getSessionTokenFromCookie(session);
//            if (isValidSession(sessionToken)) {
//                // User is logged in, provide profile content
//                Log.e("webprofile","profilePage");
//                String username = sessionMap.get(sessionToken);
//                String htmlResponse = loadAndFillTemplate(""+INDEX_HTML_PATH);
//                return newFixedLengthResponse(Response.Status.OK, "text/html", htmlResponse);
//            }
            String username = "01585855075";
            String sessionTokenn = generateSessionToken();
            sessionMap.put(sessionTokenn, username);
            Log.e("webprofile","LoginPro");


            String htmlResponse = loadAndFillTemplate(""+INDEX_HTML_PATH);
            Response response = newFixedLengthResponse(Response.Status.OK, "text/html", htmlResponse);
            response.addHeader("Set-Cookie", "sessionToken=" + sessionTokenn + "; path=/");
            response.addHeader("Location", "/profile");
            return response;


        } else if ("/profile".equals(uri)) {
            String sessionToken = getSessionTokenFromCookie(session);
            if (isValidSession(sessionToken)) {
                // User is logged in, provide profile content
                Log.e("webprofile","profilePage");
                String username = sessionMap.get(sessionToken);
                String htmlResponse = loadAndFillTemplate(""+PROFILE_HTML_PATH);
                return newFixedLengthResponse(Response.Status.OK, "text/html", htmlResponse);
            }else {
                Log.e("webprofile", "profileToLogin");

                // User is not logged in, redirect to login page
                String htmlResponse = loadAndFillTemplate("" + INDEX_HTML_PATH);
                Response response = newFixedLengthResponse(Response.Status.REDIRECT, "text/html", htmlResponse);
                response.addHeader("Location", "/login"); // Specify the URL of the login page
                return response;
            }

        }else if ("/out".equals(uri)) {
            // User logs out, remove session
            String sessionToken = getSessionTokenFromCookie(session);
            if (sessionToken != null) {
                Log.e("webprofile","sessionRemove");
                sessionMap.remove(sessionToken);
                String htmlResponse = loadAndFillTemplate(""+INDEX_HTML_PATH);
                Response response = newFixedLengthResponse(Response.Status.OK, "text/html", htmlResponse);
                response.addHeader("Set-Cookie", "sessionToken=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/");
                return response;
            }
            Log.e("webprofile","logoutRedirect");

            // Redirect to login page
            String htmlResponse = loadAndFillTemplate(""+INDEX_HTML_PATH);
            Response response = newFixedLengthResponse(Response.Status.REDIRECT, "text/html", htmlResponse);
//            response.addHeader("Set-Cookie", "sessionToken=null; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/");
            response.addHeader("Location", "/");
            return response;
        } else if ("/getUsername".equals(uri)) {
            String sessionToken = getSessionTokenFromCookie(session);
            if (isValidSession(sessionToken)) {
                String username = sessionMap.get(sessionToken);
                return newFixedLengthResponse(Response.Status.OK, "text/plain", username);
            } else {
                return newFixedLengthResponse(Response.Status.UNAUTHORIZED, "text/plain", "Not authorized");
            }
        } else if ("/getCopy".equals(uri)) {
            String clipboardText = getClipboardText();

            if (clipboardText != null) {
                return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, clipboardText);
            } else {
                return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "Clipboard is empty.");
            }
        }


        // Serve other files (CSS and JS)
        String mimeType = getMimeTypeForFile(uri);
        String fileContent = loadFileContent("web" + uri);
        return newFixedLengthResponse(Response.Status.OK, mimeType, fileContent);


    }

    private String getClipboardText() {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                CharSequence text = clipData.getItemAt(0).getText();
                if (text != null) {
                    return text.toString();
                }
            }
        }

        return null;
    }

    private Map<String, String> parseCookies(String cookieHeader) {
        Map<String, String> cookies = new HashMap<>();
        String[] cookieParts = cookieHeader.split("; ");
        for (String cookiePart : cookieParts) {
            String[] cookie = cookiePart.split("=", 2);
            if (cookie.length == 2) {
                cookies.put(cookie[0], cookie[1]);
            }
        }
        return cookies;
    }

    private Map<String, String> convertParametersToMap(IHTTPSession session) {
        Map<String, String> parameterMap = new HashMap<>();
        Map<String, List<String>> params = session.getParameters();

        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            // If there are multiple values for a parameter, you can choose how to handle them
            // For this example, we'll concatenate them with commas
            String value = String.join(" ", values);

            parameterMap.put(key, value);
        }

        return parameterMap;
    }


    private Response handlePostRequest(IHTTPSession session) {
        Map<String, String> postData = new HashMap<>();
        try {
            session.parseBody(postData);
            postData = convertParametersToMap(session);
            if (!postData.isEmpty()) {
                String message = postData.get("username");
                String copy = postData.get("copytext");
                if (message != null) {
                    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "1");
                } else if (copy!=null) {

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Edubox Text", copy);
                    clipboard.setPrimaryClip(clip);

                    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "1");

                } else {
                    return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, "Invalid request.");
                }
            } else {
                return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, "No form data received."+session.getParameters());
            }
//            if (message != null) {
//                return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "Received message: " + message);
//            } else {
//                return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, "Invalid request.");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Internal Server Error.");
        }
    }

    private String loadAndFillTemplate(String templateFilePath) {
        String sessionToken = getSessionTokenFromCookie(session);

        String template = loadFileContent(templateFilePath);
        if (isValidSession(sessionToken)) {
            // User is logged in, provide profile content
            String username = sessionMap.get(sessionToken);
            template = template.replace("{dynamic_content}", "BlackBox!"+username);
        }else {
            template = template.replace("{dynamic_content}", "BlackBox!");
        }


//        template = template.replace("<div id=\"container\">", "<div id=\"container\" style=\"display: none;\">");
        template = template.replace("{image_url}", "path/to/your/image.png");
        return template;
    }

    public boolean setLogin(){

//        // After successful login
//        String sessionToken = generateSessionToken(); // Generate a unique session token
//        CookieHandler.setSessionCookie(sessionToken);
//
//// On subsequent requests
//        String sessionToken = CookieHandler.getSessionCookie();
//        if (isValidSession(sessionToken)) {
//            // User is logged in, provide appropriate content
//            return true;
//        } else {
//            // User is not logged in, provide login form or other content
//            return false;
//        }

        return true;
    }

    private String generateSessionToken() {
        return UUID.randomUUID().toString();
    }

    private String getSessionTokenFromCookie(IHTTPSession session) {
        Map<String, String> headers = session.getHeaders();
        String cookieHeader = headers.get("cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                if (cookie.trim().startsWith("sessionToken=")) {
                    return cookie.trim().substring("sessionToken=".length());
                }
            }
        }
        return null;
    }

    private boolean isValidSession(String sessionToken) {
        return sessionToken != null && sessionMap.containsKey(sessionToken);
    }

    private String loadAndModifyTemplate(String templateFilePath, boolean isLoggedIn) {
        String template = loadFileContent(templateFilePath);

        if (isLoggedIn) {
            // Show content for logged-in users
            template = template.replace("<div id=\"loginContainer\">", "<div id=\"loginContainer\" style=\"display: none;\">");
        } else {
            // Show content for non-logged-in users
            template = template.replace("<div id=\"logoutContainer\">", "<div id=\"logoutContainer\" style=\"display: none;\">");
        }

        return template;
    }


    private Response serveStaticFile(String filename) {
        try {
            InputStream inputStream = context.getAssets().open(filename);
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.getMimeTypeForFile(filename), inputStream, inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Internal Server Error.");
        }
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
