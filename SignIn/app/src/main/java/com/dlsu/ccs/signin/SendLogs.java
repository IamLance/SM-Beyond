package com.dlsu.ccs.signin;

import android.app.IntentService;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lance on 12/28/2015.
 */
public class SendLogs extends IntentService {
    private String token;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SendLogs() {
        super(SendLogs.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        token = intent.getExtras().getString("token");
        try {
            sendLogs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendLogs() throws IOException {
        String violationReceiverLink = "http://192.168.0.201/phpprojects/AppViolationLogReceiver.php";
        BeyondSQLiteHelper db = new BeyondSQLiteHelper(this);
        ArrayList<AppViolationLog> beanList = new ArrayList<>();
        ArrayList<AppLog> allAppLog = null;
        try {

            doPOSTAppViolation(violationReceiverLink, db.getAllAppViolationLog());
            if (db.getAllAppRunningLog().size()>0) {
                db.getAllAppRunningLog().addAll(db.getAllAppInstallLog());
                allAppLog = db.getAllAppRunningLog();
            } else {

                allAppLog = db.getAllAppInstallLog();
            }
            doPOSTAppLogging("http://192.168.0.201/phpprojects/AppLoggingReceiver.php", allAppLog);
            doPOSTFileLogging("http://192.168.0.201/phpprojects/FileLoggingReceiver.php", db.getAllFileLog());
            db.deleteAllAppViolationLog();
            db.deleteAllAppRunningLog();
            db.deleteAllInstallLog();
            db.deleteAllFileLog();
        } catch (RuntimeException e) {

        }

    }

    public String doPOSTAppViolation(String link, ArrayList<AppViolationLog> beanList) {
        InputStream inputStream = null;
        final String USER_AGENT = "Mozilla/5.0";
        String result = "";
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // 2. make POST request to the given URL
            if (urlConnection != null) {

                urlConnection.setConnectTimeout(180000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", USER_AGENT);
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                JSONArray jsonLogList = new JSONArray();
                for (int i = 0; i < beanList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userToken", beanList.get(i).getUserToken());
                    jsonObject.put("timestamp", beanList.get(i).getTimestamp());
                    jsonObject.put("mac", getMacAddress());
                    jsonObject.put("appName", beanList.get(i).getAppName());
                    jsonObject.put("type", beanList.get(i).getType());
                    jsonLogList.put(jsonObject);
                }


                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(jsonLogList.toString());
                wr.flush();


                //display what returns the POST request
                StringBuilder sb = new StringBuilder();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    br.close();

                    System.out.println("RESPO: " + sb.toString());
                    result = sb.toString();
                    return result;
                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }
            }


        } catch (Exception ignored) {
        }
        return null;
    }

    public String doPOSTAppLogging(String link, ArrayList<AppLog> beanList) {
        InputStream inputStream = null;
        final String USER_AGENT = "Mozilla/5.0";
        String result = "";
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // 2. make POST request to the given URL
            if (urlConnection != null) {
                urlConnection.setConnectTimeout(180000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", USER_AGENT);
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                JSONArray jsonLogList = new JSONArray();
                for (int i = 0; i < beanList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userToken", beanList.get(i).getUserToken());
                    jsonObject.put("timestamp", beanList.get(i).getTimestamp());
                    jsonObject.put("mac", getMacAddress());
                    jsonObject.put("appName", beanList.get(i).getAppName());
                    jsonObject.put("event", beanList.get(i).getEvent());
                    jsonObject.put("type", beanList.get(i).getType());
                    jsonLogList.put(jsonObject);
                }


                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(jsonLogList.toString());
                wr.flush();


                //display what returns the POST request
                StringBuilder sb = new StringBuilder();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    br.close();

                    System.out.println("RESPO: " + sb.toString());
                    result = sb.toString();
                    return result;
                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }
            }


        } catch (Exception ignored) {
        }
        return null;
    }

    public String doPOSTFileLogging(String link, ArrayList<FileLog> beanList) {
        InputStream inputStream = null;
        final String USER_AGENT = "Mozilla/5.0";
        String result = "";
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // 2. make POST request to the given URL
            if (urlConnection != null) {
                urlConnection.setConnectTimeout(180000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", USER_AGENT);
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                JSONArray jsonLogList = new JSONArray();
                for (int i = 0; i < beanList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userToken", beanList.get(i).getUserToken());
                    jsonObject.put("timestamp", beanList.get(i).getTimestamp());
                    jsonObject.put("mac", getMacAddress());
                    jsonObject.put("path", beanList.get(i).getPath());
                    jsonObject.put("event", beanList.get(i).getEvent());
                    jsonObject.put("type", beanList.get(i).getType());
                    jsonLogList.put(jsonObject);
                }


                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(jsonLogList.toString());
                wr.flush();


                //display what returns the POST request
                StringBuilder sb = new StringBuilder();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    br.close();

                    System.out.println("RESPO: " + sb.toString());
                    result = sb.toString();
                    return result;
                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }
            }


        } catch (Exception ignored) {
        }
        return null;
    }

    private String getMacAddress() {
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String mac = info.getMacAddress();
        return mac;
    }
}
