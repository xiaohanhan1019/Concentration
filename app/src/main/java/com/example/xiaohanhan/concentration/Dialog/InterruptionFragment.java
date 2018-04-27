package com.example.xiaohanhan.concentration.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xiaohanhan.concentration.R;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by xiaohanhan on 2018/4/26.
 */

public class InterruptionFragment extends DialogFragment {

    private EditText mInterruptName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_interrupt,null);
        Window window = getDialog().getWindow();
        if(window!=null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.interrupt_dialog_background));
        }

        mInterruptName = view.findViewById(R.id.interrupt_name);
        mInterruptName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mInterruptName.setText("");
                }
                return false;
            }
        });

        ImageButton addInterrupt = view.findViewById(R.id.add_interrupt);
        addInterrupt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterruptName.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm!=null)
                    imm.showSoftInput(mInterruptName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if(window!=null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = (int) (dm.widthPixels * 0.5);
            params.height = (int) (dm.heightPixels * 0.3);
            window.setAttributes(params);
        }
    }
}
