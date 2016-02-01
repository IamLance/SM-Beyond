package com.dlsu.ccs.signin;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Jarrette on 1/13/2016.
 */
public class SentSMS extends Service {

    static final Uri SMS_STATUS_URI = Uri.parse("content://sms");

    @Override
    public void onCreate(){
        SMSObserver smsSentObserver = new SMSObserver(new Handler(), this);
        getContentResolver().registerContentObserver(SMS_STATUS_URI, true, smsSentObserver);
        System.out.println("Sent SMS starting...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
