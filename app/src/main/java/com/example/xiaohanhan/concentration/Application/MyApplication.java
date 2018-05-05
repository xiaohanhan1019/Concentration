package com.example.xiaohanhan.concentration.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by xiaohanhan on 2018/4/21.
 */

public class MyApplication extends Application {

    public static final String PREFERENCE_SETTINGS_WORKING_TIME = "working_time";
    public static final String PREFERENCE_SETTINGS_SORT_KEY = "sort_key";

    @Override
    public void onCreate() {
        super.onCreate();
    }

}