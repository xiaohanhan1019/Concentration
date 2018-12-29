package com.example.xiaohanhan.concentration.Util;

import android.util.Log;

import com.example.xiaohanhan.concentration.Model.ConcentrationRecord;
import com.example.xiaohanhan.concentration.Model.Interruption;
import com.example.xiaohanhan.concentration.Model.SubTask;
import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskGroup;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohanhan on 2018/5/1.
 */

public class MySQLHelper {

    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://10.0.2.2/concentration?connectTimeout=2000";
    private static final String user = "root";
    private static final String password = "1019";

    private static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<TaskGroup> getTaskGroupList () throws SQLException {
        List<TaskGroup> taskGroups = new ArrayList<>();
        String sql = "select * from task_group";

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                TaskGroup taskGroup = new TaskGroup();
                taskGroup.setId(rs.getInt(TaskGroup.KEY_id));
                taskGroup.setName(rs.getString(TaskGroup.KEY_groupName));

                String sql2 = MessageFormat.format("select * from task where task_group_id = {0}",Integer.toString(taskGroup.getId()));
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ResultSet rs2 = ps2.executeQuery();

                List<Task> tasks = new ArrayList<>();
                while(rs2.next()){
                    Task task = new Task();
                    task.setId(rs2.getInt(Task.KEY_id));
                    task.setGroupId(rs2.getInt(Task.KEY_task_group_id));
                    task.setTaskName(rs2.getString(Task.KEY_task_name));
                    task.setStartDate(rs2.getTimestamp(Task.KEY_start_time));
                    task.setDeadline(rs2.getTimestamp(Task.KEY_expect_time));
                    task.setFinishDate(rs2.getTimestamp(Task.KEY_finish_time));
                    task.setReminder(rs2.getTimestamp(Task.KEY_reminder));
                    task.setPriority(rs2.getInt(Task.KEY_priority));
                    task.setExpectedWorkingTime(rs2.getInt(Task.KEY_expected_working_time));
                    task.setWorkedTime(rs2.getInt(Task.KEY_worked_time));
                    task.setWorkingTimes(rs2.getInt(Task.KEY_working_times));
                    task.setDetail(rs2.getString(Task.KEY_detail));
                    //task.setFinish(rs2.getInt(Task.KEY_is_finish) == 1);

                    String sql3 = MessageFormat.format("select * from subtask where task_id = {0}",Integer.toString(task.getId()));
                    PreparedStatement ps3 = conn.prepareStatement(sql3);
                    ResultSet rs3 = ps3.executeQuery();

                    List<SubTask> subTasks = new ArrayList<>();
                    while(rs3.next()){
                        SubTask subTask = new SubTask();
                        subTask.setId(rs3.getInt(SubTask.KEY_id));
                        subTask.setTaskId(rs3.getInt(SubTask.KEY_task_id));
                        subTask.setSubTaskName(rs3.getString(SubTask.KEY_subtask_name));
                        subTask.setFinish(rs3.getInt(SubTask.KEY_is_finish) == 1);

                        subTasks.add(subTask);
                    }
                    task.setSubTasks(subTasks);
                    tasks.add(task);
                }
                taskGroup.setTasks(tasks);
                taskGroups.add(taskGroup);
            }
            return taskGroups;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeAll(conn,ps,rs);
        }
    }

    public List<Interruption> getInterruptionList() throws SQLException{
        List<Interruption> interruptions = new ArrayList<>();
        String sql = "select * from interruption";

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Interruption interruption = new Interruption();
                interruption.setId(rs.getInt(Interruption.KEY_id));
                interruption.setInterruptionName(rs.getString(Interruption.KEY_interruption_name));
                interruption.setTimes(rs.getInt(Interruption.KEY_times));

                interruptions.add(interruption);
            }
            return interruptions;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeAll(conn,ps,rs);
        }
    }

    public List<ConcentrationRecord> getConcentrationRecord(int taskId) throws SQLException{
        List<ConcentrationRecord> concentrationRecords = new ArrayList<>();
        String sql = "select * from concentration_record where task_id='" + taskId + "'";

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ConcentrationRecord concentrationRecord = new ConcentrationRecord();
                concentrationRecord.setStartTime(rs.getTimestamp(ConcentrationRecord.KEY_start_time));
                concentrationRecord.setWorkingtime(rs.getInt(ConcentrationRecord.KEY_working_time));
                concentrationRecord.setInterrupted(rs.getInt(ConcentrationRecord.KEY_is_interrupted)==1);

                concentrationRecords.add(concentrationRecord);
            }
            return concentrationRecords;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeAll(conn,ps,rs);
        }
    }

    public List<ConcentrationRecord> getTodayConcentrationRecord() throws SQLException{
        List<ConcentrationRecord> concentrationRecords = new ArrayList<>();
        String sql = "select concentration_record.start_time,concentration_record.working_time,concentration_record.is_interrupted,task.task_name from concentration_record,task where task.id=concentration_record.task_id and DateDiff(concentration_record.start_time,now())=0;";

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ConcentrationRecord concentrationRecord = new ConcentrationRecord();
                concentrationRecord.setStartTime(rs.getTimestamp(ConcentrationRecord.KEY_start_time));
                concentrationRecord.setWorkingtime(rs.getInt(ConcentrationRecord.KEY_working_time));
                concentrationRecord.setInterrupted(rs.getInt(ConcentrationRecord.KEY_is_interrupted)==1);
                concentrationRecord.setTaskName(rs.getString(ConcentrationRecord.KEY_task_name));

                concentrationRecords.add(concentrationRecord);
            }
            return concentrationRecords;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeAll(conn,ps,rs);
        }
    }

    public int ExecuteSQL(String sql,Object... args) {
        int id = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //Log.i("sql",ps.toString());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            //返回自增的id
            if (rs.next())
                id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn,ps,null);
        }
        return id;
    }

}
