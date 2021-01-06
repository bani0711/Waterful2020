package com.example.waterful;

/* 네비게이션 바 구현 페이지 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MainFragment mainFragment;
    private QuestFragment questFragment;
    private HistoryFragment historyFragment;
    private SettingFragment settingFragment;
    View view;
    private long time = 0;

    String url = "http://192.168.0.102:8080/Waterful/drinking";
    ContentValues values = new ContentValues();
    ContentValues values2 = new ContentValues();

    SharedPreferences sp, sp2, sp3;
    public static final String MyPREFERENCES = "WaterfulApp";
    public static final String MyPREFERENCES2 = "WaterData";
    public static final String MyPREFERENCES3 = "Achiv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sp2 = getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);
        sp3 = getSharedPreferences(MyPREFERENCES3, Context.MODE_PRIVATE);

        //포그라운드 실행
        onStartForegroundService(view);

        //아이콘 클릭에 따라 프래그먼트 바뀌는 것 구현(네비게이션바 아이콘 클릭에 따라)
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemMain:
                        setFrag(0);
                        break;
                    case R.id.itemQuest:
                        setFrag(1);
                        break;
                    case R.id.itemHistory:
                        setFrag(2);
                        break;
                    case R.id.itemSetting:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        mainFragment = new MainFragment();
        questFragment = new QuestFragment();
        historyFragment = new HistoryFragment();
        settingFragment = new SettingFragment();
        setFrag(0); //첫 프래그먼트 화면 지정

        //블루투스 수신 이벤트
        BluetoothSPP bt3 = ((Device1Activity) Device1Activity.context).bt;
        bt3.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                // 최초실행 판단
                int w = sp2.getInt("pre_drinking_water", -1);
                int water = Integer.parseInt(message);
                //최초실행 일 때
                if (w == -1) {
                    SharedPreferences.Editor editor = sp2.edit();
                    editor.putInt("pre_drinking_water", water);
                    editor.commit();
                }
                //최초실행이 아닐 때
                else {
                    int pre_water = sp2.getInt("pre_drinking_water", 0); // 키값으로 꺼냄, 두번째 인수는 앱 처음 시작할 때 디폴트 값

                    //물이 추가 되었을 때
                    if ((pre_water - water) < 0) {
                        SharedPreferences.Editor editor = sp2.edit();
                        editor.putInt("pre_drinking_water", water);
                        editor.commit();
                    } else {
                        int drinking_water = pre_water - water;

                        SharedPreferences.Editor editor = sp2.edit();
                        editor.putInt("pre_drinking_water", water);
                        editor.apply();
                        values.put("drinking_water", drinking_water);

                        String ucode = sp.getString("ucode", "");
                        values.put("user_ucode", ucode);
                        try {
                            //현재시간 가져옴
                            long now = System.currentTimeMillis();
                            Date date = new Date(now); //Date 형식으로 고침

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            final String getTime = simpleDateFormat.format(date);
                            values.put("cur_time", URLEncoder.encode(getTime, "UTF-8"));

                            editor.putInt("drinking_water", drinking_water);
                            editor.commit();

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        //섭취량 가져오기
                        int perDay = sp2.getInt("per_day_water", -1);
                        if (perDay == -1) { //하루 처음 실행일 때 섭취량 가져오기
                            MainFragment mf = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFrame);
                            String str = mf.getFragmentText();

                            // AsyncTask를 통해 HttpURLConnection 수행
                            NetworkTask networkTask = new NetworkTask(url, values);
                            networkTask.execute(str);
                        } else { //하루 처음 실행 아닐 때 섭취량 가져오기

                            // AsyncTask를 통해 HttpURLConnection 수행
                            NetworkTask networkTask = new NetworkTask(url, values);
                            networkTask.execute(String.valueOf(perDay));
                        }

                        //물 섭취 성취 달성 확인
                        if (sp3.getString("d_done", "섭취미완") == "섭취미완") {
                            url = "http://192.168.0.102:8080/Waterful/achievement/water";
                            values2.put("user_ucode", ucode);
                            NetworkTask2 networkTask2 = new NetworkTask2(url, values2);
                            networkTask2.execute();
                        }

                        //권장량 섭취 성취 달성 확인
                        if(sp3.getString("d56_done","권장량미완")=="권장량미완"){
                            if(sp2.getInt("per_day_water",0) >= sp.getInt("recommend",0)){
                                url = "http://192.168.0.102:8080/Waterful/achievement/recommend";
                                values2.put("user_ucode", ucode);
                                NetworkTask3 networkTask3 = new NetworkTask3(url, values2);
                                networkTask3.execute();
                            }
                        }
                    }

                }
            }
        });

    } //onCreate()

    //프래그먼트 교체 메서드
    private void setFrag(int n) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (n) {
            case 0:
                fragmentTransaction.replace(R.id.mainFrame, mainFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.mainFrame, questFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.mainFrame, historyFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentTransaction.replace(R.id.mainFrame, settingFragment);
                fragmentTransaction.commit();
                break;
        } //switch
    } //setFrag()

    public void onStartForegroundService(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction("startForeground");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }


    //뒤로가기 2번 누르면 어플 종료하기
    @Override
    public void onBackPressed() {

        //현재 시간에서 마지막으로 뒤로가기 버튼을 눌렀던 시간을 뺀 값이 2초 이상일 때
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) { //2초 이하면 종료
            ActivityCompat.finishAffinity(this);
        }
    }

    //메인페이지에 섭취량 보여주기
    public class NetworkTask extends AsyncTask<String, Void, String> {

        String url;
        ContentValues values;

        NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            requestHttpURLConnection.request(url, values);

            //MainFragment에 섭취량 기록
            int drinking_water = sp2.getInt("drinking_water", -1);
            int water = Integer.parseInt(strings[0]);
            strings[0] = (String.valueOf(water + drinking_water));

            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {

            SharedPreferences.Editor editor = sp2.edit();
            int water = Integer.parseInt(s);
            editor.putInt("per_day_water", water);
            editor.commit();

            MainFragment mf = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFrame);
            mf.setFragmentText();

        }
    }

    //성취 달성 판단하기
    public class NetworkTask2 extends AsyncTask<JSONArray, Void, Void> {

        String url;
        ContentValues values;

        NetworkTask2(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected Void doInBackground(JSONArray... jsonArrays) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            JSONArray result = requestHttpURLConnection.request(url, values);

            JSONObject obj;
            try {
                obj = result.getJSONObject(0);
                String state = obj.getString("state");
                if(state=="섭취완료"){
                    SharedPreferences.Editor editor = sp3.edit();
                    editor.putString("d_done",state);
                    editor.commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //성취 달성 판단하기
    public class NetworkTask3 extends AsyncTask<JSONArray, Void, Void> {

        String url;
        ContentValues values;

        NetworkTask3(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected Void doInBackground(JSONArray... jsonArrays) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            JSONArray result = requestHttpURLConnection.request(url, values);

            JSONObject obj;
            try {
                obj = result.getJSONObject(0);
                String state = obj.getString("state2");
                if(state=="완료"){
                    SharedPreferences.Editor editor = sp3.edit();
                    editor.putString("d56_done",state);
                    editor.commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}