package com.example.waterful;
// day 두번째 차트
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

public class DaysecondSlideActivity extends Fragment {
    BarChart bar2;
    ArrayList<BarEntry> bar2Entry;
    ArrayList<String> bar2EntryLabel;
    BarDataSet bar2DataSet;
    BarData bar2Data;
    Calendar cal = Calendar.getInstance();
    int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    String url = "http://192.168.0.102:8080/Waterful/stat/day";
    ContentValues values = new ContentValues();

    SharedPreferences sp, sp2;

    public DaysecondSlideActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.daysecond_slide, container, false);

        //bar차트 기본 설정
        bar2 = rootView.findViewById(R.id.barChart2);
        bar2Entry = new ArrayList<>();
        bar2EntryLabel = new ArrayList<>();

        MainActivity activity = (MainActivity) getActivity();
        sp = activity.getSharedPreferences("WaterfulApp", Context.MODE_PRIVATE);
        sp2 = activity.getSharedPreferences("WaterData", Context.MODE_PRIVATE);

        String ucode = sp.getString("ucode", "");
        values.put("user_ucode", ucode);

        NetworkTask networkTask = new NetworkTask(url, values);
        networkTask.execute();

        //AddValues();
        barEntryLabels();

        bar2DataSet = new BarDataSet(bar2Entry, "섭취량");
        bar2Data = new BarData(bar2EntryLabel, bar2DataSet);
        bar2DataSet.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));

        XAxis xAxis = bar2.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark)); // X축 텍스트컬러설정
        xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.colorWhite)); // X축 줄의 컬러 설정

        YAxis yAxisLeft = bar2.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)); //Y축 텍스트 컬러 설정
        yAxisLeft.setGridColor(ContextCompat.getColor(getContext(), R.color.colorWhite)); // X축 줄의 컬러 설정

        YAxis yAxisRight = bar2.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        bar2.setData(bar2Data);
        bar2.animateY(3000); //그래프 그릴 시 y축으로 애니메이션

        return rootView;
    }
    //임의로 막대 그래프 값을 넣음
    //BarEntry 값에 따라 y축 단위와 최대값이 변함
    public void AddValues(){

        for(int i= 0; i < lastDay-15; i++){
            bar2Entry.add(new BarEntry(1002, i));
        }
    }

    //x축
    public void barEntryLabels(){

        for(int i= 16; i<= lastDay; i++){
            bar2EntryLabel.add(String.valueOf(i));
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

            if (result == null)
                return null;

            JSONObject obj;

            for (int i = 0; i < lastDay-15; i++) {
                try {
                    obj = result.getJSONObject(i);
                    bar2Entry.add(new BarEntry(Float.parseFloat(obj.getString("sum")), i));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}