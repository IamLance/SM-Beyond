package com.dlsu.ccs.signin;

import android.app.IntentService;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.CompoundButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jarrette on 12/11/2015.
 */

public class LoadPolicyList extends IntentService {
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_POLICIES = "policylist";
    private static final String TAG_STATUS = "Policy_Status";
    private static final String TAG_NAME = "Policy_Name";
    private static final String TAG_PID = "Policy_ID";
    private static final String TAG_VALUE = "Policy_Value";
    private ArrayList<Policy> Policies = new ArrayList<>();
    DevicePolicyManager MDMDevicePolicyManager;
    ComponentName MDMDevicePolicyAdmin;
    public LoadPolicyList() {
        super(LoadPolicyList.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MDMDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        MDMDevicePolicyAdmin = new ComponentName(this, DeviceAdminBean.DevicePolicyReceiver.class);
        HashMap hash = new HashMap();
        JSONParser jParser = new JSONParser();
        JSONObject json = new JSONObject();
        boolean block = false;
        final String token = intent.getExtras().getString("token");
        int resultCode = intent.getExtras().getInt("resultCode");
        int requestCode = intent.getExtras().getInt("requestCode");
        hash.put("userToken", token);

        try {
            json = jParser.makeHttpRequest("http://192.168.0.201/phpprojects/SendMobilePolicies.php", "GET", hash);
                /* Sending result back to activity */
            if (json.length() > 0) {
                //bundle.putStringArray("Success", result);
                //receiver.send(STATUS_FINISHED, bundle);
            }
        } catch (Exception e) {
                /* Sending error message back to activity */
            //bundle.putString(Intent.EXTRA_TEXT, e.toString());
            //receiver.send(STATUS_ERROR, bundle);
        }
        //}

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
                    p.setPolicyValue(c.getString(TAG_VALUE));
                    System.out.print(p.getPolicyName());
                    System.out.print(p.getPolicyId());
                    Policies.add(p);
                    System.out.println(Policies.size());

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (resultCode == -1) {
            switch (requestCode) {
                case 1:
                    System.out.println("Enabling Policies Now");
                    System.out.println("Camera status=" + Policies.get(5).getPolicyStatus());
                    System.out.println("Camera ID=" + Policies.get(5).getPolicyId());
                    System.out.println("Camera IF1=" + Policies.get(5).getPolicyStatus().equals("1"));

                    if (Policies.get(1).getPolicyStatus().equals("1") && Policies.get(1).getPolicyId().equals("2")) //PASSWORD LENGTH
                    {
                        MDMDevicePolicyManager.setPasswordMinimumLength(MDMDevicePolicyAdmin, Integer.parseInt(Policies.get(1).getPolicyValue()));
                    }
                    if (Policies.get(1).getPolicyStatus().equals("0") && Policies.get(1).getPolicyId().equals("2")) {
                        MDMDevicePolicyManager.setPasswordMinimumLength(MDMDevicePolicyAdmin, 0);
                    }
                    if (Policies.get(2).getPolicyStatus().equals("1") && Policies.get(2).getPolicyId().equals("3")) //ALPHA NUMERIC PASSWORD REQURIRED
                    {
                        MDMDevicePolicyManager.setPasswordQuality(MDMDevicePolicyAdmin, DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC);
                    }
                    if (Policies.get(2).getPolicyStatus().equals("0") && Policies.get(2).getPolicyId().equals("3")) {
                        MDMDevicePolicyManager.setPasswordMinimumNumeric(MDMDevicePolicyAdmin, 0);
                        MDMDevicePolicyManager.setPasswordMinimumLetters(MDMDevicePolicyAdmin, 0);

                    }
                    if (Policies.get(3).getPolicyStatus().equals("1") && Policies.get(3).getPolicyId().equals("4")) {
                        MDMDevicePolicyManager.setPasswordQuality(MDMDevicePolicyAdmin, DevicePolicyManager.PASSWORD_QUALITY_COMPLEX);
                    }
                    if (Policies.get(3).getPolicyStatus().equals("0") && Policies.get(3).getPolicyId().equals("4")) {
                        MDMDevicePolicyManager.setPasswordMinimumSymbols(MDMDevicePolicyAdmin, 0);
                        MDMDevicePolicyManager.setPasswordMinimumLetters(MDMDevicePolicyAdmin, 0);
                        MDMDevicePolicyManager.setPasswordMinimumNumeric(MDMDevicePolicyAdmin, 0);
                    }
                    if (Policies.get(4).getPolicyStatus().equals("1") && Policies.get(4).getPolicyId().equals("5")) {
                        MDMDevicePolicyManager.setMaximumFailedPasswordsForWipe(MDMDevicePolicyAdmin, Integer.parseInt(Policies.get(4).getPolicyValue()));
                    }
                    if (Policies.get(4).getPolicyStatus().equals("0") && Policies.get(4).getPolicyId().equals("5")) {
                        MDMDevicePolicyManager.setMaximumFailedPasswordsForWipe(MDMDevicePolicyAdmin, 0);
                    }
                    if (Policies.get(5).getPolicyStatus().equals("1") && Policies.get(5).getPolicyId().equals("6")) {
                        MDMDevicePolicyManager.setCameraDisabled(MDMDevicePolicyAdmin, true);
                        System.out.println("Camera is now disabled");
                    }
                    if (Policies.get(5).getPolicyStatus().equals("0") && Policies.get(5).getPolicyId().equals("6")) {
                        MDMDevicePolicyManager.setCameraDisabled(MDMDevicePolicyAdmin, false);
                        System.out.println("Camera is now enabled");
                    }
                    if (Policies.get(6).getPolicyStatus().equals("1") && Policies.get(6).getPolicyId().equals("7")) {
                        MDMDevicePolicyManager.setMaximumTimeToLock(MDMDevicePolicyAdmin, Long.parseLong(Policies.get(6).getPolicyValue()));
                    }
                    if (Policies.get(6).getPolicyStatus().equals("0") && Policies.get(6).getPolicyId().equals("7")) {
                        MDMDevicePolicyManager.setMaximumTimeToLock(MDMDevicePolicyAdmin, 0);
                    }
                    if (Policies.get(7).getPolicyStatus().equals("1") && Policies.get(7).getPolicyId().equals("8")) {
                        block = true;
                    }
                    if (Policies.get(7).getPolicyStatus().equals("0") && Policies.get(7).getPolicyId().equals("8")) {
                        block = false;
                    }

                    boolean isSufficient = MDMDevicePolicyManager.isActivePasswordSufficient();

                    Intent i = new Intent("Policy");
                    // You can also include some extra data.
                    i.putExtra("password", isSufficient);
                    i.putExtra("block", block);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(i);

                    break;
                case 3:
                    MDMDevicePolicyManager.removeActiveAdmin(MDMDevicePolicyAdmin);
                    Intent dc = new Intent("Disconnect");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(dc);
                    break;
            }
        }
        this.stopSelf();
    }
}
