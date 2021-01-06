package com.example.waterful;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;

public class Intro0Activity extends AppCompatActivity {

    //intro1,2,3, recordTutorial1,2를 여기로 불러와서 화면에 보여줌
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro0);
        ViewPager pager = findViewById(R.id.pager);
        //캐싱을 해놓을 프래그먼트 개수
        pager.setOffscreenPageLimit(5);

        //프래그먼트 참조가능
        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());

        Intro1Activity intro1 = new Intro1Activity();
        adapter.addItem(intro1);

        Intro2Activity intro2 = new Intro2Activity();
        adapter.addItem(intro2);

        RecordTutorial1 intro3 = new RecordTutorial1();
        adapter.addItem(intro3);

        RecordTutorial2 intro4 = new RecordTutorial2();
        adapter.addItem(intro4);

        Intro3Activity intro5 = new Intro3Activity();
        adapter.addItem(intro5);

        pager.setAdapter(adapter);
    }

    //어댑터 안에서 각각의 아이템을 데이터로 관리
    class MoviePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

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