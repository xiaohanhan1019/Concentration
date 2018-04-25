package com.example.xiaohanhan.concentration.Model;

/**
 * Created by xiaohanhan on 2018/4/24.
 */

public class SubTask {

    private int mId;
    private int mTaskId;
    private String mSubTaskName;
    private boolean mIsFinish;

    public SubTask(int taskId){
        mId = (int)(Math.random() * 10000);
        mTaskId = taskId;
        mSubTaskName = "subTask" + mId;
        mIsFinish = false;
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

    public String getSubTaskName() {
        return mSubTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        mSubTaskName = subTaskName;
    }

    public boolean isFinish() {
        return mIsFinish;
    }

    public void setFinish(boolean finish) {
        mIsFinish = finish;
    }
}

