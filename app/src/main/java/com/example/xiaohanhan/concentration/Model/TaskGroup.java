package com.example.xiaohanhan.concentration.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohanhan on 2018/4/22.
 */

public class TaskGroup {

    private int mId;
    private String mName;
    private List<Task> mTasks;

    public TaskGroup (){
        mId = (int)(Math.random() * 1000);
        mName = Integer.toString(mId);
        mTasks = new ArrayList<>();
        for(int i=0;i<3;i++){
            Task t = new Task(mId);
            mTasks.add(t);
        }
    }

    public void addTask(Task task){
        mTasks.add(task);
    }

    public List<Task> getTasks(){
        return mTasks;
    }

    public Task getTask(int id){
        for(Task task:mTasks){
            if(task.getId()==id){
                return task;
            }
        }
        return null;
    }

    public void updateTask(Task task){
        for(Task t:mTasks){
            if(t.getId()==task.getId()){
                t=task;
            }
        }
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
