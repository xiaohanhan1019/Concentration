package com.example.xiaohanhan.concentration.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.xiaohanhan.concentration.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by xiaohanhan on 2018/4/22.
 */

public class ReminderPickerFragment extends android.support.v4.app.DialogFragment {

    public static final String EXTRA_REMINDER_PICKER_DATE = "ReminderPickerFragment_Reminder_date";

    private static final String ARG_DATE = "date";

    MyViewPagerAdapter myViewPagerAdapter;

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    public static ReminderPickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);

        ReminderPickerFragment fragment = new ReminderPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reminder,null);

        ViewPager viewPager = view.findViewById(R.id.reminder_view_pager);
        List<Fragment> fragments = getFragments();
        myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(myViewPagerAdapter);

        Button okButton = view.findViewById(R.id.reminder_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker = myViewPagerAdapter.getItem(0).getView().findViewById(R.id.dialog_date_picker);
                mTimePicker = myViewPagerAdapter.getItem(1).getView().findViewById(R.id.dialog_time_picker);
                int year = mDatePicker.getYear();
                int month=mDatePicker.getMonth();
                int day=mDatePicker.getDayOfMonth();
                int hour=mTimePicker.getHour();
                int minute=mTimePicker.getMinute();
                Date date = new GregorianCalendar(year,month,day,hour,minute).getTime();
                sendResult(Activity.RESULT_OK,date);
                dismiss();
            }
        });

        Button cancelButton = view.findViewById(R.id.reminder_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button clearButton = view.findViewById(R.id.reminder_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_OK,null);
                dismiss();
            }
        });

        return view;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ReminderDateFragment.newInstance((Date)getArguments().getSerializable(ARG_DATE)));
        fragments.add(ReminderTimeFragment.newInstance((Date)getArguments().getSerializable(ARG_DATE)));
        return fragments;
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments){
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    private void sendResult(int resultCode, Date date){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_REMINDER_PICKER_DATE,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}