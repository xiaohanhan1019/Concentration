package com.example.xiaohanhan.concentration.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.xiaohanhan.concentration.Fragment.TodayFragment;
import com.example.xiaohanhan.concentration.R;

public class TodayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("TodayFragment");

        if (fragment == null){
            fragment = TodayFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container,fragment,"TodayFragment").commit();
        }
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext,TodayActivity.class);
        return intent;
    }
}
