package com.example.xiaohanhan.concentration.Model;

public class SubTask {

    private int mId;
    private int mTaskId;
    private String mSubTaskName;
    private boolean mIsFinish;

    public static final String KEY_id = "id";
    public static final String KEY_task_id = "task_id";
    public static final String KEY_subtask_name = "subtask_name";
    public static final String KEY_is_finish = "is_finish";

    public SubTask(){

    }

    public SubTask(int taskId,String subTaskName){
        mTaskId = taskId;
        mSubTaskName = subTaskName;
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

