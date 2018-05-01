package com.example.xiaohanhan.concentration;

import android.util.Log;

import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohanhan on 2018/5/1.
 */

public class MySQLHelper {

    private static final String url = "jdbc:mysql://106.2.23.8/songzihan";       //数据库地址
    private static final String username = "mysql";      //数据库用户名
    private static final String password = "mysql";        //数据库密码

    private Connection conn=null;

    private void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch(Exception ex){
            Log.i("sql","fail to connect");
            ex.printStackTrace();
        }
    }

    private void close(){
        if(conn!=null) {
            try {
                conn.close();
            } catch (Exception ex) {
                Log.i("sql", "fail to close");
                ex.printStackTrace();
            }
        }
    }

    public List<TaskGroup> selectAllTaskGroup(){
        List<TaskGroup> taskGroups = new ArrayList<>();

        String sql = "select * from task_group";

        PreparedStatement ps;
        ResultSet rs;

        this.connect();

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {
                TaskGroup taskGroup = new TaskGroup();
                taskGroup.setId(rs.getInt(TaskGroup.KEY_id));
                taskGroup.setName(rs.getString(TaskGroup.KEY_groupName));
                taskGroups.add(taskGroup);
            }
        } catch (Exception ex) {
            Log.i("sql", "fail to select taskgroup");
            ex.printStackTrace();
        }finally {
            this.close();
        }
        return taskGroups;
    }

    public List<Task> selectAllTaskByTaskGroupId(int groupId){
        List<Task> tasks = new ArrayList<>();

        String sql = MessageFormat.format("select * from task where task_group_id = {0}",Integer.toString(groupId));

        PreparedStatement ps;
        ResultSet rs;

        this.connect();

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {
                //Task task = new Task();
//                task.setId = Task.
//                tasks.add(task);
            }
        } catch (Exception ex) {
            Log.i("sql", "fail to select taskgroup");
            ex.printStackTrace();
        }finally {
            this.close();
        }
        return tasks;
    }
}
