package com.example.waterful;
//  day 최종 구현
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;


public class HistoryDayFragment extends Fragment {
    TextView tvHow, tvAchivPercent, tvDailyIntake;
    ViewPager2 DayViewPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 2;   // 프레그먼트 사용할 갯수

    SharedPreferences sp, sp2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_history_day, container,false);
        // viewPager2
        DayViewPager = rootview.findViewById(R.id.pager);

        //하단 하루 섭취량 표시
        tvHow = rootview.findViewById(R.id.tvHow);
        tvAchivPercent = rootview.findViewById(R.id.tvAchivPercent);
        tvDailyIntake = rootview.findViewById(R.id.tvDailyIntake);

        MainActivity activity = (MainActivity)getActivity();
        sp = activity.getSharedPreferences("WaterfulApp",Context.MODE_PRIVATE);
        sp2 = activity.getSharedPreferences("WaterData", Context.MODE_PRIVATE);

        String recommend = String.valueOf(sp.getInt("recommend",0));
        String per_day_water = String.valueOf(sp2.getInt("per_day_water",0));
        int percent = (int) (Double.parseDouble(per_day_water)/ Double.parseDouble(recommend) * 100.0);
        tvAchivPercent.setText(String.valueOf(percent)+"%");

        if(percent < 50)
            tvHow.setText("부족하군요");
        else
            tvHow.setText("충분하군요");

        tvDailyIntake.setText(per_day_water+"ml");

        //Adapter
        pagerAdapter = new DayAdapter(this, num_page);
        DayViewPager.setAdapter(pagerAdapter);

        //viewPager 세팅
        DayViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        DayViewPager.setCurrentItem(2);    //처음 보여지는 위치를 1로 정함
        DayViewPager.setOffscreenPageLimit(1);

        DayViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    DayViewPager.setCurrentItem(position);
                }
            }
        });
        return rootview;
    }
}