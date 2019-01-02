package com.example.xiaohanhan.concentration.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.xiaohanhan.concentration.R;

import java.util.Calendar;
import java.util.Date;

public class ReminderDateFragment extends Fragment {

    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;

    public static ReminderDateFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);

        ReminderDateFragment fragment = new ReminderDateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_date,container,false);

        mDatePicker = view.findViewById(R.id.dialog_date_picker);

        Date date = (Date)getArguments().getSerializable(ARG_DATE);
        if(date!=null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            mDatePicker.init(year, month, day, null);
        }

        return view;
    }
}
