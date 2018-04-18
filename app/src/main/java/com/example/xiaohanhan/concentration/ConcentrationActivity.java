package com.example.xiaohanhan.concentration;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ConcentrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentrate);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.concentration_fragment_container);

        if (fragment == null){
            fragment = new ConcentrationFragment();
            fm.beginTransaction().add(R.id.concentration_fragment_container,fragment).commit();
        }
    }
}
