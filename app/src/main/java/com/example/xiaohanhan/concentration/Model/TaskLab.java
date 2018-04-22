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

    private List<TaskGroup> mTaskGroups;

    public static TaskLab get(Context context){
        if(sTaskLab == null){
            sTaskLab = new TaskLab(context);
        }
        return sTaskLab;
    }

    private TaskLab (Context context){
        mTaskGroups = new ArrayList<>();
        for(int i=0;i<3;i++){
            TaskGroup taskGroup = new TaskGroup();
            mTaskGroups.add(taskGroup);
        }
    }

    public List<TaskGroup> getTaskGroups(){
        return mTaskGroups;
    }

    public TaskGroup getTaskGroups(int id){
        for(TaskGroup taskGroup:mTaskGroups){
            if(taskGroup.getId()==id){
                return taskGroup;
            }
        }
        return null;
    }
}
