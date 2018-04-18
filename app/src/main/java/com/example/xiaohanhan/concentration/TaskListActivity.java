package com.example.xiaohanhan.concentration;

import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class TaskListActivity extends AppCompatActivity {

    ImageView mShowChartImageView;
    ImageView mSettingsImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        //replace actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.task_list_fragment_container);

        if (fragment == null){
            fragment = new TaskListFragment();
            fm.beginTransaction().add(R.id.task_list_fragment_container,fragment).commit();
        }

        mShowChartImageView = findViewById(R.id.show_chart);
        mShowChartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 切换activity
            }
        });

        mSettingsImageView = findViewById(R.id.settings);
        mSettingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 切换activity
            }
        });
    }
}
