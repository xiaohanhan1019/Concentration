package com.example.xiaohanhan.concentration.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaohanhan.concentration.Activity.TaskStatisticActivity;
import com.example.xiaohanhan.concentration.Dialog.DatePickerFragment;
import com.example.xiaohanhan.concentration.Dialog.ExpectedTimeFragment;
import com.example.xiaohanhan.concentration.Dialog.NoteFragment;
import com.example.xiaohanhan.concentration.Dialog.ReminderPickerFragment;
import com.example.xiaohanhan.concentration.Model.SubTask;
import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskGroup;
import com.example.xiaohanhan.concentration.Model.TaskLab;
import com.example.xiaohanhan.concentration.R;

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
    private static final String DIALOG_NOTE = "dialog_note";

    private static final int REQUEST_DEADLINE = 0;
    private static final int REQUEST_EXPECTED_WORKING_TIME = 1;
    private static final int REQUEST_REMINDER_DATE = 2;
    private static final int REQUEST_NOTE = 3;

    private Task mTask;
    private TaskGroup mTaskGroup;

    private ImageButton mArrowBack;
    private EditText mTaskName;
    private TextView mTaskDeadline;
    private TextView mTaskExpectedWorkingTime;
    private TextView mTaskReminder;
    private TextView mTaskNote;
    private TextView mTaskDurationAndTimes;
    private TextView mGroupName;
    private Button mDeleteTask;
    private ImageButton mTaskStatistic;

    private RelativeLayout mTaskDeadlineLayout;
    private RelativeLayout mTaskReminderLayout;
    private RelativeLayout mTaskPriorityLayout;
    private Spinner mTaskPrioritySpinner;
    private RelativeLayout mTaskExpectedLayout;
    private RelativeLayout mTaskNoteLayout;

    private RecyclerView mSubTaskRecycleView;
    private SubTaskAdapter mSubTaskAdapter;
    private EditText mAddSubTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int taskId = getArguments().getInt(ARG_TASK_ID);
        int taskGroupId = getArguments().getInt(ARG_TASK_GROUP_ID);
        mTaskGroup = TaskLab.get().getTaskGroups(taskGroupId);
        mTask = mTaskGroup.getTask(taskId);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);

        mArrowBack = v.findViewById(R.id.arrow_back);
        mArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上层 manifest singletop
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                    getActivity().overridePendingTransition(R.anim.goback_left_to_right,R.anim.goback_right_to_left);
                }
            }
        });

        mGroupName = v.findViewById(R.id.detail_group_name);
        mGroupName.setText(mTaskGroup.getName());

        mTaskName = v.findViewById(R.id.detail_task_name);
        mTaskName.setText(mTask.getTaskName());
        mTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setTaskName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTaskDurationAndTimes = v.findViewById(R.id.detail_task_duration_and_times);
        mTaskDurationAndTimes.setText(getResources().getString(R.string.detail_duration_and_times,mTask.getWorkedTime()/60.0,mTask.getWorkingTimes()));

        mTaskDeadline = v.findViewById(R.id.detail_task_deadline);
        if (mTask.getDeadline() != null) {
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
                expectedDateDialog.setTargetFragment(TaskFragment.this, REQUEST_DEADLINE);
                expectedDateDialog.show(fm, DIALOG_DEADLINE);
            }
        });

        mTaskReminder = v.findViewById(R.id.detail_task_reminder);
        if (mTask.getReminder() != null) {
            mTaskReminder.setText(new SimpleDateFormat("MM/d E HH:mm", Locale.getDefault()).format(mTask.getReminder()));
        } else {
            mTaskReminder.setText("");
        }

        mTaskReminderLayout = v.findViewById(R.id.detail_task_reminder_layout);
        mTaskReminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //two dialog with viewpager
                FragmentManager fm = getFragmentManager();
                ReminderPickerFragment reminderPickerDialog = ReminderPickerFragment.newInstance(mTask.getReminder());
                reminderPickerDialog.setTargetFragment(TaskFragment.this, REQUEST_REMINDER_DATE);
                reminderPickerDialog.show(fm, DIALOG_REMINDER);
            }
        });

        mTaskPrioritySpinner = v.findViewById(R.id.detail_task_priority_spinner);
        final List<String> priority = new ArrayList<>();
        priority.add("Not set");
        priority.add("Relax");
        priority.add("Normal");
        priority.add("Urgent");
        mTaskPrioritySpinner.setAdapter(new MyAdatper(priority, getActivity()));
        mTaskPrioritySpinner.setSelection(mTask.getPriority(), true);
        mTaskPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "Relax":
                        mTask.setPriority(1);
                        break;
                    case "Normal":
                        mTask.setPriority(2);
                        break;
                    case "Urgent":
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
        if(mTask.getExpectedWorkingTime() !=0 ) {
            mTaskExpectedWorkingTime.setText(String.format(Locale.getDefault(), "%.1f/%d", mTask.getWorkedTime()/60.0, mTask.getExpectedWorkingTime()));
        } else {
            mTaskExpectedWorkingTime.setText("");
        }
        mTaskExpectedLayout = v.findViewById(R.id.detail_task_expected_layout);
        mTaskExpectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ExpectedTimeFragment expectedTimeFragment = ExpectedTimeFragment.newInstance(mTask.getExpectedWorkingTime());
                expectedTimeFragment.setTargetFragment(TaskFragment.this, REQUEST_EXPECTED_WORKING_TIME);
                expectedTimeFragment.show(fm, DIALOG_EXPECTED_WORKING_TIME);
            }
        });

        mTaskNote = v.findViewById(R.id.detail_task_note);
        if (mTask.getDetail() == null) {
            mTaskNote.setTextColor(Color.GRAY);
            mTaskNote.setText(R.string.detail_note);
        } else {
            mTaskNote.setText(mTask.getDetail());
            mTaskNote.setTextColor(Color.GRAY);
        }

        mTaskNoteLayout = v.findViewById(R.id.detail_task_note_layout);
        mTaskNoteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                NoteFragment noteFragment = NoteFragment.newInstance(mTask.getDetail());
                noteFragment.setTargetFragment(TaskFragment.this, REQUEST_NOTE);
                noteFragment.show(fm, DIALOG_NOTE);
            }
        });

        mSubTaskRecycleView = v.findViewById(R.id.subTask_recycler_view);
        mSubTaskRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddSubTask = v.findViewById(R.id.detail_add_sub_task);
        mAddSubTask.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE && !v.getText().toString().equals("")){
                    SubTask subTask = new SubTask(mTask.getId(),v.getText().toString());
                    TaskLab.get().dbAddSubtask(subTask);
                    mTask.addSubtask(subTask);
                    v.setText("");
                }
                return false;
            }
        });

        mDeleteTask = v.findViewById(R.id.detail_delete_task);
        mDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskLab.get().dbDeleteTask(mTask.getId());
                TaskLab.get().getTaskGroups(mTaskGroup.getId()).deleteTask(mTask.getId());
                Toast.makeText(getActivity(),"Delete!",Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });

        mTaskStatistic = v.findViewById(R.id.detail_task_statistic);
        mTaskStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskStatisticActivity.newIntent(getActivity(),mTaskGroup.getId(),mTask.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.goright_left_to_right,R.anim.goright_right_to_left);
            }
        });

        updateUI();

        return v;
    }

    public static TaskFragment newInstance(int taskGroupId, int taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_GROUP_ID, taskGroupId);
        args.putSerializable(ARG_TASK_ID, taskId);

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 弹出dialog以后各种返回处理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DEADLINE) {
            try {
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DEADLINE);
                mTask.setDeadline(new Timestamp(date.getTime()));
                mTaskDeadline.setText(new SimpleDateFormat("MM/dd E", Locale.getDefault()).format(mTask.getDeadline()));
            } catch (Exception ex) {
                mTask.setDeadline(null);
                mTaskDeadline.setText("");
            }
        } else if (requestCode == REQUEST_EXPECTED_WORKING_TIME) {
            int expectedWorkingTime = (int) data.getSerializableExtra(ExpectedTimeFragment.EXTRA_EXPECTED_WORKING_TIME);
            mTask.setExpectedWorkingTime(expectedWorkingTime);
            if(expectedWorkingTime != 0) {
                mTaskExpectedWorkingTime.setText(String.format(Locale.getDefault(), "%.1f/%d", mTask.getWorkedTime()/60.0, mTask.getExpectedWorkingTime()));
            } else {
                mTaskExpectedWorkingTime.setText("");
            }
        } else if (requestCode == REQUEST_REMINDER_DATE) {
            try {
                Date date = (Date) data.getSerializableExtra(ReminderPickerFragment.EXTRA_REMINDER_PICKER_DATE);
                mTask.setReminder(new Timestamp(date.getTime()));
                mTaskReminder.setText(new SimpleDateFormat("MM/dd E HH:mm", Locale.getDefault()).format(mTask.getReminder()));
            } catch (Exception ex) {
                mTask.setReminder(null);
                mTaskReminder.setText("");
            }
        } else if (requestCode == REQUEST_NOTE) {
            String note = (String) data.getSerializableExtra(NoteFragment.EXTRA_NOTE);
            if (note != null) {
                mTask.setDetail(note);
                mTaskNote.setText(note);
                mTaskNote.setTextColor(Color.GRAY);
            } else {
                mTask.setDetail(null);
                mTaskNote.setText(R.string.detail_note);
                mTaskNote.setTextColor(Color.GRAY);
            }
        }
    }

    /**
     * 任务优先级的适配器
     */
    public class MyAdatper extends BaseAdapter {
        List<String> mPriority;
        Context mContext;

        public MyAdatper(List<String> priority, Context context) {
            this.mPriority = priority;
            mContext = context;
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

        private class MyViewHolder {
            TextView mPriorityName;
        }
    }

    /**
     * 子任务适配器
     */
    private class SubTaskHolder extends RecyclerView.ViewHolder{

        private CheckBox mIsFinish;
        private TextView mSubTaskName;
        private ImageButton mDeleteSubTask;

        private SubTask mSubTask;

        public SubTaskHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_subtask,parent,false));

            mIsFinish = itemView.findViewById(R.id.subTask_finish);
            mIsFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mSubTask.setFinish(isChecked);
                    TaskLab.get().dbUpdateSubtask(mSubTask);
                }
            });
            mSubTaskName = itemView.findViewById(R.id.subTask_name);
            mDeleteSubTask = itemView.findViewById(R.id.subTask_delete);
            mDeleteSubTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskLab.get().dbDeleteSubtask(mSubTask.getId());
                    mTask.deleteSubtask(mSubTask.getId());
                    updateUI();
                }
            });
        }

        public void bind(SubTask subTask){
            mSubTask = subTask;
            mIsFinish.setChecked(mSubTask.isFinish());
            mSubTaskName.setText(mSubTask.getSubTaskName());
        }
    }

    private class SubTaskAdapter extends RecyclerView.Adapter<SubTaskHolder> {

        private List<SubTask> mSubtasks;

        public SubTaskAdapter(List<SubTask> subTasks){
            mSubtasks = subTasks;
        }

        @Override
        public SubTaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SubTaskHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(SubTaskHolder holder, int position) {
            SubTask subTask = mSubtasks.get(position);
            holder.bind(subTask);
        }

        @Override
        public int getItemCount() {
            return mSubtasks.size();
        }
    }

    /**
     * 返回时完成数据库更新
     */
    @Override
    public void onPause() {
        TaskLab.get().dbUpdateTask(mTask);
        super.onPause();
    }

    private void updateUI(){
        if(mSubTaskAdapter==null) {
            mSubTaskAdapter = new SubTaskAdapter(mTask.getSubTasks());
            mSubTaskRecycleView.setAdapter(mSubTaskAdapter);
        } else {
            mSubTaskAdapter.notifyDataSetChanged();
        }
    }

}
