package com.example.waterful;
// day 첫번째 차트
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class DayfirstSlideActivity extends Fragment {
    BarChart bar;
    ArrayList<BarEntry> barEntry;
    ArrayList<String> barEntryLabel;
    BarDataSet barDataSet;
    BarData barData;

    String url = "http://192.168.0.102:8080/Waterful/stat/day";
    ContentValues values = new ContentValues();

    SharedPreferences sp, sp2;

    public DayfirstSlideActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.dayfirst_slide, container, false);

        //bar차트 기본 설정
        bar = rootview.findViewById(R.id.barChart1);
        barEntry = new ArrayList<>();   //x축 하나 당 들어가는 값
        barEntryLabel = new ArrayList<>();

        MainActivity activity = (MainActivity) getActivity();
        sp = activity.getSharedPreferences("WaterfulApp", Context.MODE_PRIVATE);
        sp2 = activity.getSharedPreferences("WaterData", Context.MODE_PRIVATE);

        String ucode = sp.getString("ucode", "");
        values.put("user_ucode", ucode);

        NetworkTask networkTask = new NetworkTask(url, values);
        networkTask.execute();

        //AddValues();
        barEntryLabels();

        barDataSet = new BarDataSet(barEntry, "섭취량");  //BarDataSet : 그래프 그룹의 이름, 하단에 위치. entry를 넣고 그래프 선의 색을 설정할 수 있음
        barData = new BarData(barEntryLabel, barDataSet); //barEntryLabel : x축 레이블값
        barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary)); //그래프 그룹 색 선택

        //x축 설정
        XAxis xAxis = bar.getXAxis(); // x축 속성 설정 (위치, 색상 등). x축에 대한 정보를 view에서 가져온다
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)); // X축 텍스트컬러설정
        xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.colorWhite)); // X축 줄의 컬러 설정

        YAxis yAxisLeft = bar.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)); //Y축 텍스트 컬러 설정
        yAxisLeft.setGridColor(ContextCompat.getColor(getContext(), R.color.colorWhite)); // X축 줄의 컬러 설정

        YAxis yAxisRight = bar.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        bar.setData(barData);
        bar.animateY(3000); //그래프 그릴 시 y축으로 애니메이션
        //Chart end

        return rootview;
    }

    //임의로 막대 그래프 값을 넣음
    public void AddValues() {

        for(int i= 0; i < 15; i++){
            barEntry.add(new BarEntry(1002, i));
        }
        /*barEntry.add(new BarEntry(1002, 0));
        barEntry.add(new BarEntry(1350, 1));
        barEntry.add(new BarEntry(1270, 2));
        barEntry.add(new BarEntry(1500, 3));
        barEntry.add(new BarEntry(1183, 4));
        barEntry.add(new BarEntry(1723, 5));
        barEntry.add(new BarEntry(1925, 6));
        barEntry.add(new BarEntry(1486, 7));
        barEntry.add(new BarEntry(1547, 8));
        barEntry.add(new BarEntry(1489, 9));
        barEntry.add(new BarEntry(1578, 10));
        barEntry.add(new BarEntry(1645, 11));
        barEntry.add(new BarEntry(1590, 12));
        barEntry.add(new BarEntry(1498, 13));
        barEntry.add(new BarEntry(1579, 14));*/


    }

    //x축
    public void barEntryLabels() {

        for (int i = 0; i < 15; i++) {
            barEntryLabel.add(String.valueOf(i));
        }
    }

    public class NetworkTask extends AsyncTask<JSONArray, Void, Void> {

        String url;
        ContentValues values;

        NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected Void doInBackground(JSONArray... jsonArrays) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            JSONArray result = requestHttpURLConnection.request(url, values);

            if(result==null)
                return null;

            JSONObject obj;
            for(int i=0; i < 15; i++){
                try {
                    obj = result.getJSONObject(i);
                    barEntry.add(new BarEntry(Float.parseFloat(obj.getString("sum")), i));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}