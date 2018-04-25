package com.example.xiaohanhan.concentration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskLab;

/**
 * Created by xiaohanhan on 2018/4/18.
 */

public class ConcentrationFragment extends Fragment{

    private static final String ARG_TASK_GROUP_ID = "task_group_id";
    private static final String ARG_TASK_ID = "task_id";

    private CircleProgressView mCircleProgressView;
    private TextView mTaskName;
    private Button mHoldDownButton;

    private Task mTask;

    private int mTime;

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
        mTime = userSettings.getInt(MyApplication.PREFERENCE_SETTINGS_WORKING_TIME,50);

        int taskId = getArguments().getInt(ARG_TASK_ID);
        int taskGroupId = getArguments().getInt(ARG_TASK_GROUP_ID);
        mTask = TaskLab.get(getActivity()).getTaskGroups(taskGroupId).getTask(taskId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_concentration, container, false);

        mTaskName = v.findViewById(R.id.concentrate_task_name);
        mTaskName.setText(mTask.getTaskName());

        mCircleProgressView = v.findViewById(R.id.circle_progress_view);
        mCircleProgressView.setTotalTime(mTime);
        mCircleProgressView.start();

        mHoldDownButton = v.findViewById(R.id.concentrate_hold_down);
        mHoldDownButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCircleProgressView.setInterrupt(true);
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                mTask.setTimes(mTask.getTimes()+1);
                mTask.setWorkedTime(mTask.getWorkedTime()+mCircleProgressView.getWorkingTime());
                return false;
            }
        });

        return v;
    }

    //TODO saveBundle
}
