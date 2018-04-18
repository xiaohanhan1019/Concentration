package com.example.xiaohanhan.concentration.Model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by xiaohanhan on 2018/4/18.
 */

public class Task {

    private int mId;
    private int mUserId;
    private String mTaskName;
    private Timestamp mStartDate;
    private Timestamp mDeadline;
    private Timestamp mFinishDate;
    private Timestamp mReminder;
    private int mPriority;
    private int mExpectedWorkingTime;
    private int mWorkedTime;
    private int mTimes;
    private String mDetail;
    private boolean mIsFinish;

    public Task(){
        mId = (int)(Math.random() * 10000);
        mStartDate = new Timestamp(new Date().getTime());
        mDeadline = new Timestamp(new Date().getTime());
        mTaskName = Integer.toString(mId);
        mPriority = (int)(Math.random() * 4);
        mExpectedWorkingTime = (int)(Math.random() * 200);
        mWorkedTime = 0;
        mTimes = 0;
        mDetail = "";
        mIsFinish = false;
//        mSubTasks = new ArrayList<>();
//        for(int i=0;i<3;i++){
//            SubTask subTask = new SubTask(mId);
//            mSubTasks.add(subTask);
//        }
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    public Timestamp getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Timestamp startDate) {
        mStartDate = startDate;
    }

    public Timestamp getDeadline() {
        return mDeadline;
    }

    public void setDeadLine(Timestamp Deadline) {
        mDeadline = Deadline;
    }

    public Timestamp getFinishTime() {
        return mFinishDate;
    }

    public void setFinishTime(Timestamp finishTime) {
        mFinishDate = finishTime;
    }

    public Timestamp getReminder() {
        return mReminder;
    }

    public void setReminder(Timestamp reminder) {
        mReminder = reminder;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public int getExpectedWorkingTime() {
        return mExpectedWorkingTime;
    }

    public void setExpectedWorkingTime(int expectedWorkingTime) {
        mExpectedWorkingTime = expectedWorkingTime;
    }

    public int getWorkedTime() {
        return mWorkedTime;
    }

    public void setWorkedTime(int workedTime) {
        mWorkedTime = workedTime;
    }

    public int getTimes() {
        return mTimes;
    }

    public void setTimes(int times) {
        mTimes = times;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public boolean isFinish() {
        return mIsFinish;
    }

    public void setFinish(boolean finish) {
        mIsFinish = finish;
    }

}
