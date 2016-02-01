package com.dlsu.ccs.signin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Lance on 1/12/2016.
 */
public class PackageInstallReceiver extends BroadcastReceiver {
    Context context;
    SharedPreferences prefs = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        System.out.println("App Installed:" + intent.getDataString());
        logEvent(intent.getDataString(), "App Installed");
    }


    private void logEvent(String packageName, String event) {
        String token = null;
        prefs = context.getSharedPreferences("com.dlsu.ccs.signin", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        String status = prefs.getString("AppLoggingPolicy", "");
        System.out.println("Status is :"+status);
        if (status.equalsIgnoreCase("1")) {
            BeyondSQLiteHelper db = new BeyondSQLiteHelper(context);
            AppLog bean = new AppLog();
            bean.setAppName(packageName);
            bean.setUserToken(token);
            bean.setEvent(event);
            db.addInstallAppLog(bean);
            System.out.println("DEBUG 2: " + status);
        }
    }
}
