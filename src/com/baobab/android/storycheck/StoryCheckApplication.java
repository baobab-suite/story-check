package com.baobab.android.storycheck;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 2013/01/14
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryCheckApplication extends Application  {

    private static final String TAG = "StoryCheckApp";
    private BroadcastReceiver receiver;
    private LocalBroadcastManager broadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (receiver != null) {
            broadcastManager.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public void broadcastError(String code, int resourceId) {
        broadcastManager.sendBroadcast(new Intent("error").putExtra("code", code).putExtra("resourceId", resourceId));
    }

    public LocalBroadcastManager getBroadcastManager() {
        return broadcastManager;
    }

}
