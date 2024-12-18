package com.mct.localization.demo;

import android.app.Application;
import android.content.Context;

import com.mct.localization.Localization;

public class App extends Application {

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Localization.attachBaseContext(this, base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
