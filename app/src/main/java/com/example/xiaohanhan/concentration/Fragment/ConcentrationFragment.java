package com.example.xiaohanhan.concentration.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaohanhan.concentration.Util.CircleProgressView;
import com.example.xiaohanhan.concentration.Model.ConcentrationRecord;
import com.example.xiaohanhan.concentration.Model.ConcentrationRecordLab;
import com.example.xiaohanhan.concentration.Model.SloganLab;
import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskLab;
import com.example.xiaohanhan.concentration.Application.MyApplication;
import com.example.xiaohanhan.concentration.R;

import java.lang.ref.WeakReference;

/**
 * Created by xiaohanhan on 2018/4/18.
 */

public class ConcentrationFragment extends Fragment{

    public static final String EXTRA_IS_INTERRPUTED = "Concentration_fragment_is_interrupt";

    private static final String ARG_TASK_GROUP_ID = "task_group_id";
    private static final String ARG_TASK_ID = "task_id";

    public static final int RESULT_CONCENTRATION = 1;

    private CircleProgressView mCircleProgressView;
    private TextView mTaskName;
    private Button mHoldDownButton;
    private TextView mSlogan;

    private Task mTask;

    private int mTime;
    private ConcentrateTask mConcentrateTask;

    public static ConcentrationFragment newInstance(int groupId,int taskId) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_TASK_GROUP_ID,groupId);
        args.putSerializable(ARG_TASK_ID,taskId);

        ConcentrationFragment fragment = new ConcentrationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences userSettings = getActivity().getSharedPreferences("Concentration_setting", Context.MODE_PRIVATE);
        mTime = userSettings.getInt(MyApplication.PREFERENCE_SETTINGS_WORKING_TIME,1800);

        int taskId = getArguments().getInt(ARG_TASK_ID);
        int taskGroupId = getArguments().getInt(ARG_TASK_GROUP_ID);
        mTask = TaskLab.get().getTaskGroups(taskGroupId).getTask(taskId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_concentration, container, false);

        mTaskName = v.findViewById(R.id.concentrate_task_name);
        mTaskName.setText(mTask.getTaskName());

        mCircleProgressView = v.findViewById(R.id.circle_progress_view);
        mCircleProgressView.setTotalTime(mTime);
        mConcentrateTask = new ConcentrateTask(this,mCircleProgressView,mTime,mTask.getId());
        mConcentrateTask.execute();

        mHoldDownButton = v.findViewById(R.id.concentrate_hold_down);
        mHoldDownButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mConcentrateTask.setInterrupt(true);
                return false;
            }
        });

        mSlogan = v.findViewById(R.id.concentrate_slogan);
        mSlogan.setText(SloganLab.get().getRadomSlogan());

        return v;
    }

    /**
     * 计时线程
     */
    private static class ConcentrateTask extends AsyncTask<Void,Integer,Void>{

        private WeakReference<ConcentrationFragment> mConcentrationFragmentWeakReference;
        private WeakReference<CircleProgressView> mCircleProgressViewWeakReference;
        private int mTotalTime;
        private int mCurrentTime = 0;
        private boolean mIsInterrupted;

        private ConcentrationRecord mConcentrationRecord;

        public void setInterrupt(boolean interrupt) {
            mIsInterrupted = interrupt;
        }

        public ConcentrateTask(ConcentrationFragment concentrationFragment,CircleProgressView circleProgressView,int totalTime,int taskId){
            mConcentrationFragmentWeakReference = new WeakReference<>(concentrationFragment);
            mCircleProgressViewWeakReference = new WeakReference<>(circleProgressView);
            mTotalTime = totalTime;

            mConcentrationRecord = new ConcentrationRecord(taskId);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (mCurrentTime < mTotalTime && !mIsInterrupted) {
                mCurrentTime += 1;
                publishProgress(mCurrentTime);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            CircleProgressView circleProgressView = mCircleProgressViewWeakReference.get();
            circleProgressView.setProgress(mCurrentTime);

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ConcentrationFragment concentrationFragment = mConcentrationFragmentWeakReference.get();

            if(concentrationFragment == null || concentrationFragment.getActivity().isFinishing()){
                return;
            } else {
                Task task = concentrationFragment.mTask;
                concentrationFragment.mTask.setWorkingTimes(task.getWorkingTimes()+1);
                concentrationFragment.mTask.setWorkedTime(task.getWorkedTime()+mCurrentTime);

                mConcentrationRecord.setWorkingtime(mCurrentTime);
                mConcentrationRecord.setInterrupted(mIsInterrupted);
                ConcentrationRecordLab.get().dbInsertRecord(mConcentrationRecord);
            }

            if(!mIsInterrupted) {
                concentrationFragment.sendResult(RESULT_CONCENTRATION,false);
            } else {
                concentrationFragment.sendResult(RESULT_CONCENTRATION,true);
            }

            super.onPostExecute(aVoid);
        }
    }

    /**
     * 返回给主页面数据
     */
    private void sendResult(int resultCode, boolean isInterrupted){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_INTERRPUTED,isInterrupted);

        if(!isInterrupted) {
            Toast.makeText(getActivity(), "Concentrate Finish!", Toast.LENGTH_SHORT).show();
        }

        getActivity().setResult(resultCode,intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}
