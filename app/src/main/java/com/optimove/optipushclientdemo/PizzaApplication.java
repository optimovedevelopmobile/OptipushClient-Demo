package com.optimove.optipushclientdemo;

import android.app.Application;

public class PizzaApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        PiwikManager.configure(this);
    }
}
