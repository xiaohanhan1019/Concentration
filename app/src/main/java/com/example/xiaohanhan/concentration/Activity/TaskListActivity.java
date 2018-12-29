package com.example.xiaohanhan.concentration.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaohanhan.concentration.Fragment.TaskListFragment;
import com.example.xiaohanhan.concentration.Model.Task;
import com.example.xiaohanhan.concentration.Model.TaskGroup;
import com.example.xiaohanhan.concentration.Model.TaskLab;
import com.example.xiaohanhan.concentration.Application.MyApplication;
import com.example.xiaohanhan.concentration.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private Toolbar mToolbar;
    private FrameLayout mFuckUpLayout;
    private LinearLayout mAddTaskLayout;

    private TextView mDateday;
    private TextView mDateMonth;
    private TextView mDateWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        //replace actionbar
        mToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        setTitle("");

        Date now = new Date();
        mDateday = findViewById(R.id.date_day);
        mDateday.setText(new SimpleDateFormat("dd", Locale.getDefault()).format(now));
        mDateday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TodayActivity.newIntent(TaskListActivity.this);
                startActivity(intent);
                overridePendingTransition(R.anim.goright_left_to_right,R.anim.goright_right_to_left);
            }
        });
        mDateMonth = findViewById(R.id.date_month);
        mDateMonth.setText(new SimpleDateFormat("MMMM", Locale.getDefault()).format(now));
        mDateWeek = findViewById(R.id.date_week);
        mDateWeek.setText(new SimpleDateFormat("E", Locale.getDefault()).format(now));

//        mShowChart = findViewById(R.id.show_chart);
//        mShowChart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        mSettings = findViewById(R.id.settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SettingsActivity.newIntent(TaskListActivity.this);
                startActivity(intent);
            }
        });

        mAddTask = findViewById(R.id.add_task);
        mAddTask.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE && !v.getText().toString().equals("")){
                    int currentIdx = mViewPager.getCurrentItem();
                    int groupId = TaskLab.get().getTaskGroupIdByPostion(currentIdx);
                    Task task = new Task(groupId,v.getText().toString());
                    TaskLab.get().dbAddTask(task);
                    TaskLab.get().getTaskGroups(groupId).addTask(task);
                    v.setText("");
                    //increase the size of group
                    TaskListFragment taskListFragment = (TaskListFragment)mMyViewpagerAdapter.instantiateItem(mViewPager,currentIdx);
                    taskListFragment.updateUI();
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
                popupWindow.setBackgroundDrawable(new ColorDrawable(0xFFFAFAFA));
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setAnimationStyle(R.style.anim_popupwindow);
                popupWindow.showAtLocation(findViewById(R.id.task_list_main_layout), Gravity.BOTTOM,0,0);

                //set background
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

                //sort
                //默认按照 starttime排
                final View.OnClickListener sort = new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        int currentIdx = mViewPager.getCurrentItem();
                        String sortKey;
                        TaskListFragment taskListFragment = (TaskListFragment)mMyViewpagerAdapter.instantiateItem(mViewPager,currentIdx);
                        switch(v.getId()){
                            case R.id.sort_by_name:
                                taskListFragment.sort(Task.SORT_by_name);
                                sortKey = Task.SORT_by_name;
                                break;
                            case R.id.sort_by_deadline:
                                taskListFragment.sort(Task.SORT_by_deadline);
                                sortKey = Task.SORT_by_deadline;
                                break;
                            case R.id.sort_by_worked_time:
                                taskListFragment.sort(Task.SORT_by_worked_time);
                                sortKey = Task.SORT_by_worked_time;
                                break;
                            case R.id.sort_by_priority:
                                taskListFragment.sort(Task.SORT_by_priority);
                                sortKey = Task.SORT_by_priority;
                                break;
                            default:
                                sortKey="";
                                break;
                        }
                        SharedPreferences userSettings = getSharedPreferences("Concentration_setting", Context.MODE_PRIVATE);
                        userSettings.edit().putString(MyApplication.PREFERENCE_SETTINGS_SORT_KEY,sortKey).apply();
                        popupWindow.dismiss();
                    }
                };

                Button sortByWorkedTime = view.findViewById(R.id.sort_by_worked_time);
                sortByWorkedTime.setOnClickListener(sort);
                Button sortByName = view.findViewById(R.id.sort_by_name);
                sortByName.setOnClickListener(sort);
                Button sortByPriority = view.findViewById(R.id.sort_by_priority);
                sortByPriority.setOnClickListener(sort);
                Button sortByDeadline = view.findViewById(R.id.sort_by_deadline);
                sortByDeadline.setOnClickListener(sort);
            }
        });

        mViewPager = findViewById(R.id.task_list_view_pager);
        mFuckUpLayout = findViewById(R.id.fuck_up_layout);
        mFuckUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });
        mAddTaskLayout = findViewById(R.id.add_task_layout);

        updateUI();

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

    void updateUI(){
        TaskLab.get().getData();
        mTaskGroups = TaskLab.get().getTaskGroups();
        if(mTaskGroups==null){
            //TODO
            Log.i("debug","fuck up");
            mFuckUpLayout.setVisibility(View.VISIBLE);
            mToolbar.setVisibility(View.GONE);
            mAddTaskLayout.setVisibility(View.GONE);
        } else {
            mAddTaskLayout.setVisibility(View.VISIBLE);
            mToolbar.setVisibility(View.VISIBLE);
            mFuckUpLayout.setVisibility(View.GONE);
            FragmentManager fm = getSupportFragmentManager();
            mMyViewpagerAdapter = new MyViewpagerAdapter(fm);
            mViewPager.setAdapter(mMyViewpagerAdapter);
            mViewPager.setCurrentItem(mCurrentPage);
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
