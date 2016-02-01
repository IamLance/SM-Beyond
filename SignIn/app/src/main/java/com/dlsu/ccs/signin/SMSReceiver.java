package com.dlsu.ccs.signin;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;

/**
 * Created by Lance on 1/12/2016.
 */
public class SMSReceiver extends BroadcastReceiver {
    @SuppressWarnings("deprecation")

    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        SmsMessage shortMessage= null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            shortMessage = SmsMessage.createFromPdu((byte[]) pdus[0], "3gpp");
        }
        else{
            shortMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
        }

        System.out.println("Text Message Received From: " + shortMessage.getOriginatingAddress() +" Name: "+ getContactName(context, shortMessage.getDisplayOriginatingAddress()) + " Message: " + shortMessage.getDisplayMessageBody()+ " TimeStamp: "+ shortMessage.getTimestampMillis());
    }


    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,
                new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }


}



