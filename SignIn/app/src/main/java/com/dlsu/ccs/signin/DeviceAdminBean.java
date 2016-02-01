package com.dlsu.ccs.signin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.app.AlarmManager.*;

/**
 * Created by Lance on 12/7/2015.
 */
public class DeviceAdminBean extends Activity {
    private CheckBox MDMAdminEnabledCheckbox;
    DevicePolicyManager MDMDevicePolicyManager;
    ComponentName MDMDevicePolicyAdmin;
    CountDown time1 = new CountDown(15000,1000,15);
    CountDown time2 = new CountDown(5000,1000,5);
    CountDown time3 = new CountDown(30000,1000,30);
    CountDown time4 = new CountDown(15000,1000,15);
    CountDown time5 = new CountDown(15000,1000,15);
    private boolean pass = false;
    private boolean close = false;
    private boolean block = false;
    private int resCode = 0;
    private int reqCode;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_policy_admin);
        MDMDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        MDMDevicePolicyAdmin = new ComponentName(this, DevicePolicyReceiver.class);
        MDMAdminEnabledCheckbox = (CheckBox) findViewById(R.id.checkBox1);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("Policy"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mDisconnectReceiver, new IntentFilter("Disconnect"));
        token = getIntent().getExtras().getString("token");

        Button button7 = (Button) findViewById(R.id.button7); // logout
        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DeviceAdminBean.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logging Out")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(DeviceAdminBean.this, LoadPolicyList.class);
                                intent.putExtra("resultCode", -1);
                                intent.putExtra("requestCode", 3);
                                DeviceAdminBean.this.startService(intent);
                                intent = new Intent(DeviceAdminBean.this, MainActivity.class);
                                DeviceAdminBean.this.startActivity(intent);
                                close = true;
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox1.isChecked()) {
                    deleteAlarm();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logging Out")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(DeviceAdminBean.this, LoadPolicyList.class);
                        intent.putExtra("resultCode", -1);
                        intent.putExtra("requestCode", 3);
                        DeviceAdminBean.this.startService(intent);
                        intent = new Intent(DeviceAdminBean.this, MainActivity.class);
                        DeviceAdminBean.this.startActivity(intent);
                        close = true;
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            pass = intent.getBooleanExtra("password", true);
            block = intent.getBooleanExtra("block", false);
            System.out.println("Checking pass now.");
            if (pass) {
                System.out.println("Password sufficient.");
                onResume();
            } else {
                System.out.println("Password not sufficient.");
                Intent setPasswordIntent = new Intent(
                        DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                startActivityForResult(setPasswordIntent, 2);
                MDMDevicePolicyManager.setPasswordExpirationTimeout(
                        MDMDevicePolicyAdmin, 0L);
            }
        }
    };

    private BroadcastReceiver mDisconnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            deleteAlarm();
        }
    };

    public void deleteAlarm() {
/*
        Intent cancelAlarm = new Intent(DeviceAdminBean.this, SecondReceiver.class);
        cancelAlarm.putExtra("resultCode", resCode);
        cancelAlarm.putExtra("requestCode", reqCode);
        cancelAlarm.putExtra("token", token);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pIntent = PendingIntent.getBroadcast(DeviceAdminBean.this, 0, cancelAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pIntent);
        System.out.println("Alarm Deleted.");

        Intent appBlockAlarm = new Intent(DeviceAdminBean.this, FirstReceiver.class);
        appBlockAlarm.putExtra("resultCode", resCode);
        appBlockAlarm.putExtra("requestCode", reqCode);
        appBlockAlarm.putExtra("token", token);
        AlarmManager appAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent blockAppIntent = PendingIntent.getBroadcast(DeviceAdminBean.this, 0, appBlockAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        appAlarm.cancel(blockAppIntent);
        System.out.println("Alarm Deleted");
*/
        time1.cancel();
        time2.cancel();
        time3.cancel();
        time4.cancel();
        time5.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isMyDevicePolicyReceiverActive()) {
            MDMAdminEnabledCheckbox.setChecked(true);
        } else {
            MDMAdminEnabledCheckbox.setChecked(false);
        }
        MDMAdminEnabledCheckbox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            Intent intent = new Intent(
                                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(
                                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                    MDMDevicePolicyAdmin);
                            intent.putExtra(
                                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                    getString(R.string.admin_explanation));

                            startActivityForResult(intent, 1);
                        } else {
                            MDMDevicePolicyManager
                                    .removeActiveAdmin(MDMDevicePolicyAdmin);
                            resCode = 0;
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Request Code: " + requestCode);
        System.out.println("Result Code: " + resultCode);

        resCode = resultCode;
        reqCode = requestCode;
        token = getIntent().getExtras().getString("token");

        if (!close && resultCode == -1) {
            time1.onFinish();
            time2.onFinish();
            time3.onFinish();
            time4.onFinish();
            Intent sms = new Intent(this, SentSMS.class);
            startService(sms);

        }
    }

    private void timedAlarm(int count){
        if(count == 15) { // LoadPolicyList
            Intent intentAlarm = new Intent(this, SecondReceiver.class);
            intentAlarm.putExtra("resultCode", -1);
            intentAlarm.putExtra("requestCode", 1);
            intentAlarm.putExtra("token", token);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
            alarm.set(RTC_WAKEUP, System.currentTimeMillis(), pIntent);
            System.out.println("Alarm Set for Policies");
            time1.start();

            //send logs
            intentAlarm = new Intent(this, SecondReceiver.class);
            intentAlarm.putExtra("resultCode", -1);
            intentAlarm.putExtra("requestCode", 1);
            intentAlarm.putExtra("token", token);
            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            pIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
            alarm.set(RTC_WAKEUP, System.currentTimeMillis(), pIntent);
            System.out.println("Alarm Set for Send Logs");
            time4.start();

            // log receiver
            /*
            intentAlarm = new Intent(this, SecondReceiver.class);
            intentAlarm.putExtra("resultCode", -1);
            intentAlarm.putExtra("requestCode", 1);
            intentAlarm.putExtra("token", token);
            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            pIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
            alarm.set(RTC_WAKEUP, System.currentTimeMillis(), pIntent);
            System.out.println("Alarm Set for Logging Receiver");
            time5.start();
            */

        } else if (count == 5) { // BlockApps
            if(!block) {
                Intent appBlockAlarm = new Intent(this, FirstReceiver.class);
                appBlockAlarm.putExtra("resultCode", -1);
                appBlockAlarm.putExtra("requestCode", 1);
                appBlockAlarm.putExtra("token", token);
                AlarmManager appAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent blockAppIntent = PendingIntent.getBroadcast(this, 0, appBlockAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
                appAlarm.set(RTC_WAKEUP, System.currentTimeMillis(), blockAppIntent);
                System.out.println("Alarm Set for Blocked Apps");
                time2.start();
            }
        } else if (count == 30){ // LoadAppsAllowed
            Intent appAllowAlarm = new Intent(this, ThirdReceiver.class);
            appAllowAlarm.putExtra("resultCode", -1);
            appAllowAlarm.putExtra("requestCode", 1);
            appAllowAlarm.putExtra("token", token);
            AlarmManager allowAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent allowAppIntent = PendingIntent.getBroadcast(this, 0, appAllowAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
            allowAlarm.set(RTC_WAKEUP, System.currentTimeMillis(), allowAppIntent);
            System.out.println("Alarm Set for Allowed Apps");
            time3.start();
        }
    }

    private boolean isMyDevicePolicyReceiverActive() {
        return MDMDevicePolicyManager
                .isAdminActive(MDMDevicePolicyAdmin);
    }

    public static class DevicePolicyReceiver extends DeviceAdminReceiver {
        @Override
        public void onDisabled(Context context, Intent intent) {
            Toast.makeText(context, "BEYOND's Device Admin Disabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnabled(Context context, Intent intent) {
            Toast.makeText(context, "BEYOND's Device Admin is now enabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            CharSequence disableRequestedSeq = "Requesting to disable BEYOND Device Admin";
            return disableRequestedSeq;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPasswordChanged(Context context, Intent intent) {
            Toast.makeText(context, "Device password is now changed",
                    Toast.LENGTH_SHORT).show();
            DevicePolicyManager localDPM = (DevicePolicyManager) context
                    .getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName localComponent = new ComponentName(context,
                    DevicePolicyReceiver.class);
            localDPM.setPasswordExpirationTimeout(localComponent, 360L * 86400L * 1000L);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override

        public void onPasswordExpiring(Context context, Intent intent) {
            // This would require API 11 an above
            Toast.makeText(
                    context,
                    "BEYOND's Device password is going to expire, please change to a new password",
                    Toast.LENGTH_LONG).show();

            DevicePolicyManager localDPM = (DevicePolicyManager) context
                    .getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName localComponent = new ComponentName(context,
                    DevicePolicyReceiver.class);
            long expr = localDPM.getPasswordExpiration(localComponent);
            long delta = expr - System.currentTimeMillis();
            boolean expired = delta < 0L;
            if (expired) {
                localDPM.setPasswordExpirationTimeout(localComponent, 10000L);
                Intent passwordChangeIntent = new Intent(
                        DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                passwordChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(passwordChangeIntent);
            }
        }

        @Override
        public void onPasswordFailed(Context context, Intent intent) {
            Toast.makeText(context, "Password failed", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onPasswordSucceeded(Context context, Intent intent) {
            Toast.makeText(context, "Access Granted", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.print("DevicePolicyReceiver Received: " + intent.getAction());
            super.onReceive(context, intent);
        }
    }

    public class CountDown extends CountDownTimer {
        int count;

        public CountDown(long millisInFuture, long countDownInterval, int n) {
            super(millisInFuture, countDownInterval);
            count = n;
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            timedAlarm(count);
        }
    }
}



