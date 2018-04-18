package com.example.xiaohanhan.concentration.Model;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohanhan on 2018/4/18.
 */

public class TaskLab {

    private static TaskLab sTaskLab;

    private List<Task> mTasks;

    public static TaskLab get(Context context){
        if(sTaskLab == null){
            sTaskLab = new TaskLab(context);
        }
        return sTaskLab;
    }

    private TaskLab (Context context){
        mTasks = new ArrayList<>();
        for(int i=0;i<10;i++){
            Task t = new Task();
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

    private static ContentValues getContentValues(Task task){
        ContentValues values = new ContentValues();
        return values;
    }
}
