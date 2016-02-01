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
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Lance on 12/16/2015.
 */
public class BlockApps extends IntentService {
    private static final String filename = "Applist.txt";
    private String token;

    public BlockApps() {
        super(BlockApps.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        this.token = intent.getExtras().getString("token");
        AppsBlocking(readAllApps());
        this.stopSelf();
    }

    public String readAllApps() {
        try {
            FileInputStream inputStream = openFileInput(filename);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i;
            try {
                i = inputStream.read();
                while (i != -1) {
                    byteArrayOutputStream.write(i);
                    i = inputStream.read();
                }
                PackageManager pm = getPackageManager();
                List<PackageInfo> list = pm.getInstalledPackages(0);

                for (PackageInfo pi : list) {
                    ApplicationInfo ai = pm.getApplicationInfo(pi.packageName, 0);
                    if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        byteArrayOutputStream.write(pi.packageName.getBytes());
                        byteArrayOutputStream.write("\r\n".getBytes());
                    }
                }
                inputStream.close();
                return byteArrayOutputStream.toString();
            } catch (IOException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public void AppsBlocking(String appList) {
        if (appList != null) {
            ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
            PackageManager pm = getPackageManager();
            PackageInfo foregroundAppPackageInfo = null;
            try {
                foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
                String strArray[] = appList.split("\r\n");
                int flag = 0;
                for (int u = 0; u < strArray.length; u++) {
                    if (foregroundAppPackageInfo.packageName.matches(".*" + strArray[u] + ".*")) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    System.out.println("This Application is blocked:" + foregroundAppPackageInfo.packageName);
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(startMain);
                    activityManager.killBackgroundProcesses(foregroundAppPackageInfo.packageName);
                    showToast("This application is Blocked By SM BEYOND:");
                    logViolation(foregroundAppPackageInfo.packageName);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }


    public void logViolation(String appName) {
        BeyondSQLiteHelper db = new BeyondSQLiteHelper(this);
        AppViolationLog bean = new AppViolationLog();
        bean.setAppName(appName);
        bean.setUserToken(token);
        db.addAppViolationLog(bean);
       }
}
