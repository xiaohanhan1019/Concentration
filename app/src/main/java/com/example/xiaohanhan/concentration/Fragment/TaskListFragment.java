package com.example.xiaohanhan.concentration.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.xiaohanhan.concentration.Activity.ConcentrationActivity;
import com.example.xiaohanhan.concentration.Dialog.InterruptionFragment;
import com.example.xiaohanhan.concentration.Dialog.ManageGroupFragment;
import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskGroup;
import com.example.xiaohanhan.concentration.Model.TaskLab;
import com.example.xiaohanhan.concentration.Application.MyApplication;
import com.example.xiaohanhan.concentration.R;
import com.example.xiaohanhan.concentration.Activity.TaskActivity;
import com.example.xiaohanhan.concentration.Activity.TaskListActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskListFragment extends Fragment{

    private static final String ARG_TASK_GROUP_ID = "task_group_id";

    private static final int REQUEST_CONCENTRATION = 0;
    private static final int REQUEST_GROUP = 1;

    private static final String DIALOG_INTERRUPT = "dialog_interruption";
    private static final String DIALOG_MANAGE_GROUP = "dialog_manage_group";

    private TaskListActivity mTaskListActivity;

    private TaskGroup mTaskGroup;

    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mTaskAdapter;
    private TextView mTaskNumber;

    private ImageButton mManageGroup;
    private TextView mTaskGroupName;

    public static TaskListFragment newInstance(int taskGroupId){

        Bundle args = new Bundle();

        args.putSerializable(ARG_TASK_GROUP_ID, taskGroupId);

        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int taskGroupId = (int)getArguments().getSerializable(ARG_TASK_GROUP_ID);
        mTaskGroup = TaskLab.get().getTaskGroups(taskGroupId);

        mTaskListActivity = (TaskListActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list,container,false);

        mTaskRecyclerView = v.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        DefaultItemAnimator animator = new DefaultItemAnimator();
//        animator.setAddDuration(2000);
//        animator.setRemoveDuration(2000);
//        mTaskRecyclerView.setItemAnimator(animator);

        mTaskGroupName = v.findViewById(R.id.task_group_name);
        mTaskGroupName.setText(mTaskGroup.getName());

        mTaskNumber = v.findViewById(R.id.task_number);
        mTaskNumber.setText(String.format(Locale.getDefault(),"(%d)",mTaskGroup.getTaskNumber()));

        mManageGroup = v.findViewById(R.id.manage_group);
        mManageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_up_window_manage_group,null);
                final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);

                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                popupWindow.showAsDropDown(mTaskGroupName,dm.widthPixels,-20);

                Button manageGroup = view.findViewById(R.id.menu_manage_group);
                manageGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getFragmentManager();
                        ManageGroupFragment manageGroupFragment = new ManageGroupFragment();
                        manageGroupFragment.setTargetFragment(TaskListFragment.this, REQUEST_GROUP);
                        manageGroupFragment.show(fm, DIALOG_MANAGE_GROUP);
                        popupWindow.dismiss();
                    }
                });

                Button deleteGroup = view.findViewById(R.id.menu_delete_group);
                deleteGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(getActivity()).setTitle("Are you sure?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TaskLab.get().dbDeleteTaskGroup(mTaskGroup.getId());
                                        TaskLab.get().deleteTaskGroup(mTaskGroup.getId());
                                        mTaskListActivity.notifyChange();
                                    }
                                })
                                .setNegativeButton("Cancel",null).show();
                        popupWindow.dismiss();
                    }
                });
            }
        });

        return v;
    }

    /**
     *跳转回来以后的一些处理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Concentration页面回来以后判断是否是人为中断
        if(resultCode == ConcentrationFragment.RESULT_CONCENTRATION) {
            if (requestCode == REQUEST_CONCENTRATION) {
                boolean isInterrupt = (boolean) data.getSerializableExtra(ConcentrationFragment.EXTRA_IS_INTERRPUTED);
                if (isInterrupt) {
                    FragmentManager fm = getFragmentManager();
                    InterruptionFragment interruptionFragment = new InterruptionFragment();
                    interruptionFragment.show(fm, DIALOG_INTERRUPT);
                }
            }
        } else if(resultCode == ManageGroupFragment.RESULT_MANAGE_GROUP){
            //选择任务组更新UI
            if(requestCode == REQUEST_GROUP){
                int groupId = (int)data.getSerializableExtra(ManageGroupFragment.EXTRA_GROUP_ID);
                mTaskListActivity.setCurrentPage(groupId);
            }
        }
    }

    /**
     * 任务适配器
     */
    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        private TextView mTaskPriority;
        private TextView mTaskName;
        private TextView mTaskStatus;
        private TextView mTaskDeadline;

        private ImageButton mTaskConcentration;

        private Task mTask;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_task,parent,false));

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mTaskPriority = itemView.findViewById(R.id.task_priority);
            mTaskName = itemView.findViewById(R.id.task_name);
            mTaskStatus = itemView.findViewById(R.id.task_status);
            mTaskDeadline = itemView.findViewById(R.id.task_dead_line);
            mTaskConcentration = itemView.findViewById(R.id.task_concentration);
            mTaskConcentration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ConcentrationActivity.newIntent(getActivity(), mTaskGroup.getId(),mTask.getId());
                    startActivityForResult(intent,REQUEST_CONCENTRATION);
                    getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            });
        }

        public void bind(Task task){
            //给控件设值
            mTask = task;
            switch(mTask.getPriority()){
                case 1:
                    mTaskPriority.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    mTaskPriority.setText("!");
                    break;
                case 2:
                    mTaskPriority.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPink));
                    mTaskPriority.setText("!!");
                    break;
                case 3:
                    mTaskPriority.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorOrangeRed));
                    mTaskPriority.setText("!!!");
                    break;
                default:
                    mTaskPriority.setText("");
                    break;
            }
            mTaskName.setText(mTask.getTaskName());
            if(mTask.isFinish()) {
                mTaskName.setTextColor(Color.GRAY);
                mTaskName.setPaintFlags(mTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mTaskName.setTextColor(Color.DKGRAY);
                mTaskName.setPaintFlags(mTaskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            if(mTask.getExpectedWorkingTime()!=0) {
                mTaskStatus.setText(String.format(Locale.getDefault(), "(%.1f/%d)", mTask.getWorkedTime()/60.0, mTask.getExpectedWorkingTime()));
                mTaskStatus.setVisibility(View.VISIBLE);
            } else {
                mTaskStatus.setText("");
                mTaskStatus.setVisibility(View.GONE);
            }

            if(mTask.getDeadline()!=null) {
                mTaskDeadline.setText(getResources().getString(R.string.deadline,new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mTask.getDeadline())));
                mTaskDeadline.setVisibility(View.VISIBLE);
            } else {
                mTaskDeadline.setText("");
                mTaskDeadline.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = TaskActivity.newIntent(getActivity(), mTaskGroup.getId(),mTask.getId());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.goright_left_to_right,R.anim.goright_right_to_left);
        }

        /**
         *长按退出Concentration
         */
        @Override
        public boolean onLongClick(View v) {
            //mTask.setFinish(!mTask.isFinish());
            SharedPreferences userSettings = getActivity().getSharedPreferences("Concentration_setting", Context.MODE_PRIVATE);
            String sortKey = userSettings.getString(MyApplication.PREFERENCE_SETTINGS_SORT_KEY,"");
            if(mTask.isFinish()) {
                mTaskName.setTextColor(Color.GRAY);
                mTaskName.setPaintFlags(mTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mTask.setFinishDate(new Timestamp(new Date().getTime()));
                sort(sortKey);
            } else {
                mTaskName.setTextColor(Color.DKGRAY);
                mTaskName.setPaintFlags(mTaskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                mTask.setFinishDate(null);
                sort(sortKey);
            }
            TaskLab.get().dbUpdateTask(mTask);
            return true;
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> mTasks;

        public TaskAdapter(List<Task> tasks){
            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new TaskHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            Task task=mTasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }

    public void updateUI(){
        if(mTaskAdapter==null) {
            mTaskAdapter = new TaskAdapter(mTaskGroup.getTasks());
            mTaskRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.notifyDataSetChanged();
        }
        mTaskNumber.setText(String.format(Locale.getDefault(),"(%d)",mTaskGroup.getTaskNumber()));
        SharedPreferences userSettings = getActivity().getSharedPreferences("Concentration_setting", Context.MODE_PRIVATE);
        String sortKey = userSettings.getString(MyApplication.PREFERENCE_SETTINGS_SORT_KEY,"");
        sort(sortKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void sort(String sortKey){
        Comparator<Task> comparator;
        switch (sortKey){
            case Task.SORT_by_name:
                comparator = new Task.ComparatorByName();
                break;
            case Task.SORT_by_deadline:
                comparator = new Task.ComparatorByDeadline();
                break;
            case Task.SORT_by_priority:
                comparator = new Task.ComparatorByPriority();
                break;
            case Task.SORT_by_worked_time:
                comparator = new Task.ComparatorByWorkedTime();
                break;
            default:
                comparator = new Task.ComparatorDefault();
                break;
        }
        mTaskGroup.getTasks().sort(comparator);
        mTaskAdapter.notifyDataSetChanged();
    }
}
