package com.example.xiaohanhan.concentration.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.xiaohanhan.concentration.Fragment.SettingsFragment;
import com.example.xiaohanhan.concentration.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.settings_fragment_container);

        if (fragment == null){
            fragment = new SettingsFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext,SettingsActivity.class);
        return intent;
    }
}
