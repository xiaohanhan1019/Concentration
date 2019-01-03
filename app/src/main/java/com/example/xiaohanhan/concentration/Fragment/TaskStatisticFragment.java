package com.example.xiaohanhan.concentration.Fragment;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaohanhan.concentration.Model.ConcentrationRecord;
import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskGroup;
import com.example.xiaohanhan.concentration.Model.TaskLab;
import com.example.xiaohanhan.concentration.R;
import com.example.xiaohanhan.concentration.Util.MySQLHelper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TaskStatisticFragment extends Fragment {

    private static final String ARG_TASK_ID = "task_id";
    private static final String ARG_TASK_GROUP_ID = "group_task_id";

    private TaskGroup mTaskGroup;
    private Task mTask;
    private List<ConcentrationRecord> mConcentrationRecords;

    private ImageButton mArrowBack;
    private TextView mTaskName;
    private TextView mConcentratingTimes;
    private TextView mWorkingTime;
    private TextView mInterruption;
    private TextView mExpectedWorkingTime;
    private TextView mStartTime;

    private RecyclerView mRecordRecyclerView;
    private RecordAdapter mRecordAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int taskId = getArguments().getInt(ARG_TASK_ID);
        int taskGroupId = getArguments().getInt(ARG_TASK_GROUP_ID);
        mTaskGroup = TaskLab.get().getTaskGroups(taskGroupId);
        mTask = mTaskGroup.getTask(taskId);

        mConcentrationRecords = new ArrayList<>();
        SelectAllRecord selectAllRecord = new SelectAllRecord(mTask.getId());
        try {
            mConcentrationRecords = selectAllRecord.execute().get();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_statistic, container, false);

        mArrowBack = v.findViewById(R.id.statistic_arrow_back);
        mArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.goback_left_to_right,R.anim.goback_right_to_left);
            }
        });

        mTaskName = v.findViewById(R.id.statistic_task_name);
        mTaskName.setText(mTask.getTaskName());

        mConcentratingTimes = v.findViewById(R.id.statistic_working_times);
        mConcentratingTimes.setText(String.format(Locale.getDefault(),"%d",mTask.getWorkingTimes()));

        mWorkingTime = v.findViewById(R.id.statistic_time);
        mWorkingTime.setText(String.format(Locale.getDefault(),"%.1f",mTask.getWorkedTime()/60.0));

        mInterruption = v.findViewById(R.id.statistic_interruption);
        int interruptions=0;
        for(ConcentrationRecord concentrationRecord : mConcentrationRecords){
            if(concentrationRecord.isInterrupted()){
                interruptions++;
            }
        }
        mInterruption.setText(String.format(Locale.getDefault(),"%d",interruptions));

        mExpectedWorkingTime = v.findViewById(R.id.statistic_expected_working_time);
        mExpectedWorkingTime.setText(getResources().getString(R.string.record_expected_working_time,mTask.getExpectedWorkingTime()));

        mStartTime = v.findViewById(R.id.statistic_start_time);
        mStartTime.setText(getResources().getString(R.string.statistic_start_time,getGMT8TimeByTimeStamp(mTask.getStartDate())));

        mRecordRecyclerView = v.findViewById(R.id.statistic_recycler_view);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    public static TaskStatisticFragment newInstance(int taskGroupId,int taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        args.putSerializable(ARG_TASK_GROUP_ID, taskGroupId);

        TaskStatisticFragment fragment = new TaskStatisticFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Concentration记录适配器
     */
    private class RecordHolder extends RecyclerView.ViewHolder{

        private TextView mRecordWorkingTime;
        private TextView mRecordIsInterrupted;
        private TextView mRecordStartTime;
        private ImageView mRecordDot;

        private ConcentrationRecord mRecord;

        public RecordHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_concentration_record,parent,false));

            mRecordWorkingTime = itemView.findViewById(R.id.record_working_time);
            mRecordIsInterrupted = itemView.findViewById(R.id.record_is_interrupted);
            mRecordStartTime = itemView.findViewById(R.id.record_start_time);
            mRecordDot = itemView.findViewById(R.id.record_dot);
        }

        public void bind(ConcentrationRecord concentrationRecord){
            mRecord =concentrationRecord;
            mRecordWorkingTime.setText(getResources().getString(R.string.record_working_time,mRecord.getWorkingtime()/60.0));
            Drawable icon = ContextCompat.getDrawable(getActivity(),R.drawable.ic_action_dot);
            Drawable tintIcon = DrawableCompat.wrap(icon);
            if(mRecord.isInterrupted()) {
                mRecordIsInterrupted.setVisibility(View.VISIBLE);
                mRecordIsInterrupted.setText(getResources().getString(R.string.record_interrupt));
                DrawableCompat.setTintList(tintIcon, ContextCompat.getColorStateList(getActivity(),R.color.colorLightBlue));
                mRecordDot.setImageDrawable(tintIcon);
            } else {
                mRecordIsInterrupted.setVisibility(View.GONE);
                DrawableCompat.setTintList(tintIcon, ContextCompat.getColorStateList(getActivity(),R.color.colorOrangeRed));
                mRecordDot.setImageDrawable(tintIcon);
            }
            mRecordStartTime.setText(getGMT8TimeByTimeStamp(mRecord.getStartTime()));
        }
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordHolder>{

        private List<ConcentrationRecord> mConcentrationRecords;

        public RecordAdapter(List<ConcentrationRecord> concentrationRecords) {
            mConcentrationRecords = concentrationRecords;
        }

        @Override
        public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecordHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(RecordHolder holder, int position) {
            ConcentrationRecord concentrationRecord = mConcentrationRecords.get(position);
            holder.bind(concentrationRecord);
        }

        @Override
        public int getItemCount() {
            return mConcentrationRecords.size();
        }
    }

    private void updateUI(){
        if(mRecordAdapter == null){
            mRecordAdapter = new RecordAdapter(mConcentrationRecords);
            mRecordRecyclerView.setAdapter(mRecordAdapter);
        } else {
            mRecordAdapter.notifyDataSetChanged();
        }
    }

    /**
     *
     * @param timestamp 数据库的时间戳变量
     * @return 返回GMT+8时区时间
     */
    public String getGMT8TimeByTimeStamp(Timestamp timestamp){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(timestamp);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return String.format(Locale.getDefault(),"%04d-%02d-%02d %02d:%02d:%02d",year,month,day,hour,minute,second);
    }

    /**
     * 获取数据库数据
     */
    private static class SelectAllRecord extends AsyncTask<Void,Void,List<ConcentrationRecord>> {

        int taskId;

        public SelectAllRecord(int taskId) {
            this.taskId = taskId;
        }

        @Override
        protected List<ConcentrationRecord> doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try {
                return mySQLHelper.getConcentrationRecord(taskId);
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

    }
}

