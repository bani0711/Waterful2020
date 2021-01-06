package com.example.waterful;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.color.white;


public class HistoryFragment extends Fragment {
    private View view;
    Button btnDay, btnWeek, btnMonth;

    //    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_history, container, false);

        //처음 보일 fragment 지정
        getFragmentManager().beginTransaction().add(R.id.childFragment, new HistoryDayFragment()).commit();

        //하위 버튼
        btnDay = view.findViewById(R.id.btnDay);
        btnWeek = view.findViewById(R.id.btnWeek);
        btnMonth = view.findViewById(R.id.btnMonth);

        //버튼 색지정
        final Drawable select = getResources().getDrawable(R.drawable.edt_border);
        final int colorPrimary = getResources().getColor(R.color.colorPrimary);
        final int colorPrimaryLight = getResources().getColor(R.color.colorPrimaryLight);

        //일, 주, 월 버튼이 눌렸을 때 이벤트
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //첫번째 버튼
                    case R.id.btnDay:
                        getFragmentManager().beginTransaction().replace(R.id.childFragment, new HistoryDayFragment()).commit();
                        btnDay.setText("Day");
                        btnDay.setBackground(select);
                        btnDay.setTextColor(colorPrimary);
                        btnWeek.setText("W");
                        btnWeek.setBackgroundColor(colorPrimary);
                        btnWeek.setTextColor(getResources().getColor(white));
                        btnMonth.setText("M");
                        btnMonth.setBackgroundColor(colorPrimary);
                        btnMonth.setTextColor(getResources().getColor(white));
                        break;
                    case R.id.btnWeek:
                        getFragmentManager().beginTransaction().replace(R.id.childFragment, new HistoryWeekFragment()).commit();
                        btnWeek.setText("Week");
                        btnWeek.setBackground(select);
                        btnWeek.setTextColor(colorPrimary);
                        btnDay.setText("D");
                        btnDay.setBackgroundColor(colorPrimary);
                        btnDay.setTextColor(getResources().getColor(white));
                        btnMonth.setText("M");
                        btnMonth.setBackgroundColor(colorPrimary);
                        btnMonth.setTextColor(getResources().getColor(white));
                        break;
                    case R.id.btnMonth:
                        getFragmentManager().beginTransaction().replace(R.id.childFragment, new HistoryMonthFragment()).commit();
                        btnMonth.setText("Month");
                        btnMonth.setBackground(select);
                        btnMonth.setTextColor(colorPrimary);
                        btnWeek.setText("W");
                        btnWeek.setBackgroundColor(colorPrimary);
                        btnWeek.setTextColor(getResources().getColor(white));
                        btnDay.setText("D");
                        btnDay.setBackgroundColor(colorPrimary);
                        btnDay.setTextColor(getResources().getColor(white));
                        break;
                }
            }
        };
        btnDay.setOnClickListener(onClickListener);
        btnWeek.setOnClickListener(onClickListener);
        btnMonth.setOnClickListener(onClickListener);

        return view;
    }
}