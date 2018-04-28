package com.example.xiaohanhan.concentration;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by xiaohanhan on 2018/4/21.
 */

public class MyApplication extends Application {

    public static final String PREFERENCE_SETTINGS_WORKING_TIME = "working_time";
    private static final int mDefaultWorkingTime = 100;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences mUserSettings = getSharedPreferences("Concentration_setting", Context.MODE_PRIVATE);
        mUserSettings.edit().putInt(MyApplication.PREFERENCE_SETTINGS_WORKING_TIME,mDefaultWorkingTime).apply();

    }

}