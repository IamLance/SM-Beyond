package com.dlsu.ccs.signin;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lance on 12/30/2015.
 */
public class LoadMonitoringPolicies extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private ArrayList<Policy> Policies = new ArrayList<>();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_POLICIES = "policylist";
    private static final String TAG_STATUS = "Policy_Status";
    private static final String TAG_NAME = "Policy_Name";
    private static final String TAG_PID = "Policy_ID";
    private static final String filename = "MonitoringPolicyList.txt";


    public LoadMonitoringPolicies() {
        super(LoadMonitoringPolicies.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HashMap hash = new HashMap();
        JSONParser jParser = new JSONParser();
        JSONObject json = new JSONObject();
        String result[] = null;
        final String token = intent.getExtras().getString("token");
        hash.put("userToken", token);
        System.out.println("Here is your token: "+ token);
        try {
            json = jParser.makeHttpRequest("http://192.168.0.201/phpprojects/SendMonitoringPolicies.php", "GET", hash);
                /* Sending result back to activity */
            if (json.length() > 0) {
                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);
                    System.out.println(success);
                    if (success == 1) {// products found
                        // Getting Array of Policylist
                        JSONArray product = json.getJSONArray(TAG_POLICIES);
                        // looping through All policies
                        for (int i = 0; i < product.length(); i++) {
                            Policy p = new Policy();
                            JSONObject c = product.getJSONObject(i);
                            p.setPolicyId(c.getString(TAG_PID));
                            p.setPolicyName(c.getString(TAG_NAME));
                            p.setPolicyStatus(c.getString(TAG_STATUS));
                            System.out.print(p.getPolicyName());
                            System.out.print(p.getPolicyId());
                            Policies.add(p);
                            System.out.println(Policies.size());
                        }

                        InitializeLogging();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
                /* Sending error message back to activity */
            //bundle.putString(Intent.EXTRA_TEXT, e.toString());
            //receiver.send(STATUS_ERROR, bundle);
        }


        this.stopSelf();
    }
    SharedPreferences prefs = null;
    private void InitializeLogging() {
            System.out.println("SMS LOGGING ENABLED");
            prefs = this.getSharedPreferences("com.dlsu.ccs.signin", Context.MODE_PRIVATE);
            prefs.edit().putString("SMSLoggingPolicy", Policies.get(0).getPolicyStatus()).apply();
            System.out.println("APP LOGGING ENABLED");
            prefs = this.getSharedPreferences("com.dlsu.ccs.signin", Context.MODE_PRIVATE);
            prefs.edit().putString("AppLoggingPolicy", Policies.get(1).getPolicyStatus()).apply();
    }





}
