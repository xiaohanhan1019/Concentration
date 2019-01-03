package com.example.xiaohanhan.concentration.Dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xiaohanhan.concentration.Model.TaskGroup;
import com.example.xiaohanhan.concentration.Model.TaskLab;
import com.example.xiaohanhan.concentration.R;
import com.example.xiaohanhan.concentration.Activity.TaskListActivity;
import com.matrixxun.starry.badgetextview.MaterialBadgeTextView;

import java.util.List;

public class ManageGroupFragment extends DialogFragment {

    public static final String EXTRA_GROUP_ID = "manage_group_fragment_group_id";

    public static final int RESULT_MANAGE_GROUP = 2;

    List<TaskGroup> mTaskGroups;

    private EditText mAddGroupName;

    private RecyclerView mManageGroupRecycleView;
    private GroupAdapter mGroupAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTaskGroups = TaskLab.get().getTaskGroups();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_manage_group,null);
        Window window = getDialog().getWindow();
        if(window!=null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.interrupt_dialog_background));
        }

        mAddGroupName = view.findViewById(R.id.add_group_name);
        mAddGroupName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    TaskGroup taskGroup = new TaskGroup(v.getText().toString());
                    TaskLab.get().dbAddTaskGroup(taskGroup);
                    TaskLab.get().addTaskGroup(taskGroup);
                    TaskListActivity taskListActivity = (TaskListActivity)getActivity();
                    taskListActivity.notifyChange();
                    v.setText("");
                }
                return false;
            }
        });

        ImageButton addGroup = view.findViewById(R.id.add_group);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddGroupName.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm!=null)
                    imm.showSoftInput(mAddGroupName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        mManageGroupRecycleView = view.findViewById(R.id.group_recycler_view);
        mManageGroupRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if(window!=null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = (int) (dm.widthPixels * 0.6);
            params.height = (int) (dm.heightPixels * 0.4);
            window.setAttributes(params);
        }
    }

    private class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mGroupName;
        private ImageButton mRenameGroup;

        private TaskGroup mTaskGroup;
        private MaterialBadgeTextView mGroupCount;

        public GroupHolder(LayoutInflater inflater,ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_group,parent,false));

            itemView.setOnClickListener(this);

            mGroupName = itemView.findViewById(R.id.group_name);
            mGroupCount = itemView.findViewById(R.id.group_count);
            mRenameGroup = itemView.findViewById(R.id.task_group_rename);
            mRenameGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(TaskGroup taskGroup){
            mTaskGroup = taskGroup;
            mGroupName.setText(taskGroup.getName());
            mGroupCount.setBadgeCount(mTaskGroup.getTasks().size());
        }

        @Override
        public void onClick(View v) {
            dismiss();
            sendResult(RESULT_MANAGE_GROUP,mTaskGroup.getId());
        }
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupHolder>{

        private List<TaskGroup> mTaskGroups;

        public GroupAdapter(List<TaskGroup> taskGroups){
            mTaskGroups = taskGroups;
        }

        @Override
        public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new GroupHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(GroupHolder holder, int position) {
            TaskGroup taskGroup = mTaskGroups.get(position);
            holder.bind(taskGroup);
        }

        @Override
        public int getItemCount() {
            return mTaskGroups.size();
        }
    }

    private void updateUI(){
        if(mGroupAdapter == null){
            mGroupAdapter = new GroupAdapter(mTaskGroups);
            mManageGroupRecycleView.setAdapter(mGroupAdapter);
        } else {
            mGroupAdapter.notifyDataSetChanged();
        }
    }

    private void sendResult(int resultCode, int groupId){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_GROUP_ID,groupId);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
