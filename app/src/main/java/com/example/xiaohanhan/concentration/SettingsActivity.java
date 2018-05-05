package com.example.xiaohanhan.concentration;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.settings_fragment_container);

        if (fragment == null){
            fragment = new SettingsFragment();
            fm.beginTransaction().add(R.id.task_fragment_container,fragment).commit();
        }
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext,SettingsActivity.class);
        return intent;
    }
}
