package com.example.xiaohanhan.concentration.Model;

import java.sql.Timestamp;
import java.util.Date;

public class Interruption {

    private int mId;
    private String mInterruptionName;
    private int mTimes;

    public static final String KEY_id = "id";
    public static final String KEY_interruption_name = "interruption_name";
    public static final String KEY_times = "times";

    public Interruption() {

    }

    public Interruption(String interruptionName){
        mInterruptionName = interruptionName;
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
