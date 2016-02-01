package com.dlsu.ccs.signin;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Jarrette on 1/13/2016.
 */
public class SMSObserver extends ContentObserver {

    private Context mContext;
    private String contactId = "", contactName = "";
    private String smsBodyStr = "", phoneNoStr = "";
    private int lastID;
    private long smsDatTime = System.currentTimeMillis();
    static final Uri SMS_STATUS_URI = Uri.parse("content://sms/sent");


    public SMSObserver(Handler handler, Context context) {
        super(handler);
        mContext = context;
        lastID = getLastMsgId();
    }

    public int getLastMsgId() {
        Cursor sent = mContext.getContentResolver().query(SMS_STATUS_URI, null, null, null, null);
        sent.moveToFirst();
        int last = sent.getInt(sent.getColumnIndex("_id"));
        return last;
    }

    @Override
    public void onChange(boolean selfChange) {
        int newID = getLastMsgId();
        try{
            Cursor sms_sent_cursor = mContext.getContentResolver().query(SMS_STATUS_URI, null, null, null, null);
            if (sms_sent_cursor != null) {
                if (sms_sent_cursor.moveToFirst()) {
                    if(lastID != newID){
                        String protocol = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("protocol"));
                        Log.e("Info","protocol : " + protocol);
                        //send protocol is null
                        if(protocol == null){
                            int type = sms_sent_cursor.getInt(sms_sent_cursor.getColumnIndex("type"));
                            Log.e("Info", "SMS Type : " + type);
                            // for sent sms, type = 2
                            //if(type == 2 && !lastID.contentEquals(sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("_id")))){
                            if(type == 2){
                                smsBodyStr = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("body")).trim();
                                phoneNoStr = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address")).trim();
                                smsDatTime = sms_sent_cursor.getLong(sms_sent_cursor.getColumnIndex("date"));
                                String timestamp = Long.toString(smsDatTime);

                                System.out.println("Sent SMS : " + phoneNoStr +" Name: "+ sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("person")) +
                                        " Message: " + smsBodyStr + " TimeStamp: " + timestamp + "Last ID: " + lastID + "New ID: " + newID);
                            }
                        }
                    //
                    }
                }
            }
            else
                Log.e("Info","Send Cursor is Empty");
        }
        catch(Exception e){

        }
        super.onChange(selfChange);
    }
}
