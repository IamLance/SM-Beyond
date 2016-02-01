package com.dlsu.ccs.signin;

import android.app.IntentService;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Lance on 12/16/2015.
 */
public class LoadAppsAllowed extends IntentService {
    private static final String TAG_SUCCESS = "success";

    private static final String filename = "Applist.txt";


    public LoadAppsAllowed() {
        super(LoadAppsAllowed.class.getName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        HashMap hash = new HashMap();
        JSONParser jParser = new JSONParser();
        JSONObject json = new JSONObject();
        final String token = intent.getExtras().getString("token");
        hash.put("userToken", token);
        try {
            json = jParser.makeHttpRequest("http://192.168.0.201/phpprojects/SendAllowedApps.php", "GET", hash);
        } catch (Exception e) {
        }

        try {
            // Checking for SUCCESS TAG
            FileOutputStream outputStream;
            int success = json.getInt(TAG_SUCCESS);
            System.out.println(success);
            if (success == 1) {// products found
                JSONArray product = json.getJSONArray("AppList");
                StringBuilder list = new StringBuilder(product.length());
                for (int i = 0; i < product.length(); i++) {
                    String temp = product.get(i).toString() + "\r\n";
                    list.append(temp);
                }
                System.out.println("This are the list"+list);
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(list.toString().getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.stopSelf();
    }
}
