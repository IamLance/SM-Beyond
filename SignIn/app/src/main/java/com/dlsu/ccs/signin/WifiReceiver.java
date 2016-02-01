package com.dlsu.ccs.signin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Jarrette on 12/27/2015.
 */
public class WifiReceiver extends BroadcastReceiver {

    public WifiReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Toast.makeText(context, "Wifi connected.", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Intent disconnect = new Intent(context, LoadPolicyList.class);
                disconnect.putExtra("resultCode", -1);
                disconnect.putExtra("requestCode", 3);
                context.startService(disconnect);
            }
            catch (Exception e){
                System.out.println(e);
            }

            Toast.makeText(context, "Wifi disconnected.", Toast.LENGTH_SHORT).show();
        }
    }
}
