package com.dlsu.ccs.signin;

/**
 * Created by Lance on 11/30/2015.
 */


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.HttpURLConnection;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.widget.TextView;

public class SigninActivity extends AsyncTask<String, Void, String> {
    private TextView statusField, roleField;
    private Context context;
    private final String USER_AGENT = "Mozilla/5.0";
    private String token = null;
    private int access = 0;
    private String name;
    SharedPreferences prefs = null;

    public SigninActivity(Context context, TextView statusField) {
        this.context = context;
        this.statusField = statusField;
    }


    @Override
    protected String doInBackground(String... params) {
        String link = "http://192.168.0.201/phpprojects/Login.php";
        URL url = null;
        try {
            url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection != null) {
                urlConnection.setConnectTimeout(180000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", USER_AGENT);
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = "uname=" + params[0] + "&pass=" + params[1] + "&ipAddress=" + getIpAddress()
                        + "&macAddress=" + getMacAddress();
                name = params[0];

                // Send post request
                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                int responseCode = urlConnection.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));

                String inputLine;

                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                access = checkAccess(response);
                getToken(response);
                System.out.println(response.toString());
                removePreviousSessionData();
                return token;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private void removePreviousSessionData() {
        String filename1 = "runningApps.txt";
        File appRunningFile= new File(context.getFilesDir(), filename1);
        if (appRunningFile.delete()) {
            System.out.println("Application RunningApp File successfully refreshed");
        } else {
            System.out.println("Failed to refresh Running AppFile");
        }
    }

    private String getIpAddress() {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wm.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        System.out.print("IP Address:" + ip + "\n");
        return ip;
    }

    private String getMacAddress() {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String mac = info.getMacAddress();
        System.out.print("Mac Address:" + mac);
        return mac;

    }

    private int checkAccess(StringBuffer response) {
        if (response.toString().contains("Access-Granted"))
            return 1;
        else if (response.toString().contains("Access-Denied"))
            return 0;
        return -1;
    }

    private void getToken(StringBuffer response) {
        token = response.substring(response.indexOf("userToken=") + 10, response.indexOf("!endOfToken")).toString(); //extracts token
        System.out.print("This is the Token" + token);
        prefs = context.getSharedPreferences("com.dlsu.ccs.signin", Context.MODE_PRIVATE);
        prefs.edit().putString("token", token).apply();

    }

    @Override
    protected void onPostExecute(String result) {
        if (access == 1) {
            this.statusField.setText("You have been granted Access to the Network");
        } else if (access == 0) {
            this.statusField.setText("Access Denied, Pls input your login credentials again");
        } else if (access == -1) {
            this.statusField.setText("Ooops, Something went wrong pls try again later or contact your administrator");
        }
        System.out.println(access);
    }
}

