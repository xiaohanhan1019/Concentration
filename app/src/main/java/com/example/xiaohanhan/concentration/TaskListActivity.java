package com.example.xiaohanhan.concentration;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class TaskListActivity extends AppCompatActivity {

    MyApplication mApplication;

    ImageView mShowChartImageView;
    ImageView mSettingsImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        mApplication = (MyApplication)this.getApplicationContext();

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //不知道有没有更好的实现办法
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mApplication.setMove(false);
                break;
            case MotionEvent.ACTION_MOVE:
                mApplication.setMove(true);
                break;
            case MotionEvent.ACTION_UP:
                if(!mApplication.isMove()) {
                    View view = getCurrentFocus();
                    if (view == findViewById(R.id.add_task) || view == findViewById(R.id.task_group_name)) {
                        UtilHelpers.hideKeyboard(ev, view, TaskListActivity.this);//调用方法判断是否需要隐藏键盘
                        mApplication.setMove(false);
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
