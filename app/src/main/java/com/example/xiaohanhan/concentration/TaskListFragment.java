package com.example.xiaohanhan.concentration;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.xiaohanhan.concentration.Dialog.InterruptionFragment;
import com.example.xiaohanhan.concentration.Dialog.ManageGroupFragment;
import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskGroup;
import com.example.xiaohanhan.concentration.Model.TaskLab;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by xiaohanhan on 2018/4/17.
 */

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
        mTaskGroup = TaskLab.get(getActivity()).getTaskGroups(taskGroupId);

        mTaskListActivity = (TaskListActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list,container,false);

        mTaskRecyclerView = v.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                                        TaskLab.get(getActivity()).deleteGroupById(mTaskGroup.getId());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
            if(requestCode == REQUEST_GROUP){
                int groupId = (int)data.getSerializableExtra(ManageGroupFragment.EXTRA_GROUP_ID);
                mTaskListActivity.setCurrentPage(groupId);
            }
        }
    }

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

            //绑定控件
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
                }
            });
        }

        public void bind(Task task){
            //给控件设值
            //TODO tag
            mTask = task;
            switch(mTask.getPriority()){
                case 1:
                    mTaskPriority.setText("!");
                    break;
                case 2:
                    mTaskPriority.setText("!!");
                    break;
                case 3:
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
                mTaskStatus.setText(String.format(Locale.getDefault(), "(%.1f/%d)", mTask.getWorkedTime(), mTask.getExpectedWorkingTime()));
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
            //先跳转到activity 通过activity跳转到fragment 可能有更好的办法
            Intent intent = TaskActivity.newIntent(getActivity(), mTaskGroup.getId(),mTask.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            mTask.setFinish(!mTask.isFinish());
            if(mTask.isFinish()) {
                mTaskName.setTextColor(Color.GRAY);
                mTaskName.setPaintFlags(mTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mTaskName.setTextColor(Color.DKGRAY);
                mTaskName.setPaintFlags(mTaskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
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

    private void updateUI(){
        if(mTaskAdapter==null) {
            mTaskAdapter = new TaskAdapter(mTaskGroup.getTasks());
            mTaskRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.notifyDataSetChanged();
        }
        mTaskNumber.setText(String.format(Locale.getDefault(),"(%d)",mTaskGroup.getTaskNumber()));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
