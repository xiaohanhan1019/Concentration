package com.example.xiaohanhan.concentration.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Task {

    private int mId;
    private int mGroupId;
    private String mTaskName;
    private Timestamp mStartDate;
    private Timestamp mDeadline;
    private Timestamp mFinishDate;
    private Timestamp mReminder;
    private int mPriority;
    private int mExpectedWorkingTime;   //in minute
    private int mWorkedTime;            //in second
    private int mWorkingTimes;
    private String mDetail;
    private boolean mIsFinish;
    private List<SubTask> mSubTasks;

    public static final String KEY_id = "id";
    public static final String KEY_task_group_id = "task_group_id";
    public static final String KEY_task_name = "task_name";
    public static final String KEY_start_time = "start_time";
    public static final String KEY_expect_time = "expect_time";
    public static final String KEY_finish_time = "finish_time";
    public static final String KEY_reminder = "reminder";
    public static final String KEY_priority = "priority";
    public static final String KEY_expected_working_time = "expect_working_time";
    public static final String KEY_worked_time = "worked_time";
    public static final String KEY_working_times = "working_times";
    public static final String KEY_detail = "detail";
    public static final String KEY_is_finish = "is_finish";

    public static final String SORT_by_worked_time = "sort_by_worked_time";
    public static final String SORT_by_priority = "sort_by_priority";
    public static final String SORT_by_name = "sort_by_name";
    public static final String SORT_by_deadline = "sort_by_deadline";


    public Task(){

    }

    public Task(int groupId,String taskName){
        mGroupId = groupId;
        mTaskName = taskName;
        mStartDate = new Timestamp(new Date().getTime());
        mSubTasks = new ArrayList<>();
        mExpectedWorkingTime = 100;
    }

    public void addSubtask(SubTask subtask){
        mSubTasks.add(subtask);
    }

    public void deleteSubtask(int subtaskId){
        for(int i=0;i<mSubTasks.size();i++){
            if(mSubTasks.get(i).getId()==subtaskId) {
                mSubTasks.remove(i);
                break;
            }
        }
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getGroupId() {
        return mGroupId;
    }

    public void setGroupId(int groupId) {
        mGroupId = groupId;
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

    public void setDeadline(Timestamp deadline) {
        mDeadline = deadline;
    }

    public Timestamp getFinishDate() {
        return mFinishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        mFinishDate = finishDate;
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

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public boolean isFinish() {
        if(mWorkedTime/60>=mExpectedWorkingTime)
            return true;
        else
            return false;
    }

    //public void setFinish(boolean finish) {
        //mIsFinish = finish;
    //}

    public List<SubTask> getSubTasks() {
        return mSubTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        mSubTasks = subTasks;
    }

    public int getWorkingTimes() {
        return mWorkingTimes;
    }

    public void setWorkingTimes(int workingTimes) {
        mWorkingTimes = workingTimes;
    }

    /**
     * 排序接口
     */
    public static class ComparatorByName implements Comparator<Task>{

        @Override
        public int compare(Task o1, Task o2) {
            if(o1.isFinish() || o2.isFinish() && !(o1.isFinish() && o2.isFinish()))
                return o1.isFinish()?1:-1;
            if(o1.getTaskName().equals(o2.getTaskName()))
                return o1.getStartDate().compareTo(o2.getStartDate());
            else
                return o1.getTaskName().compareTo(o2.getTaskName());
        }
    }

    public static class ComparatorByWorkedTime implements Comparator<Task>{

        @Override
        public int compare(Task o1, Task o2) {
            if(o1.isFinish() || o2.isFinish() && !(o1.isFinish() && o2.isFinish()))
                return o1.isFinish()?1:-1;
            if(o1.getWorkedTime()==o2.getWorkedTime())
                return o1.getStartDate().compareTo(o2.getStartDate());
            else
                return o2.getWorkedTime()-o1.getWorkedTime();
        }
    }

    public static class ComparatorByDeadline implements Comparator<Task>{

        @Override
        public int compare(Task o1, Task o2) {
            if(o1.isFinish() || o2.isFinish() && !(o1.isFinish() && o2.isFinish()))
                return o1.isFinish()?1:-1;
            if(o1.getDeadline()!=null && o2.getDeadline()!=null) {
                if (o1.getDeadline().equals(o2.getDeadline()))
                    return o1.getStartDate().compareTo(o2.getStartDate());
                else
                    return o1.getDeadline().compareTo(o2.getDeadline());
            } else {
                if(o1.getDeadline()!=null)
                    return -1;
                else
                    return 1;
            }
        }
    }

    public static class ComparatorByPriority implements Comparator<Task>{

        @Override
        public int compare(Task o1, Task o2) {
            if(o1.isFinish() || o2.isFinish() && !(o1.isFinish() && o2.isFinish()))
                return o1.isFinish()?1:-1;
            if(o1.getPriority()==o2.getPriority())
                return o1.getStartDate().compareTo(o2.getStartDate());
            else
                return o2.getPriority()-o1.getPriority();
        }
    }

    public static class ComparatorDefault implements Comparator<Task>{

        @Override
        public int compare(Task o1, Task o2) {
            if(o1.isFinish() || o2.isFinish() && !(o1.isFinish() && o2.isFinish()))
                return o1.isFinish()?1:-1;
            return o1.getStartDate().compareTo(o2.getStartDate());
        }
    }
}
