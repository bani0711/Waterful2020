package com.example.waterful;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DayAdapter extends FragmentStateAdapter {

    public  int mCount;
    //mCount : 생성할 프래그먼트 개수가 들어옴

    public DayAdapter(HistoryDayFragment historyDayFragment, int count){
        super(historyDayFragment);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==1) return new DaysecondSlideActivity();
            // else if(index==1) return new SecondFragment();
        else return new DayfirstSlideActivity();
    }

    @Override
    public int getItemCount() {
        return 2;   //프래그먼트 3화면까지만 슬라이딩이 가능
    }

    //position에서 프래그먼트 갯수로 나눈 나머지값이 진짜 position
    public int getRealPosition(int position) { return position % mCount; }
}
