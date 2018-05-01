package com.example.xiaohanhan.concentration.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiaohanhan on 2018/4/18.
 */

public class Task {

    private int mId;
    private int mGroupId;
    private int mUserId;
    private String mTaskName;
    private Timestamp mStartDate;
    private Timestamp mDeadline;
    private Timestamp mFinishDate;
    private Timestamp mReminder;
    private int mPriority;
    private int mExpectedWorkingTime;   //in minute
    private double mWorkedTime;            //in minute
    private int mTimes;
    private String mDetail;
    private boolean mIsFinish;

    public static final String KEY_id = "id";
    public static final String KEY_task_group_id = "task_group_id";


    private List<SubTask> mSubTasks;

    public Task(int groupId){
        mGroupId = groupId;
        mId = (int)(Math.random() * 10000);
        mStartDate = new Timestamp(new Date().getTime());
        mReminder = null;
        mDeadline = null;
        mTaskName = Integer.toString(mId);
        mPriority = 0;
        mExpectedWorkingTime = 0;
        mWorkedTime = 0;
        mTimes = 0;
        mDetail = null;
        mIsFinish = false;
        mSubTasks = new ArrayList<>();
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
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

    public double getWorkedTime() {
        return mWorkedTime;
    }

    public void setWorkedTime(double workedTime) {
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

    public List<SubTask> getSubTasks() {
        return mSubTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        mSubTasks = subTasks;
    }

    public void addSubtask(SubTask subTask){
        mSubTasks.add(subTask);
    }

    public int getGroupId() {
        return mGroupId;
    }

}
