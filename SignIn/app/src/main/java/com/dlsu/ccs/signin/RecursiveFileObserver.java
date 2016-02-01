package com.dlsu.ccs.signin;

import android.content.Context;
import android.os.FileObserver;
import android.os.Handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static android.os.FileObserver.CREATE;

/**
 * Created by Lance on 1/13/2016.
 */
public class RecursiveFileObserver extends FileObserver {

    public static int CHANGES_ONLY = CLOSE_WRITE | MOVE_SELF | MOVED_FROM;

    List<SingleFileObserver> mObservers;
    String mPath;
    int mMask;
    Context context;
    String token;
    public RecursiveFileObserver(String path, Context context, String token) {
        this(path, ALL_EVENTS, context, token);
    }

    public RecursiveFileObserver(String path, int mask,Context context, String token) {
        super(path, mask);
        mPath = path;
        mMask = mask;
        this.token = token;
        this.context = context;
    }

    @Override
    public void startWatching() {
        System.out.println("Now Observing....");
        if (mObservers != null) return;
        mObservers = new ArrayList<SingleFileObserver>();
        Stack<String> stack = new Stack<String>();
        stack.push(mPath);

        while (!stack.empty()) {
            String parent = stack.pop();
            mObservers.add(new SingleFileObserver(parent, mMask));
            File path = new File(parent);
            File[] files = path.listFiles();
            if (files == null) continue;
            for (int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory() && !files[i].getName().equals(".")
                        && !files[i].getName().equals("..")) {
                    stack.push(files[i].getPath());
                }
            }
        }
        for (int i = 0; i < mObservers.size(); i++)
            mObservers.get(i).startWatching();
    }

    @Override
    public void stopWatching() {
        if (mObservers == null) return;

        for (int i = 0; i < mObservers.size(); ++i)
            mObservers.get(i).stopWatching();

        mObservers.clear();
        mObservers = null;
    }

    @Override
    public void onEvent(int event, String path) {
        if (path == null) {
            return;
        }
        event &= FileObserver.ALL_EVENTS;
        switch (event) {
            case FileObserver.CREATE:
                logEvent(path,"File Created");
                System.out.println("CREATE   " + path);
                break;
            case FileObserver.DELETE:
                logEvent(path,"File Deleted");
                System.out.println("DELETE   " + path);
                break;
            case FileObserver.MODIFY:
                logEvent(path,"File Modified");
                System.out.println("MODIFY   " + path);
                break;
            case FileObserver.ATTRIB:
                logEvent(path,"File Metadata was changed explicitly");
                System.out.println("METADATA WAS EDITED   " + path);
                break;
            default:
                break;
        }



    }

    private void logEvent(String filePath, String event) {
        BeyondSQLiteHelper db = new BeyondSQLiteHelper(this.context);
        FileLog bean = new FileLog();
        bean.setPath(filePath);
        bean.setUserToken(token);
        bean.setEvent(event);
        db.addFileLog(bean);
    }

    private class SingleFileObserver extends FileObserver {
        private String mPath;

        public SingleFileObserver(String path, int mask) {
            super(path, mask);
            mPath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            String newPath = mPath + "/" + path;
            RecursiveFileObserver.this.onEvent(event, newPath);
        }

    }
}