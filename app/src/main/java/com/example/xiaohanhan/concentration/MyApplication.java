package com.example.xiaohanhan.concentration;

import android.app.Application;

/**
 * Created by xiaohanhan on 2018/4/21.
 */

public class MyApplication extends Application {

    private boolean mIsActionMove;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public boolean isMove() {
        return mIsActionMove;
    }

    public void setMove(boolean move) {
        mIsActionMove = move;
    }
}