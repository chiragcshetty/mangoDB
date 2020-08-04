package com.codetoart.android.qrcodescannerandroid;

import android.app.Application;
import android.content.Context;

public class MangoDB extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MangoDB.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MangoDB.context;
    }
}
