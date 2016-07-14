package com.peterlzhou.pokedex;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by peterlzhou on 7/13/16.
 */
public class MyApplication extends Application {
    //Use this to enable multidex for building an APK
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
