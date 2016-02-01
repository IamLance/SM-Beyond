package com.dlsu.ccs.signin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LogoutActivity extends AsyncTask<String,Void,String> {
    private Context context;
    private final String USER_AGENT = "Mozilla/5.0";
    private SharedPreferences prefs = null;

    public LogoutActivity(){

    }

    public LogoutActivity(Context context) {
        this.context = context;
    }

    protected String doInBackground(String... params) {
        String link = "http://192.168.0.201/phpprojects/Logout.php";
        URL url = null;
        try {
            String token=null;
            url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if(urlConnection!=null) {
                urlConnection.setConnectTimeout(180000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", USER_AGENT);
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                prefs = context.getSharedPreferences("com.dlsu.ccs.signin", Context.MODE_PRIVATE);
                token = prefs.getString("token","");
                System.out.print(token);
                String urlParameters = "token="+token;


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

                StringBuffer response =  new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result

                System.out.println(response.toString());

                return response.toString();
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

    protected void onPostExecute(){


    }
}
