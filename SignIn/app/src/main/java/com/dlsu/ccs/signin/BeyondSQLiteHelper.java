package com.dlsu.ccs.signin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lance on 1/14/2016.
 */
public class BeyondSQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "LogDB";
    private static final String TABLE_APPVIOLATION = "AppViolationLog";
    private static final String KEY_TIMESTAMP =  "timestamp";
    private static final String KEY_APPNAME = "appName";
    private static final String KEY_USERTOKEN = "userToken";
    private static final String KEY_TYPE = "type";
    private static final String KEY_EVENT = "event" ;
    private static final String TABLE_APPRUNNINGLOG = "AppRunningLog" ;
    private static final String TABLE_APPINSTALLLOG = "AppInstallationLog" ;
    private static final String KEY_PATH = "filePath";
    private static final String TABLE_FILELOG = "FileLog" ;

    public BeyondSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        checkTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS AppViolationLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "appName TEXT," +
                "type TEXT)";
        db.execSQL(createTable);
        createTable = "CREATE TABLE IF NOT EXISTS AppRunningLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "appName TEXT," +
                "event TEXT," +
                "type TEXT)";
        db.execSQL(createTable);
        System.out.println("THE TABLE:"+createTable);
        createTable = "CREATE TABLE IF NOT EXISTS AppInstallationLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "appName TEXT," +
                "event TEXT," +
                "type TEXT)";
        db.execSQL(createTable);
    /*    createTable = "CREATE TABLE IF NOT EXISTS SMSLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "message TEXT," +
                "event TEXT," +
                "senderNumber," +
                "contactName," +
                "receiverNumber" +
                "type TEXT)";
        db.execSQL(createTable);
*/
        createTable = "CREATE TABLE IF NOT EXISTS FileLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "filePath TEXT," +
                "event TEXT," +
                "type TEXT)";
        db.execSQL(createTable);

    }

    public void checkTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String createTable = "CREATE TABLE IF NOT EXISTS AppViolationLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "appName TEXT," +
                "type TEXT)";
        db.execSQL(createTable);
        createTable = "CREATE TABLE IF NOT EXISTS AppRunningLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "appName TEXT," +
                "event TEXT," +
                "type TEXT)";
        db.execSQL(createTable);
        System.out.println("THE TABLE:"+createTable);
        createTable = "CREATE TABLE IF NOT EXISTS AppInstallationLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "appName TEXT," +
                "event TEXT," +
                "type TEXT)";
        db.execSQL(createTable);
    /*    createTable = "CREATE TABLE IF NOT EXISTS SMSLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "message TEXT," +
                "event TEXT," +
                "senderNumber," +
                "contactName," +
                "receiverNumber" +
                "type TEXT)";
        db.execSQL(createTable);
*/
        createTable = "CREATE TABLE IF NOT EXISTS FileLog ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, "+
                "userToken TEXT," +
                "filePath TEXT," +
                "event TEXT," +
                "type TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }







    public void addRunningAppLog(AppLog bean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMESTAMP, getCurrentDateTime());
        values.put(KEY_APPNAME,bean.getAppName());
        values.put(KEY_USERTOKEN, bean.getUserToken());
        values.put(KEY_EVENT, bean.getEvent());
        values.put(KEY_TYPE,"AppRunningLog");
        //insert
        db.insert(TABLE_APPRUNNINGLOG, // table
                null,
                values);
        db.close();
    }

    public ArrayList<AppLog> getAllAppRunningLog(){
        ArrayList<AppLog> beanList = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_APPRUNNINGLOG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        AppLog bean = null;
        if (cursor.moveToFirst()) {
            do {
                bean = new AppLog();
                bean.setId(Integer.parseInt(cursor.getString(0)));
                bean.setTimestamp(cursor.getString(1));
                bean.setUserToken(cursor.getString(2));
                bean.setAppName(cursor.getString(3));
                bean.setEvent(cursor.getString(4));
                bean.setType(cursor.getString(5));
                beanList.add(bean);
            } while (cursor.moveToNext());
        }
        return beanList;
    }

    public void deleteAllAppRunningLog(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_APPRUNNINGLOG);
        db.close();

    }

    public void addAppViolationLog(AppViolationLog bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMESTAMP, getCurrentDateTime());
        values.put(KEY_APPNAME,bean.getAppName());
        values.put(KEY_USERTOKEN, bean.getUserToken());
        values.put(KEY_TYPE,"AppViolationLog");
        //insert
        db.insert(TABLE_APPVIOLATION, // table
                null,
                values);
        db.close();
    }

    public ArrayList<AppViolationLog> getAllAppViolationLog() {
        ArrayList<AppViolationLog> beanList = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_APPVIOLATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        AppViolationLog bean = null;
        if (cursor.moveToFirst()) {
            do {
                bean = new AppViolationLog();
                bean.setId(Integer.parseInt(cursor.getString(0)));
                bean.setTimestamp(cursor.getString(1));
                bean.setUserToken(cursor.getString(2));
                bean.setAppName(cursor.getString(3));
                bean.setType(cursor.getString(4));
                beanList.add(bean);
            } while (cursor.moveToNext());
        }



        return beanList;
    }

    public void deleteAllAppViolationLog(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_APPVIOLATION);
        db.close();
    }

    public String getCurrentDateTime() {
        return  java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }

    public void addInstallAppLog(AppLog bean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMESTAMP, getCurrentDateTime());
        values.put(KEY_APPNAME,bean.getAppName());
        values.put(KEY_USERTOKEN, bean.getUserToken());
        values.put(KEY_EVENT, bean.getEvent());
        values.put(KEY_TYPE,"AppInstallationLog");
        //insert
        db.insert(TABLE_APPINSTALLLOG, // table
                null,
                values);
        db.close();
    }

    public ArrayList<AppLog> getAllAppInstallLog(){
        ArrayList<AppLog> beanList = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_APPINSTALLLOG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        AppLog bean = null;
        if (cursor.moveToFirst()) {
            do {
                bean = new AppLog();
                bean.setId(Integer.parseInt(cursor.getString(0)));
                bean.setTimestamp(cursor.getString(1));
                bean.setUserToken(cursor.getString(2));
                bean.setAppName(cursor.getString(3));
                bean.setEvent(cursor.getString(4));
                bean.setType(cursor.getString(5));
                beanList.add(bean);
            } while (cursor.moveToNext());
        }
        return beanList;
    }

    public void deleteAllInstallLog(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_APPINSTALLLOG);
        db.close();

    }

    public void addFileLog(FileLog bean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMESTAMP, getCurrentDateTime());
        values.put(KEY_PATH,bean.getPath());
        values.put(KEY_USERTOKEN, bean.getUserToken());
        values.put(KEY_EVENT, bean.getEvent());
        values.put(KEY_TYPE,"FileLog");
        //insert
        db.insert(TABLE_FILELOG, // table
                null,
                values);
        db.close();
    }

    public ArrayList<FileLog> getAllFileLog(){
        ArrayList<FileLog> beanList = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_FILELOG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        FileLog bean = null;
        if (cursor.moveToFirst()) {
            do {
                bean = new FileLog();
                bean.setId(Integer.parseInt(cursor.getString(0)));
                bean.setTimestamp(cursor.getString(1));
                bean.setUserToken(cursor.getString(2));
                bean.setPath(cursor.getString(3));
                bean.setEvent(cursor.getString(4));
                bean.setType(cursor.getString(5));
                beanList.add(bean);
            } while (cursor.moveToNext());
        }
        return beanList;
    }

    public void deleteAllFileLog(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FILELOG);
        db.close();

    }


}
