package com.example.xiaohanhan.concentration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskLab;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by xiaohanhan on 2018/4/17.
 */

public class TaskListFragment extends Fragment{

    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mTaskAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list,container,false);

        mTaskRecyclerView = v.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
            Intent intent = TaskActivity.newIntent(getActivity(), mTask.getId());
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
        TaskLab taskLab = TaskLab.get(getActivity());
        List<Task> tasks = taskLab.getTasks();

        if(mTaskAdapter==null) {
            mTaskAdapter = new TaskAdapter(tasks);
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
