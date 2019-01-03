package com.example.xiaohanhan.concentration.Model;

import java.util.ArrayList;
import java.util.List;

public class TaskGroup {

    private int mId;
    private String mName;
    private List<Task> mTasks;

    public static final String KEY_id = "id";
    public static final String KEY_groupName = "group_name";

    public TaskGroup(){

    }

    public TaskGroup (String groupName){
        mName=groupName;
        mTasks = new ArrayList<>();
    }

    public void deleteTask(int id){
        for(int i=0;i<mTasks.size();i++){
            if(mTasks.get(i).getId()==id){
                mTasks.remove(i);
                break;
            }
        }
    }

    public void addTask(Task task){
        mTasks.add(task);
    }

    public Task getTask(int id){
        for(Task task:mTasks){
            if(task.getId()==id){
                return task;
            }
        }
        return null;
    }

    public List<Task> getTasks(){
        return mTasks;
    }

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }

    public int getTaskNumber(){
        return mTasks.size();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
