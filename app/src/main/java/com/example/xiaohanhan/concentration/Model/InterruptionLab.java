package com.example.xiaohanhan.concentration.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohanhan on 2018/4/28.
 */

public class InterruptionLab {

    private static InterruptionLab sInterruptionLab;

    private List<Interruption> mInterruptions;

    public static InterruptionLab get(Context context){
        if(sInterruptionLab == null){
            sInterruptionLab = new InterruptionLab(context);
        }
        return sInterruptionLab;
    }

    private InterruptionLab (Context context){
        mInterruptions = new ArrayList<>();
        for(int i=0;i<3;i++){
            Interruption interruption = new Interruption();
            mInterruptions.add(interruption);
        }
    }

    public List<Interruption> getInterruptions(){
        return mInterruptions;
    }

}
