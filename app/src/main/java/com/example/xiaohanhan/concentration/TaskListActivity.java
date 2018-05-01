package com.example.xiaohanhan.concentration;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskGroup;
import com.example.xiaohanhan.concentration.Model.TaskLab;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private final String SAVED_CURRENT_PAGE = "current_page";

    ImageButton mShowChart;
    ImageButton mSettings;
    ImageButton mFilter;

    private ViewPager mViewPager;
    private MyViewpagerAdapter mMyViewpagerAdapter;

    private List<TaskGroup> mTaskGroups;
    private int mCurrentPage;

    private EditText mAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        //replace actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        mViewPager = findViewById(R.id.task_list_view_pager);
        mTaskGroups = TaskLab.get(this).getTaskGroups();
        FragmentManager fm = getSupportFragmentManager();
        mMyViewpagerAdapter = new MyViewpagerAdapter(fm);
        mViewPager.setAdapter(mMyViewpagerAdapter);
        mViewPager.setCurrentItem(mCurrentPage);

        mShowChart = findViewById(R.id.show_chart);
        mShowChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 切换activity
            }
        });

        mSettings = findViewById(R.id.settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 切换activity
            }
        });

        mAddTask = findViewById(R.id.add_task);
        mAddTask.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE && !v.getText().toString().equals("")){
                    int currentIdx = mViewPager.getCurrentItem();
                    int groupId = TaskLab.get(TaskListActivity.this).getTaskGroupIdByPostion(currentIdx);
                    Task task = new Task(groupId);
                    task.setTaskName(v.getText().toString());
                    v.setText("");
                    TaskLab.get(TaskListActivity.this).getTaskGroups(groupId).addTask(task);
                    Intent intent = TaskActivity.newIntent(TaskListActivity.this, groupId,task.getId());
                    startActivity(intent);
                }
                return false;
            }
        });

        mFilter = findViewById(R.id.filter);
        mFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(TaskListActivity.this).inflate(R.layout.pop_up_window_sort,null);
                final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(findViewById(R.id.task_list_main_layout), Gravity.BOTTOM,0,0);

                //背景变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.7f;
                getWindow().setAttributes(lp);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                    }
                });

                //所有排序按钮的onclick事件
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        switch(v.getId()){
                            case R.id.sort_by_name:
                                break;
                            default:
                                break;
                        }
                        popupWindow.dismiss();
                    }
                };
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_CURRENT_PAGE,mViewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPage = savedInstanceState.getInt(SAVED_CURRENT_PAGE);
    }

    public void notifyChange(){
        mMyViewpagerAdapter.notifyDataSetChanged();
    }

    public void setCurrentPage(int groupId){
        for(int i=0;i<mTaskGroups.size();i++){
            if(mTaskGroups.get(i).getId()==groupId){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    private class MyViewpagerAdapter extends FragmentStatePagerAdapter{

        public MyViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TaskGroup taskGroup = mTaskGroups.get(position);
            return TaskListFragment.newInstance(taskGroup.getId());
        }

        @Override
        public int getCount() {
            return mTaskGroups.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        View view;
//        //不知道有没有更好的实现办法
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mApplication.setMove(false);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mApplication.setMove(true);
//                break;
//            case MotionEvent.ACTION_UP:
//                if(!mApplication.isMove()) {
//                    view = getCurrentFocus();
//                    if (view == findViewById(R.id.add_task) || view == findViewById(R.id.task_group_name)) {
//                        UtilHelpers.hideKeyboard(ev, view, TaskListActivity.this);//调用方法判断是否需要隐藏键盘
//                        mApplication.setMove(false);
//                        return true;
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

}
