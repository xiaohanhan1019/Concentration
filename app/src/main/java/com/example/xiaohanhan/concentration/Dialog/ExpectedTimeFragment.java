package com.example.xiaohanhan.concentration.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.xiaohanhan.concentration.R;

import java.util.Locale;

public class ExpectedTimeFragment extends DialogFragment {

    public static final String EXTRA_EXPECTED_WORKING_TIME = "ExpectedTimeFragment_Expected_working_time";

    static final String ARG_EXPECTED_WORKING_TIME = "expected_working_time";

    private EditText mEditText;

    public static ExpectedTimeFragment newInstance(int expectedWorkingTime) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXPECTED_WORKING_TIME,expectedWorkingTime);

        ExpectedTimeFragment fragment = new ExpectedTimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int expectedWorkingTime = (int)getArguments().getSerializable(ARG_EXPECTED_WORKING_TIME);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_working_time,null);

        mEditText = v.findViewById(R.id.expected_working_time);
        mEditText.setText(String.format(Locale.getDefault(),"%d",expectedWorkingTime));
        mEditText.setSelection(mEditText.getText().length());

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int expectedWorkingTime = 0;
                        try {
                            expectedWorkingTime = Integer.parseInt(mEditText.getText().toString());
                        } catch(Exception ex){
                            expectedWorkingTime = 0;
                        } finally {
                            sendResult(Activity.RESULT_OK,expectedWorkingTime);
                        }
                    }
                })
                .setNegativeButton("Cancel",null)
                .setNeutralButton("clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK,0);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, int expectedWorkingTime){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EXPECTED_WORKING_TIME,expectedWorkingTime);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
