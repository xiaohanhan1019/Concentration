package com.example.xiaohanhan.concentration.Model;

import android.os.AsyncTask;

import com.example.xiaohanhan.concentration.MySQLHelper;

import java.util.List;

/**
 * Created by xiaohanhan on 2018/5/3.
 */

public class ConcentrationRecordLab {

    private static ConcentrationRecordLab sConcentrationRecordLab;

    private List<ConcentrationRecord> mConcentrationRecords;

    public static ConcentrationRecordLab get(){
        if(sConcentrationRecordLab == null){
            sConcentrationRecordLab= new ConcentrationRecordLab();
        }
        return sConcentrationRecordLab;
    }

    private ConcentrationRecordLab()
    {

    }

    public List<ConcentrationRecord> getConcentrationRecords() {
        return mConcentrationRecords;
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
                mConcentrationRecord.setId(mySQLHelper.ExecuteSQL("Insert into concentration_record(task_id,start_time,working_time) values(?,?,?)"
                        ,mConcentrationRecord.getTaskId(),mConcentrationRecord.getStartTime(),mConcentrationRecord.getWorkingtime()));
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }
}
