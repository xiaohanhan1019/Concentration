package com.example.xiaohanhan.concentration.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.xiaohanhan.concentration.Model.ConcentrationRecord;
import com.example.xiaohanhan.concentration.R;
import com.example.xiaohanhan.concentration.Util.MySQLHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodayFragment extends Fragment {

    private PieChart mChart;
    private ImageButton mArrowBack;
    private ImageButton mShare;
    private List<ConcentrationRecord> mConcentrationRecords;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConcentrationRecords = new ArrayList<>();
        SelectTodayRecord selectTodayRecord = new SelectTodayRecord();
        try {
            mConcentrationRecords = selectTodayRecord.execute().get();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);

        mArrowBack = v.findViewById(R.id.today_arrow_back);
        mArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.goback_left_to_right,R.anim.goback_right_to_left);
            }
        });

        mShare = v.findViewById(R.id.today_share);
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(Intent.ACTION_SEND);//调用系统的ACTION_SEND
                imageIntent.setType("image/png");
                startActivity(Intent.createChooser(imageIntent, "分享"));
            }
        });

        //计算piechart数据
        Map<String,Integer> chartData = new HashMap<>();
        List<PieEntry> entries = new ArrayList<>();

        float sum=0;

        for(ConcentrationRecord record :mConcentrationRecords){
            if(chartData.containsKey(record.getTaskName())) {
                int temp=chartData.get(record.getTaskName());
                temp+=record.getWorkingtime();
                chartData.put(record.getTaskName(), temp);
                sum+=record.getWorkingtime();
            }
            else{
                chartData.put(record.getTaskName(),record.getWorkingtime());
                sum+=record.getWorkingtime();
            }
        }

        for(Map.Entry<String, Integer> entry : chartData.entrySet()){
            entries.add(new PieEntry(entry.getValue()/sum,entry.getKey()));
        }

        //显示
        mChart = v.findViewById(R.id.today_piechart);
        mChart.getDescription().setEnabled(false);
        mChart.setUsePercentValues(true);
        mChart.setExtraOffsets(5, 10, 5, 5);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextColor(Color.WHITE);
        l.setTextSize(14);

        mChart.setCenterText(getText(sum));
        mChart.setCenterTextSize(32);
        mChart.setCenterTextColor(getResources().getColor(R.color.colorOrangeRed));

        PieDataSet dataSet = new PieDataSet(entries, null);
        int[] colors = {
                Color.rgb(51,132,210),
                Color.rgb(84,198,95),
                Color.rgb(237,186,49),
                Color.rgb(222,53,46),
                Color.rgb(173,86,198)
        };

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        mChart.setData(data);
        mChart.invalidate();

        return v;
    }

    String getText(float sum){
        int time=(int)sum/60;
        if(time==1)
            return "1 min";
        else
            return time+"mins";
    }

    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    private static class SelectTodayRecord extends AsyncTask<Void,Void,List<ConcentrationRecord>> {

        @Override
        protected List<ConcentrationRecord> doInBackground(Void... voids) {
            MySQLHelper mySQLHelper = new MySQLHelper();
            try {
                return mySQLHelper.getTodayConcentrationRecord();
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

    }
}
