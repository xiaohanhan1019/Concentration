package com.example.xiaohanhan.concentration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    private TaskGroup mTaskGroup;

    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mTaskAdapter;

    private TextView mTaskGroupName;
    private EditText mAddTask;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list,container,false);

        mTaskRecyclerView = v.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTaskGroupName = v.findViewById(R.id.task_group_name);
        mTaskGroupName.setText(mTaskGroup.getName());
        mAddTask = v.findViewById(R.id.add_task);

        return v;
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTaskPriority;
        private TextView mTaskName;
        private TextView mTaskStatus;
        private TextView mTaskDeadline;

        private ImageButton mTaskConcentration;

        private Task mTask;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_task,parent,false));

            itemView.setOnClickListener(this);

            //绑定控件
            //TODO tag
            mTaskPriority = itemView.findViewById(R.id.task_priority);
            mTaskName = itemView.findViewById(R.id.task_name);
            mTaskStatus = itemView.findViewById(R.id.task_status);
            mTaskDeadline = itemView.findViewById(R.id.task_dead_line);
            mTaskConcentration = itemView.findViewById(R.id.task_concentration);
            mTaskConcentration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),ConcentrationActivity.class);
                    startActivity(intent);
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
            mTaskStatus.setText(String.format(Locale.getDefault(),"%d/%d",mTask.getWorkedTime(),mTask.getExpectedWorkingTime()));
            if(mTask.getDeadline()!=null) {
                mTaskDeadline.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mTask.getDeadline()));
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
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
