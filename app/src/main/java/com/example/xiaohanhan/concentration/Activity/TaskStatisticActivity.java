package com.example.xiaohanhan.concentration.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.xiaohanhan.concentration.Fragment.TaskStatisticFragment;
import com.example.xiaohanhan.concentration.R;

public class TaskStatisticActivity extends AppCompatActivity {

    private static final String EXTRA_TASK_ID = "TaskStatisticActivity_task_id";
    private static final String EXTRA_TASK_GROUP_ID = "TaskStatisticActivity_task_group_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        int taskId = getIntent().getIntExtra(EXTRA_TASK_ID,0);
        int taskGroupId = getIntent().getIntExtra(EXTRA_TASK_GROUP_ID,0);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("StatisticFragment");

        if (fragment == null){
            fragment = TaskStatisticFragment.newInstance(taskId,taskGroupId);
            fm.beginTransaction().add(R.id.fragment_container,fragment,"StatisticFragment").commit();
        }
    }

    public static Intent newIntent(Context packageContext, int taskId,int taskGroupId){
        Intent intent = new Intent(packageContext,TaskStatisticActivity.class);
        intent.putExtra(EXTRA_TASK_ID,taskId);
        intent.putExtra(EXTRA_TASK_GROUP_ID,taskGroupId);
        return intent;
    }
}
