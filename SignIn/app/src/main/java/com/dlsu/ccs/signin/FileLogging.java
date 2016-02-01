package com.dlsu.ccs.signin;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Created by Lance on 1/13/2016.
 */
public class FileLogging extends IntentService {
    private static RecursiveFileObserver observer;
    String token;

    public FileLogging() {
        super(FileLogging.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.token = intent.getExtras().getString("token");
        implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getAbsolutePath());
        implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath());
        implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).getAbsolutePath());
        implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getAbsolutePath());
        implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getAbsolutePath());
        implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            implementLogging(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
        }

        this.stopSelf();
    }

    private void implementLogging(String pathToWatch) {
        RecursiveFileObserver observer = new RecursiveFileObserver(pathToWatch, this, token);
        observer.startWatching();
        //observer.stopWatching();
    }
}
