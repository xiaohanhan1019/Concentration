package com.example.xiaohanhan.concentration.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.example.xiaohanhan.concentration.Model.Interruption;
import com.example.xiaohanhan.concentration.Model.InterruptionLab;
import com.example.xiaohanhan.concentration.R;

import java.util.List;
import java.util.Locale;

public class InterruptionFragment extends DialogFragment {

    private EditText mAddInterruptName;

    private RecyclerView mInterruptionRecyclerView;
    private InterruptionAdapter mInterruptionAdapter;

    private List<Interruption> mInterruptions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInterruptions = InterruptionLab.get().getInterruptions();

        this.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_interruption,null);
        Window window = getDialog().getWindow();
        if(window!=null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.interrupt_dialog_background));
        }

        mAddInterruptName = view.findViewById(R.id.add_interruption_name);
        mAddInterruptName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Interruption interruption = new Interruption();
                    interruption.setInterruptionName(v.getText().toString());
                    v.setText("");
                    mInterruptions.add(interruption);
                    InterruptionLab.get().dbInsertInterruption(interruption);
                }
                return false;
            }
        });

        ImageButton addInterrupt = view.findViewById(R.id.add_interruption);
        addInterrupt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddInterruptName.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm!=null)
                    imm.showSoftInput(mAddInterruptName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        mInterruptionRecyclerView = view.findViewById(R.id.interruption_recycler_view);
        mInterruptionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

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
            params.width = (int) (dm.widthPixels * 0.6);
            params.height = (int) (dm.heightPixels * 0.4);
            window.setAttributes(params);
        }
    }

    private class InterruptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mInterruptionName;
        private TextView mInterruptTimes;

        private Interruption mInterruption;

        public InterruptionHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_interruption,parent,false));

            itemView.setOnClickListener(this);

            mInterruptionName = itemView.findViewById(R.id.interruption_name);
            mInterruptTimes = itemView.findViewById(R.id.interruption_times);
        }

        public void bind(Interruption interruption){
            mInterruption = interruption;
            mInterruptionName.setText(interruption.getInterruptionName());
            mInterruptTimes.setText(String.format(Locale.getDefault(),"(%d)",interruption.getTimes()));
        }

        @Override
        public void onClick(View v) {
            mInterruption.setTimes(mInterruption.getTimes()+1);
            InterruptionLab.get().dbUpdateInterruption(mInterruption);
            dismiss();
            Toast.makeText(getActivity(),"Reason: "+mInterruption.getInterruptionName(),Toast.LENGTH_SHORT).show();
        }
    }

    private class InterruptionAdapter extends RecyclerView.Adapter<InterruptionHolder>{

        private List<Interruption> mInterruptions;

        public InterruptionAdapter(List<Interruption> interruptions){
            mInterruptions = interruptions;
        }

        @Override
        public InterruptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new InterruptionHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(InterruptionHolder holder, int position) {
            Interruption interruption = mInterruptions.get(position);
            holder.bind(interruption);
        }

        @Override
        public int getItemCount() {
            return mInterruptions.size();
        }
    }

    private void updateUI(){
        if(mInterruptionAdapter==null) {
            mInterruptionAdapter = new InterruptionAdapter(mInterruptions);
            mInterruptionRecyclerView.setAdapter(mInterruptionAdapter);
        } else {
            mInterruptionAdapter.notifyDataSetChanged();
        }
    }
}
