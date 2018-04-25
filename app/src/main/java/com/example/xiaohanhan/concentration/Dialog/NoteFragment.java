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

/**
 * Created by xiaohanhan on 2018/4/24.
 */

public class NoteFragment extends DialogFragment {

    public static final String EXTRA_NOTE = "NoteFragment_note";

    static final String ARG_NOTE = "note";

    private EditText mEditText;

    public static NoteFragment newInstance(String note) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE,note);

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String note = (String)getArguments().getSerializable(ARG_NOTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_note,null);

        mEditText = v.findViewById(R.id.note);
        mEditText.setText(note);
        mEditText.setSelection(mEditText.getText().length());

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String note = mEditText.getText().toString();
                        if(note.equals("")){
                            sendResult(Activity.RESULT_OK,null);
                        }
                        else {
                            sendResult(Activity.RESULT_OK,mEditText.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel",null)
                .setNeutralButton("clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK,null);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, String note){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NOTE,note);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
