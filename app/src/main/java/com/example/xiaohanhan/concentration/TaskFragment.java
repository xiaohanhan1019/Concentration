package com.example.xiaohanhan.concentration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaohanhan.concentration.Model.DatePickerFragment;
import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskLab;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xiaohanhan on 2018/4/18.
 */

public class TaskFragment extends Fragment {

    private static final String ARG_TASK_ID = "task_id";

    private static final String DIALOG_DEADLINE = "dialog_deadline";

    private static final int REQUEST_DEADLINE = 0;

    private Task mTask;

    private ImageButton mArrowBack;
    private EditText mTaskName;
    private TextView mTaskDeadline;

    private RelativeLayout mTaskDeadlineLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int taskId = getArguments().getInt(ARG_TASK_ID);
        mTask = TaskLab.get(getActivity()).getTask(taskId);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task,container,false);

        mArrowBack = v.findViewById(R.id.arrow_back);
        mArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上层
                if(NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
            }
        });

        mTaskName = v.findViewById(R.id.detail_task_name);
        mTaskName.setText(mTask.getTaskName());

        mTaskDeadline = v.findViewById(R.id.detail_task_deadline);
        if(mTask.getDeadline()!=null) {
            mTaskDeadline.setText(new SimpleDateFormat("MM/d E", Locale.getDefault()).format(mTask.getDeadline()));
        } else {
            mTaskDeadline.setText("");
        }

        mTaskDeadlineLayout = v.findViewById(R.id.detail_task_deadline_layout);
        mTaskDeadlineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment expectedDateDialog = DatePickerFragment.newInstance(mTask.getDeadline());
                expectedDateDialog.setTargetFragment(TaskFragment.this,REQUEST_DEADLINE);
                expectedDateDialog.show(fm,DIALOG_DEADLINE);
            }
        });

        return v;
    }

    public static TaskFragment newInstance(int taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID,taskId);
        
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DEADLINE){
            try {
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DEADLINE);
                mTask.setDeadLine(new Timestamp(date.getTime()));
                mTaskDeadline.setText(new SimpleDateFormat("MM/d E", Locale.getDefault()).format(mTask.getDeadline()));
            }catch (Exception ex){
                mTask.setDeadLine(null);
                mTaskDeadline.setText("");
            }
        }
    }
}
