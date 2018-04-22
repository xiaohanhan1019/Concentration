package com.example.xiaohanhan.concentration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskLab;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by xiaohanhan on 2018/4/18.
 */
//TODO

public class TaskFragment extends Fragment {

    private static final String ARG_TASK_ID = "task_id";
    private static final String ARG_TASK_GROUP_ID = "group_task_id";

    private static final String DIALOG_DEADLINE = "dialog_deadline";
    private static final String DIALOG_REMINDER = "dialog_reminder";
    private static final String DIALOG_EXPECTED_WORKING_TIME = "dialog_expected_working_time";

    private static final int REQUEST_DEADLINE = 0;
    private static final int REQUEST_EXPECTED_WORKING_TIME = 1;

    private Task mTask;

    private ImageButton mArrowBack;
    private EditText mTaskName;
    private TextView mTaskDeadline;
    private TextView mTaskExpectedWorkingTime;

    private RelativeLayout mTaskDeadlineLayout;
    private RelativeLayout mTaskReminderLayout;
    private RelativeLayout mTaskPriorityLayout;
    private Spinner mTaskPrioritySpinner;
    private RelativeLayout mTaskExpectedLayout;
    private RelativeLayout mTaskNoteLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int taskId = getArguments().getInt(ARG_TASK_ID);
        int taskGroupId = getArguments().getInt(ARG_TASK_GROUP_ID);
        mTask = TaskLab.get(getActivity()).getTaskGroups(taskGroupId).getTask(taskId);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task,container,false);

        mArrowBack = v.findViewById(R.id.arrow_back);
        mArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上层 manifest singletop
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

        mTaskReminderLayout = v.findViewById(R.id.detail_task_reminder_layout);
        mTaskReminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //two dialog with viewpager
            }
        });

        mTaskPrioritySpinner = v.findViewById(R.id.detail_task_priority_spinner);
        final List<String> priority = new ArrayList<>();
        priority.add("Not set");
        priority.add("Relax");
        priority.add("Normal");
        priority.add("Urgency");
        mTaskPrioritySpinner.setAdapter(new MyAdatper(priority,getActivity()));
        mTaskPrioritySpinner.setSelection(mTask.getPriority(),true);
        mTaskPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch(selectedItem){
                    case "Relax":
                        mTask.setPriority(1);
                        break;
                    case "Normal":
                        mTask.setPriority(2);
                        break;
                    case "Urgency":
                        mTask.setPriority(3);
                        break;
                    default:
                        mTask.setPriority(0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTaskExpectedWorkingTime = v.findViewById(R.id.detail_task_expected);
        mTaskExpectedWorkingTime.setText(String.format(Locale.getDefault(),"%d/%d",mTask.getWorkedTime(),mTask.getExpectedWorkingTime()));

        mTaskExpectedLayout = v.findViewById(R.id.detail_task_expected_layout);
        mTaskExpectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ExpectedTimeFragment expectedTimeFragment = ExpectedTimeFragment.newInstance(mTask.getExpectedWorkingTime());
                expectedTimeFragment.setTargetFragment(TaskFragment.this,REQUEST_EXPECTED_WORKING_TIME);
                expectedTimeFragment.show(fm,DIALOG_EXPECTED_WORKING_TIME);
            }
        });

        mTaskNoteLayout = v.findViewById(R.id.detail_task_note_layout);
        mTaskNoteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //above keyboard
            }
        });

        return v;
    }

    public static TaskFragment newInstance(int taskGroupId,int taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_GROUP_ID,taskGroupId);
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
        } else if(requestCode == REQUEST_EXPECTED_WORKING_TIME){
            int expectedWorkingTime = (int) data.getSerializableExtra(ExpectedTimeFragment.EXTRA_EXPECTED_WORKING_TIME);
            mTask.setExpectedWorkingTime(expectedWorkingTime);
            mTaskExpectedWorkingTime.setText(String.format(Locale.getDefault(),"%d/%d",mTask.getWorkedTime(),mTask.getExpectedWorkingTime()));
        }
    }

    public class MyAdatper extends BaseAdapter{
        List<String> mPriority;
        Context mContext;

        public MyAdatper(List<String> priority, Context context){
            this.mPriority =priority;
            mContext=context;
        }

        @Override
        public int getCount() {
            return mPriority.size();
        }

        @Override
        public Object getItem(int position) {
            return mPriority.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder myViewHolder;
            if (convertView == null) {
                myViewHolder = new MyViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_item_priority, null);
                myViewHolder.mPriorityName = convertView.findViewById(R.id.priority_name);
                convertView.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) convertView.getTag();
            }

            myViewHolder.mPriorityName.setText(mPriority.get(position));

            return convertView;
        }

        private class MyViewHolder{
            TextView mPriorityName;
        }
    }
}
