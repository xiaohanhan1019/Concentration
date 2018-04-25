package com.example.xiaohanhan.concentration;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class TaskActivity extends AppCompatActivity {

    private static final String EXTRA_TASK_ID = "TaskActivity_task_id";
    private static final String EXTRA_TASK_GROUP_ID = "TaskActivity_task_group_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        int taskId = getIntent().getIntExtra(EXTRA_TASK_ID,0);
        int taskGroupId = getIntent().getIntExtra(EXTRA_TASK_GROUP_ID,0);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.task_fragment_container);

        if (fragment == null){
            fragment = TaskFragment.newInstance(taskGroupId,taskId);
            fm.beginTransaction().add(R.id.task_fragment_container,fragment).commit();
        }
    }

    public static Intent newIntent(Context packageContext,int taskGroupId, int taskId){
        Intent intent = new Intent(packageContext,TaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID,taskId);
        intent.putExtra(EXTRA_TASK_GROUP_ID,taskGroupId);
        return intent;
    }
}
