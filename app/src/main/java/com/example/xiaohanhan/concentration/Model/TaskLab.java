package com.example.xiaohanhan.concentration.Model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xiaohanhan.concentration.Util.MySQLHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskLab {

    private static TaskLab sTaskLab;

    private List<TaskGroup> mTaskGroups;

    public static TaskLab get(){
        if(sTaskLab == null){
            sTaskLab = new TaskLab();
        }
        return sTaskLab;
    }

    private TaskLab (){
        mTaskGroups = new ArrayList<>();
        getData();
    }

    public void getData(){
        SelectAllTaskGroup selectAllTaskGroup = new SelectAllTaskGroup();
        try {
            mTaskGroups = selectAllTaskGroup.execute().get();
            Log.i("sql","get data");
        } catch (Exception ex){
            ex.printStackTrace();
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

    public int getTaskGroupIdByPostion(int position){
        return mTaskGroups.get(position).getId();
    }

    public void addTaskGroup(TaskGroup taskGroup){
        mTaskGroups.add(taskGroup);
    }

    public void deleteTaskGroup(int groupId){
        for(int i=0;i<mTaskGroups.size();i++){
            if(mTaskGroups.get(i).getId()==groupId){
                mTaskGroups.remove(i);
            }
        }
    }

    /**
     *数据库操作
     */
    public void dbAddTaskGroup(TaskGroup taskGroup){
        AddTaskGroup addTaskGroup = new AddTaskGroup(taskGroup);
        addTaskGroup.execute();
    }

    public void dbDeleteTaskGroup(int groupId){
        DeleteTaskGroup deleteTaskGroup = new DeleteTaskGroup(groupId);
        deleteTaskGroup.execute();
    }

    public void dbAddTask(Task task){
        AddTask addTask = new AddTask(task);
        addTask.execute();
    }

    public void dbUpdateTask(Task task){
        UpdateTask updateTask = new UpdateTask(task);
        updateTask.execute();
    }

    public void dbDeleteTask(int taskId){
        DeleteTask deleteTask = new DeleteTask(taskId);
        deleteTask.execute();
    }

    public void dbAddSubtask(SubTask subTask){
        AddSubtask addSubtask = new AddSubtask(subTask);
        addSubtask.execute();
    }

    public void dbUpdateSubtask(SubTask subTask){
        UpdateSubtask updateSubtask = new UpdateSubtask(subTask);
        updateSubtask.execute();
    }

    public void dbDeleteSubtask(int subtaskId){
        Deletesubtask deletesubtask = new Deletesubtask(subtaskId);
        deletesubtask.execute();
    }

    private static class SelectAllTaskGroup extends AsyncTask<Void,Void,List<TaskGroup>>{

        @Override
        protected List<TaskGroup> doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try {
                return mySQLHelper.getTaskGroupList();
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    private static class AddTaskGroup extends AsyncTask<Void,Void,Void>{

        private TaskGroup mTaskGroup;

        public AddTaskGroup(TaskGroup taskGroup) {
            mTaskGroup = taskGroup;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mTaskGroup.setId(mySQLHelper.ExecuteSQL("Insert into task_group(group_name) values(?)",mTaskGroup.getName()));
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

    }

    //taskGroup cannot be edited

    private static class DeleteTaskGroup extends AsyncTask<Void,Void,Void>{

        private int mGroupId;

        public DeleteTaskGroup(int groupId) {
            mGroupId = groupId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mySQLHelper.ExecuteSQL("Delete concentration_record from concentration_record,task where task.task_group_id=? and concentration_record.task_id=task.id",mGroupId);
                mySQLHelper.ExecuteSQL("Delete subtask from subtask,task where task.task_group_id=? and subtask.task_id=task.id",mGroupId);
                mySQLHelper.ExecuteSQL("Delete from task where task_group_id=?",mGroupId);
                mySQLHelper.ExecuteSQL("Delete from task_group where id=?",mGroupId);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    private static class AddTask extends AsyncTask<Void,Void,Void>{

        private Task mTask;

        public AddTask(Task task) {
            mTask = task;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mTask.setId(mySQLHelper.ExecuteSQL("Insert into task(task_group_id,task_name,start_time,expect_working_time) values(?,?,?,?)",mTask.getGroupId(),mTask.getTaskName(),mTask.getStartDate(),mTask.getExpectedWorkingTime()));
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Void,Void,Void> {

        private Task mTask;

        public UpdateTask(Task task) {
            mTask = task;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mySQLHelper.ExecuteSQL("Update task set task_name=?,expect_time=?,finish_time=?,reminder=?,priority=?,expect_working_time=?,detail=?,is_finish=? where id=?"
                        ,mTask.getTaskName(),mTask.getDeadline(),mTask.getFinishDate(),mTask.getReminder(),mTask.getPriority(),mTask.getExpectedWorkingTime(),mTask.getDetail(),mTask.isFinish()?1:0,mTask.getId());
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Void,Void,Void>{

        private int mTaskId;

        public DeleteTask(int taskId) {
            mTaskId = taskId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mySQLHelper.ExecuteSQL("Delete from subtask where task_id=?",mTaskId);
                mySQLHelper.ExecuteSQL("Delete from concentration_record where task_id=?",mTaskId);
                mySQLHelper.ExecuteSQL("Delete from task where id=?",mTaskId);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    private static class AddSubtask extends AsyncTask<Void,Void,Void>{

        private SubTask mSubTask;

        public AddSubtask(SubTask subTask) {
            mSubTask = subTask;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mSubTask.setId(mySQLHelper.ExecuteSQL("Insert into subtask(task_id,subtask_name) values(?,?)",mSubTask.getTaskId(),mSubTask.getSubTaskName()));
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    private static class UpdateSubtask extends AsyncTask<Void,Void,Void>{

        private SubTask mSubTask;

        public UpdateSubtask(SubTask subTask) {
            mSubTask = subTask;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mySQLHelper.ExecuteSQL("Update subtask set is_finish=?,subtask_name=? where id=?",mSubTask.isFinish()?1:0,mSubTask.getSubTaskName(),mSubTask.getId());
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    private static class Deletesubtask extends AsyncTask<Void,Void,Void>{

        int subtaskId;

        public Deletesubtask(int subtaskId) {
            this.subtaskId = subtaskId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mySQLHelper.ExecuteSQL("Delete from subtask where id=?",subtaskId);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

}
