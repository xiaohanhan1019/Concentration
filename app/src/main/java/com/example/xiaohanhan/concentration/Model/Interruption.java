package com.example.xiaohanhan.concentration.Model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by xiaohanhan on 2018/4/28.
 */

public class Interruption {

    private int mId;
    private String mInterruptionName;
    private int mTimes;
    private Timestamp mInterruptionTime;

    public Interruption() {
        mId = (int)(Math.random() * 10000);
        mTimes = 0;
        mInterruptionName = Integer.toString(mId);
        mInterruptionTime = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getInterruptionName() {
        return mInterruptionName;
    }

    public void setInterruptionName(String interruptionName) {
        mInterruptionName = interruptionName;
    }

    public int getTimes() {
        return mTimes;
    }

    public void setTimes(int times) {
        mTimes = times;
    }
}
