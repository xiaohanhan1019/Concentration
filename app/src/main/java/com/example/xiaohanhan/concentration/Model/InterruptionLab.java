package com.example.xiaohanhan.concentration.Model;

import android.os.AsyncTask;

import com.example.xiaohanhan.concentration.Util.MySQLHelper;

import java.util.ArrayList;
import java.util.List;

public class InterruptionLab {

    private static InterruptionLab sInterruptionLab;

    private List<Interruption> mInterruptions;

    public static InterruptionLab get(){
        if(sInterruptionLab == null){
            sInterruptionLab = new InterruptionLab();
        }
        return sInterruptionLab;
    }

    private InterruptionLab (){
        mInterruptions = new ArrayList<>();
        SelectAllInterruption selectAllInterruption = new SelectAllInterruption();
        try{
            mInterruptions = selectAllInterruption.execute().get();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public List<Interruption> getInterruptions(){
        return mInterruptions;
    }

    public void dbInsertInterruption(Interruption interruption){
        InsertInterruption insertInterruption = new InsertInterruption(interruption);
        insertInterruption.execute();
    }

    public void dbUpdateInterruption(Interruption interruption){
        UpdateInterruption updateInterruption = new UpdateInterruption(interruption);
        updateInterruption.execute();
    }

    //Database
    private static class SelectAllInterruption extends AsyncTask<Void,Void,List<Interruption>>{

        @Override
        protected List<Interruption> doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try {
                return mySQLHelper.getInterruptionList();
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    private static class InsertInterruption extends AsyncTask<Void,Void,Void>{

        Interruption mInterruption;

        public InsertInterruption(Interruption interruption) {
            mInterruption = interruption;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mInterruption.setId(mySQLHelper.ExecuteSQL("Insert into interruption(interruption_name) values(?)",mInterruption.getInterruptionName()));
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    private static class UpdateInterruption extends AsyncTask<Void,Void,Void>{

        Interruption mInterruption;

        public UpdateInterruption(Interruption interruption) {
            mInterruption = interruption;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try{
                mySQLHelper.ExecuteSQL("Update interruption set interruption_name=?,times=? where id=?",mInterruption.getInterruptionName(),mInterruption.getTimes(),mInterruption.getId());
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

//    public void dbDeleteInterruption(int interruptionId){
//        DeleteInterruption deleteInterruption = new DeleteInterruption(interruptionId);
//        deleteInterruption.execute();
//    }

//    不能删除
//    private static class DeleteInterruption extends AsyncTask<Void,Void,Void>{
//
//        private int mInterruptionId;
//
//        public DeleteInterruption(int id) {
//            mInterruptionId = id;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            MySQLHelper mySQLHelper = new MySQLHelper();
//            try{
//                mySQLHelper.ExecuteSQL("Delete from interruption where id=?",mInterruptionId);
//            } catch (Exception ex){
//                ex.printStackTrace();
//            }
//            return null;
//        }
//    }
}
