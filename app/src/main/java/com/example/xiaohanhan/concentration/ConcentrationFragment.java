package com.example.xiaohanhan.concentration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiaohanhan on 2018/4/18.
 */

public class ConcentrationFragment extends Fragment{

    //进度条
    private CircleProgressView mCircleProgressView;
    //设定时间 秒
    private int mTime = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_concentration, container, false);

        mCircleProgressView = v.findViewById(R.id.circle_progress_view);
        mCircleProgressView.setOnClickListener(new View.OnClickListener() {
            Thread timer;
            @Override
            public void onClick(View v) {
                if(timer==null || !timer.isAlive()) {
                    mCircleProgressView.setTotalTime(mTime);
                    timer = new Thread(new ProgressRunable(mTime));
                    timer.start();
                }
            }
        });

        return v;
    }

    class ProgressRunable implements Runnable {
        private int mTotalTime;
        private int mCurrentTime = 0;

        ProgressRunable(int totalTime){
            mTotalTime = totalTime;
        }

        @Override
        public void run() {
            while (mCurrentTime < mTotalTime) {
                mCurrentTime += 1;
                mCircleProgressView.setProgress(mCurrentTime);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(NavUtils.getParentActivityName(getActivity()) != null) {
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        }
    }
}
