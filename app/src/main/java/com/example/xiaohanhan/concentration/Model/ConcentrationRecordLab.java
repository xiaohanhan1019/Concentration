package com.example.xiaohanhan.concentration.Model;

import android.os.AsyncTask;

import com.example.xiaohanhan.concentration.Util.MySQLHelper;

import java.util.List;

public class ConcentrationRecordLab {

    private static ConcentrationRecordLab sConcentrationRecordLab;

    public static ConcentrationRecordLab get(){
        if(sConcentrationRecordLab == null){
            sConcentrationRecordLab= new ConcentrationRecordLab();
        }
        return sConcentrationRecordLab;
    }

    private ConcentrationRecordLab()
    {

    }

    public void dbInsertRecord(ConcentrationRecord concentrationRecord){
        InsertRecord insertRecord = new InsertRecord(concentrationRecord);
        insertRecord.execute();
    }

    //database
    private static class InsertRecord extends AsyncTask<Void,Void,Void> {

        ConcentrationRecord mConcentrationRecord;

        public InsertRecord(ConcentrationRecord concentrationRecord) {
            mConcentrationRecord = concentrationRecord;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mConcentrationRecord.setId(mySQLHelper.ExecuteSQL("Insert into concentration_record(task_id,start_time,working_time,is_interrupted) values(?,?,?,?)"
                        ,mConcentrationRecord.getTaskId(),mConcentrationRecord.getStartTime(),mConcentrationRecord.getWorkingtime(),mConcentrationRecord.isInterrupted()?1:0));
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }
}
