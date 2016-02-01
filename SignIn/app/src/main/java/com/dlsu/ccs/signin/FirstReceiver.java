package com.dlsu.ccs.signin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Jarrette on 1/5/2016.
 */
public class FirstReceiver extends BroadcastReceiver {
    // triggers every 5 seconds

    private Integer resCode;
    private Integer reqCode;
    private String token;

    @Override
    public void onReceive(Context context, Intent intent) {
        resCode = intent.getExtras().getInt("resultCode");
        reqCode = intent.getExtras().getInt("requestCode");
        token = intent.getExtras().getString("token");
        intent = new Intent(context, BlockApps.class);
        intent.putExtra("resultCode", resCode);
        intent.putExtra("requestCode", reqCode);
        intent.putExtra("token", token);
        context.startService(intent);

        intent = new Intent(context, AppLogging.class);
        intent.putExtra("resultCode", resCode);
        intent.putExtra("requestCode", reqCode);
        intent.putExtra("token", token);
        context.startService(intent);



        intent = new Intent(context, FileLogging.class);
        intent.putExtra("resultCode", resCode);
        intent.putExtra("requestCode", reqCode);
        intent.putExtra("token", token);
        context.startService(intent);
        System.out.println("Pokemon1");



    }

}
