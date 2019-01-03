package com.example.xiaohanhan.concentration.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.xiaohanhan.concentration.R;

import java.util.Calendar;
import java.util.Date;

public class ReminderTimeFragment extends Fragment {

    private static final String ARG_TIME = "time";
    private TimePicker mTimePicker;

    public static ReminderTimeFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME,date);

        ReminderTimeFragment fragment = new ReminderTimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time,container,false);

        mTimePicker = view.findViewById(R.id.dialog_time_picker);

        Date date = (Date)getArguments().getSerializable(ARG_TIME);
        if(date!=null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);

            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }

        return view;
    }
}
