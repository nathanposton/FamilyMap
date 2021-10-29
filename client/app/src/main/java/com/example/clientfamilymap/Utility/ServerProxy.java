package com.example.clientfamilymap.Utility;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Request.AllEventServiceRequest;
import Request.AllPersonServiceRequest;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.Responses.AllEventServiceResult;
import Result.Responses.AllPersonServiceResult;
import Result.Responses.LoginResult;
import Result.Responses.RegisterResult;

public class ServerProxy {
    //Members --------------------------------------------------------------------------------------

    private static final String LOGIN_URL_TAG = "user/login";
    private static final String REGISTER_URL_TAG = "user/register";
    private static final String EVENTS_URL_TAG = "events";
    private static final String PEOPLE_URL_TAG = "person";
    private static String domainUrl = "http://localhost:8080/";

    //Client-side API calls ------------------------------------------------------------------------

    public static LoginResult login(LoginRequest r) {
        assert (r != null) &&
                (r.getUsername() != null) &&
                (r.getPassword() != null);

        LoginResult loginResult = null;
        try {
            loginResult = request(LOGIN_URL_TAG, null, r, LoginResult.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Login error");
        }

        return loginResult;
    }

    public static RegisterResult register(RegisterRequest r) {
        assert (r != null) &&
                (r.getUsername() != null) &&
                (r.getPassword() != null);

        RegisterResult registerResult = null;
        try {
            registerResult = request(REGISTER_URL_TAG, null, r, RegisterResult.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Register error");
        }

        return registerResult;

    }
    public static AllEventServiceResult getEvents(AllEventServiceRequest request) {
        assert (request != null) && (request.getAuthtoken() != null);

        AllEventServiceResult people = null;
        try {
            people = request(EVENTS_URL_TAG, request.getAuthtoken(), request, AllEventServiceResult.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("getEvents error");
        }
        return people;

    }
    public static AllPersonServiceResult getPeople(AllPersonServiceRequest request) {
        assert (request != null) && (request.getAuthtoken() != null);

        AllPersonServiceResult people = null;
        try {
            people = request(PEOPLE_URL_TAG, request.getAuthtoken(), request, AllPersonServiceResult.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("getPeople error");
        }
        return people;

    }

    //Helper functions -----------------------------------------------------------------------------

    public static void setDomain(String serverHost, String serverPort) {
        domainUrl = "http://" + serverHost + ":" + serverPort + "/";
    }

    private static <T> T request(String requestUrlString, String authToken, Object request,
                             Class<T> responseType) throws IOException {
        HttpURLConnection connection = getHttpUrlConnection(requestUrlString, authToken);

        if (authToken == null) {
            try (OutputStream requestBody = connection.getOutputStream();) {
                writeString(JsonSerializer.serialize(request), requestBody);
            }
        }

        T result = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            String responseBodyString = readString(responseBody);

            result = JsonSerializer.deserialize(responseBodyString, responseType);
        }
        else {
            InputStream responseBody = connection.getErrorStream();
            if (responseBody != null) {
                String responseBodyString = readString(responseBody);

                result = JsonSerializer.deserialize(responseBodyString, responseType);
            }
        }
        return result;
    }

    private static HttpURLConnection getHttpUrlConnection(String requestUrlString, String authToken)
            throws IOException {
        String urlString = domainUrl + requestUrlString;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        if (authToken == null) {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
        }
        else {
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", authToken);
        }
        connection.connect();

        return connection;
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

}
