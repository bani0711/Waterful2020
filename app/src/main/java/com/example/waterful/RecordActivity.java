package com.example.waterful;

/* 메인 페이지 - 기록 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    RecyclerView recyclerViewRecord;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<RecordInfo> recordInfoArrayList = new ArrayList<>();
    RecordAdapter recordAdapter = new RecordAdapter(recordInfoArrayList);
    Toolbar toolbarRecord;
    ActionBar actionBar;
    TextView tvRecordDate, tvRecordIntake;

    String url = "http://192.168.0.103:8080/Waterful/drinking/record";
    ContentValues values = new ContentValues();

    SharedPreferences sp, sp2;
    public static final String MyPREFERENCES = "WaterfulApp";
    public static final String MyPREFERENCES2 = "WaterData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sp2 = getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);

        //Toolbar 구현 코드
        toolbarRecord = findViewById(R.id.toolbarRecord);
        setSupportActionBar(toolbarRecord);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //뒤로가기 아이콘
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24);

        //RecyclerView 구현 코드
        recyclerViewRecord = findViewById(R.id.recyclerViewRecord);
        recyclerViewRecord.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewRecord.setLayoutManager(layoutManager);

        tvRecordDate = findViewById(R.id.tvRecordDate);
        tvRecordIntake = findViewById(R.id.tvRecordIntake);

        values.put("user_ucode", sp.getString("ucode",""));
        NetworkTask networkTask = new NetworkTask(url, values);
        networkTask.execute();

        //구현된 RecyclerView에 개체 추가하기
        int index = sp2.getInt("array_size",0);
        for (int i = 0 ; i < index; i++) {
            recordInfoArrayList.add(new RecordInfo(sp2.getString("cur_time"+i, ""), sp2.getInt("drinking_water"+i, 0) + "ml"));
            recordAdapter.notifyItemInserted(i);
        }

        //어댑터 적용
        recyclerViewRecord.setAdapter(recordAdapter);

        //스와이프 삭제하기 위한 itemTouchHelper 객체 생성
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewRecord);
    }

    //스와이프 삭제 기능 구현
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //onSwiped(스와이프) 했을 때 remove(삭제) 하기
            final int position = viewHolder.getAdapterPosition();
            recordInfoArrayList.remove(position);
            recordAdapter.notifyItemRemoved(position);
            values.put("cur_time", sp2.getString("cur_time"+position,""));
            url = "http://192.168.0.103:8080/Waterful/drinking/remove";
            NetworkTask networkTask = new NetworkTask(url, values);
            networkTask.execute();
        }
    };

    //툴바 뒤로가기 버튼 동작 메서드
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                //메인화면으로 돌아가도록
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
            SharedPreferences.Editor editor = sp2.edit();

            editor.putInt("array_size", result.length());

            for (int i = 0; i < result.length(); i++) {
                try {
                    obj = result.getJSONObject(i);

                    editor.putString("cur_time" + i, obj.getString("cur_time"+i));
                    editor.putInt("drinking_water" + i, obj.getInt("drinking_water"+i));
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            editor.commit();
            return null;
        }

    }
}