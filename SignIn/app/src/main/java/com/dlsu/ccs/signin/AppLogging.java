package com.dlsu.ccs.signin;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lance on 12/30/2015.
 */
@SuppressWarnings("ALL")
public class AppLogging extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private String token;
    private static final String filename2 = "runningApps.txt";
    private FileOutputStream outputStream;


    public AppLogging() {
        super(AppLogging.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.token = intent.getExtras().getString("token");
        try {
            startAppRunnedLogging(readPreviousRunApps());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stopSelf();
    }

    private String readPreviousRunApps() {
        int i;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput(filename2);
            try {
                i = inputStream.read();
                while (i != -1) {
                    byteArrayOutputStream.write(i);
                    i = inputStream.read();
                }
                return byteArrayOutputStream.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startAppRunnedLogging(String prevRunList) throws IOException {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
        String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
        PackageManager pm = getPackageManager();
        PackageInfo foregroundAppPackageInfo = null;
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String tempString = readPreviousRunApps();
        if (prevRunList != null) {
            outputStream = openFileOutput(filename2, Context.MODE_PRIVATE);
            try {
                foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
                    if (!prevRunList.equals(foregroundAppPackageInfo.packageName)) {
                        System.out.println(foregroundAppPackageInfo.packageName + " has been launched");
                        outputStream.write((foregroundAppPackageInfo.packageName).getBytes());
                        logEvent(foregroundAppPackageInfo.packageName,"App Opened");
                        outputStream.close();
                    } else {
                        outputStream.write((foregroundAppPackageInfo.packageName).getBytes());
                        outputStream.close();
                    }
            } catch (Exception e) {
                if(!tempString.equals("none")) {
                    logEvent(tempString,"App Closed");
                }
                outputStream.write(("none").getBytes());
                outputStream.close();
                e.printStackTrace();
            }
        } else {
            RunApp();
        }
    }

    private void RunApp() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
        String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
        PackageManager pm = getPackageManager();
        PackageInfo foregroundAppPackageInfo = null;
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        try {
            outputStream = openFileOutput(filename2, Context.MODE_PRIVATE);
            foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
            System.out.println(" Initial launch:" + foregroundAppPackageInfo.packageName + " has been launched");
            outputStream.write((foregroundAppPackageInfo.packageName).getBytes());
            logEvent(foregroundAppPackageInfo.packageName,"App Opened");
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logEvent(String packageName, String event) {
        BeyondSQLiteHelper db = new BeyondSQLiteHelper(this);
        AppLog bean = new AppLog();
        bean.setAppName(packageName);
        bean.setUserToken(token);
        bean.setEvent(event);
        db.addRunningAppLog(bean);
    }
}
