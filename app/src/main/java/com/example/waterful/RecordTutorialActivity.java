package com.example.waterful;
// 기록페이지의 튜토리얼페이지가 이곳에서 구현됨

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class RecordTutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_tutorial);
        ViewPager pager = findViewById(R.id.recordPager);
        //캐싱을 해놓을 프래그먼트 개수
        pager.setOffscreenPageLimit(2);

        //프래그먼트 참조가능
        RecordTutorialActivity.MoviePagerAdapter adapter = new RecordTutorialActivity.MoviePagerAdapter(getSupportFragmentManager());

        RecordTutorial1 record1 = new RecordTutorial1();
        adapter.addItem(record1);

        RecordTutorial2 record2 = new RecordTutorial2();
        adapter.addItem(record2);

        pager.setAdapter(adapter);
    }

    //어댑터 안에서 각각의 아이템을 데이터로 관리
    class MoviePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<>();

        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item){
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}