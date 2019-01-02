package com.example.xiaohanhan.concentration.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiaohanhan.concentration.Application.MyApplication;
import com.example.xiaohanhan.concentration.R;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private ImageButton mArrowBack;
    private EditText mSettingsTime;
    private LinearLayout mSettingTimeLinearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_settings,container,false);

        mArrowBack = v.findViewById(R.id.settings_arrow_back);
        mArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
            }
        });

        //保存用户信息到SharedPreference
        mSettingsTime = v.findViewById(R.id.settings_working_time);
        SharedPreferences userSettings = getActivity().getSharedPreferences("Concentration_setting", Context.MODE_PRIVATE);
        int settingTime = userSettings.getInt(MyApplication.PREFERENCE_SETTINGS_WORKING_TIME,1800);
        mSettingsTime.setText(String.format(Locale.getDefault(),"%d",settingTime/60));
        mSettingsTime.setSelection(mSettingsTime.getText().length());
        mSettingsTime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    SharedPreferences userSettings = getActivity().getSharedPreferences("Concentration_setting", Context.MODE_PRIVATE);
                    userSettings.edit().putInt(MyApplication.PREFERENCE_SETTINGS_WORKING_TIME,Integer.parseInt(v.getText().toString())*60).apply();
                }
                return false;
            }
        });

        mSettingTimeLinearLayout = v.findViewById(R.id.settings_working_time_layout);
        mSettingTimeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsTime.setFocusable(true);
                mSettingsTime.setFocusableInTouchMode(true);
                mSettingsTime.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm!=null)
                    imm.showSoftInput(mSettingsTime, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        return v;
    }
}
