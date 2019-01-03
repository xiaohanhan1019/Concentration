package com.example.xiaohanhan.concentration.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SloganLab {

    private static SloganLab sSloganLab; 
    
    private static List<String> mSlogans;

    public static SloganLab get(){
        if(sSloganLab == null){
            sSloganLab = new SloganLab();
        }
        return sSloganLab;
    }

    public SloganLab() {
        mSlogans = new ArrayList<>();
        mSlogans.add("Whatever is worth doing is worth doing well.");
        mSlogans.add("Keep on going never give up.");
        mSlogans.add("Action speak louder than words.");
        mSlogans.add("Never put off what you can do today until tomorrow.");
        mSlogans.add("You cannot improve your past, but you can improve your future. Once time is wasted, life is wasted.");
        mSlogans.add("Don't aim for success if you want it; just do what you love and believe in, and it will come naturally.");
        mSlogans.add("Keep good men company and you shall be of the number.");
        mSlogans.add("Knowledge makes humble, ignorance makes proud.");
        mSlogans.add("If you are doing your best,you will not have to worry about failure.");
        mSlogans.add("We improve ourselves by victories over ourselves. There must be contests, and we must win.");
        mSlogans.add("A man is not old as long as he is seeking something. A man is not old until regrets take the place of dreams.");
    }

    public String getRadomSlogan(){
        Random random = new Random();
        int idx=Math.abs(random.nextInt())%mSlogans.size();
        return mSlogans.get(idx);
    }

}
