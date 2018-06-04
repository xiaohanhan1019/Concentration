package com.example.xiaohanhan.concentration.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.xiaohanhan.concentration.Fragment.ConcentrationFragment;
import com.example.xiaohanhan.concentration.R;

public class ConcentrationActivity extends AppCompatActivity {

    private static final String EXTRA_TASK_ID = "ConcentrationActivity_task_id";
    private static final String EXTRA_TASK_GROUP_ID = "ConcentrationActivity_task_group_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentrate);

        int taskId = getIntent().getIntExtra(EXTRA_TASK_ID,0);
        int taskGroupId = getIntent().getIntExtra(EXTRA_TASK_GROUP_ID,0);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.concentration_fragment_container);

        if (fragment == null){
            fragment = ConcentrationFragment.newInstance(taskGroupId,taskId);
            fm.beginTransaction().add(R.id.concentration_fragment_container,fragment).commit();
        }
    }

    public static Intent newIntent(Context packageContext, int taskGroupId, int taskId){
        Intent intent = new Intent(packageContext,ConcentrationActivity.class);
        intent.putExtra(EXTRA_TASK_ID,taskId);
        intent.putExtra(EXTRA_TASK_GROUP_ID,taskGroupId);
        return intent;
    }

    /**
     * 不允许用户在Concentrate的时候返回
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "Please Concentrate!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
