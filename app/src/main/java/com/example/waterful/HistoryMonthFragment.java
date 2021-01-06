package com.example.waterful;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HistoryMonthFragment extends Fragment {
    TextView tvHow, tvAchivPercent, tvDailyIntake;
    BarChart bar3;
    ArrayList<BarEntry> bar3Entry;
    ArrayList<String> bar3EntryLabel;
    BarDataSet bar3DataSet;
    BarData bar3Data;

    String url = "http://192.168.0.102:8080/Waterful/stat/month";
    ContentValues values = new ContentValues();

    SharedPreferences sp, sp2;

    public HistoryMonthFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_history_month,container,false);

        //bar차트 기본 설정
        bar3 = rootview.findViewById(R.id.barChart3);
        bar3Entry = new ArrayList<>();
        bar3EntryLabel = new ArrayList<>();

        //하단 하루 섭취량 표시
        tvHow = rootview.findViewById(R.id.tvHow);
        tvAchivPercent = rootview.findViewById(R.id.tvAchivPercent);
        tvDailyIntake = rootview.findViewById(R.id.tvDailyIntake);


        MainActivity activity = (MainActivity) getActivity();
        sp = activity.getSharedPreferences("WaterfulApp", Context.MODE_PRIVATE);
        sp2 = activity.getSharedPreferences("WaterData", Context.MODE_PRIVATE);

        String ucode = sp.getString("ucode", "");
        values.put("user_ucode", ucode);

        NetworkTask networkTask = new NetworkTask(url, values);
        networkTask.execute();

        //AddValues();
        barEntryLabels();

        bar3DataSet = new BarDataSet(bar3Entry, "섭취량");
        bar3Data = new BarData(bar3EntryLabel, bar3DataSet);
        bar3DataSet.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        XAxis xAxis = bar3.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark)); // X축 텍스트컬러설정
        xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.colorWhite)); // X축 줄의 컬러 설정

        YAxis yAxisLeft = bar3.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)); //Y축 텍스트 컬러 설정
        yAxisLeft.setGridColor(ContextCompat.getColor(getContext(), R.color.colorWhite)); // X축 줄의 컬러 설정

        YAxis yAxisRight = bar3.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        bar3.setData(bar3Data);
        bar3.animateY(3000);
        //Chart end

        return rootview;

    }

    //임의로 막대 그래프 값을 넣음
    //BarEntry 값에 따라 y축 단위와 최대값이 변함
    public void AddValues(){
        bar3Entry.add(new BarEntry(1002f, 0));
        bar3Entry.add(new BarEntry(1800f, 1));
        bar3Entry.add(new BarEntry(500f, 2));
        bar3Entry.add(new BarEntry(0f, 3));
        bar3Entry.add(new BarEntry(800f, 4));
        bar3Entry.add(new BarEntry(100f, 5));
        bar3Entry.add(new BarEntry(1100f, 6));
        bar3Entry.add(new BarEntry(0f, 7));
        bar3Entry.add(new BarEntry(1352f, 8));
        bar3Entry.add(new BarEntry(1800f, 9));
        bar3Entry.add(new BarEntry(2000f, 10));
        bar3Entry.add(new BarEntry(0f, 11));
    }

    //x축
    public void barEntryLabels(){
        bar3EntryLabel.add("Jan");
        bar3EntryLabel.add("Feb");
        bar3EntryLabel.add("Mar");
        bar3EntryLabel.add("Apr");
        bar3EntryLabel.add("May");
        bar3EntryLabel.add("Jun");
        bar3EntryLabel.add("Jul");
        bar3EntryLabel.add("Aug");
        bar3EntryLabel.add("Sep");
        bar3EntryLabel.add("Oct");
        bar3EntryLabel.add("Nov");
        bar3EntryLabel.add("Dec");
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

            for (int i = 0; i < result.length(); i++) {
                try {
                    obj = result.getJSONObject(i);
                    bar3Entry.add(new BarEntry(Float.parseFloat(obj.getString("sum")), i));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}