package com.example.xiaohanhan.concentration.Model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by xiaohanhan on 2018/5/3.
 */

public class ConcentrationRecord {

    private int mId;
    private int mTaskId;
    private Timestamp mStartTime;
    private int mWorkingtime;
    private boolean mIsInterrupted;

    private String mTaskName;

    public static final String KEY_id = "id";
    public static final String KEY_task_id = "task_id";
    public static final String KEY_start_time = "start_time";
    public static final String KEY_working_time = "working_time";
    public static final String KEY_is_interrupted = "is_interrupted";
    public static final String KEY_task_name = "task_name";

    public ConcentrationRecord(int taskId){
        mTaskId=taskId;
        mStartTime = new Timestamp(new Date().getTime());
    }

    public ConcentrationRecord(){

    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getTaskId() {
        return mTaskId;
    }

    public void setTaskId(int taskId) {
        mTaskId = taskId;
    }

    public Timestamp getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Timestamp starttime) {
        mStartTime = starttime;
    }

    public int getWorkingtime() {
        return mWorkingtime;
    }

    public void setWorkingtime(int workingtime) {
        mWorkingtime = workingtime;
    }

    public boolean isInterrupted() {
        return mIsInterrupted;
    }

    public void setInterrupted(boolean interrupted) {
        mIsInterrupted = interrupted;
    }

}
