package com.example.waterful;

/* 업적 페이지 - 내 업적 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class QuestFragment extends Fragment {
private View view;
RecyclerView recyclerViewQ;
TextView tvQuestProgress,tvUserName;
ProgressBar pbQuestProgress;
ImageView ivComplete;

    String url = "http://192.168.0.102:8080/Waterful/achievement";
    ContentValues values = new ContentValues();

    SharedPreferences sp,sp3;
    public static final String MyPREFERENCES = "WaterfulApp";
    public static final String MyPREFERENCES3 = "Achiv";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quest, container, false);

        sp = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sp3 = getActivity().getSharedPreferences(MyPREFERENCES3, Context.MODE_PRIVATE);

        tvUserName = view.findViewById(R.id.tvUserName);
        String nickname = sp.getString("nickname", "");
        tvUserName.setText(nickname);


        //총 업적 진행도 부분 추가해야 함

        //RecyclerView 구현 코드
        recyclerViewQ = view.findViewById(R.id.recyclerViewQ);
        recyclerViewQ.setHasFixedSize(true);
        recyclerViewQ.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<QuestInfo> questInfoArrayList = new ArrayList<>();

        values.put("user_ucode",sp.getString("ucode",""));
        NetworkTask networkTask = new NetworkTask(url, values);
        networkTask.execute();

        int index=11; //이 앱의 총 업적 개수는 11개
        for(int i=0; i < index; i++ ){
            questInfoArrayList.add(new QuestInfo(sp3.getString("achiv_about",""), sp3.getInt("achiv_point",0)));
        }
        /*questInfoArrayList.add(new QuestInfo("업적포인트 100 달성", "10point"));
        questInfoArrayList.add(new QuestInfo("업적포인트 300 달성", "30point"));
        questInfoArrayList.add(new QuestInfo("업적포인트 500 달성", "50point"));

        questInfoArrayList.add(new QuestInfo("첫번째 물마시기", "5point"));
        questInfoArrayList.add(new QuestInfo("3,000ml 마시기", "10point"));
        questInfoArrayList.add(new QuestInfo("5,000ml 마시기", "20point"));
        questInfoArrayList.add(new QuestInfo("10,000ml 마시기", "30point"));
        questInfoArrayList.add(new QuestInfo("처음으로 권장량 섭취", "5point"));
        questInfoArrayList.add(new QuestInfo("권장량보다 77ml 더 마시기", "77point"));

        questInfoArrayList.add(new QuestInfo("몸무게 변경하기", "3point"));
        questInfoArrayList.add(new QuestInfo("닉네임 변경하기", "3point"));*/

        QuestAdapter questAdapter = new QuestAdapter(questInfoArrayList);

        recyclerViewQ.setAdapter(questAdapter);

        //성취 완료 된 경우 체크처리
        if(sp.getInt("array_size",0)<11 ) {

            ivComplete = view.findViewById(R.id.ivComplete);
            ivComplete.setVisibility(View.VISIBLE);
        }

        //성취 진행도 프로그레스바 조절
        tvQuestProgress = view.findViewById(R.id.tvQuestProgress);
        pbQuestProgress = view.findViewById(R.id.pbQuestProgress);
       // tvQuestProgress.setText();
        int value = Integer.parseInt(tvQuestProgress.getText().toString().replaceAll("[^0-9]", "")); // 00% 중 숫자(00)만 추출
        pbQuestProgress.setProgress(value);

        return view;

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

            JSONArray array = null;
            JSONArray array2 = null;
            JSONObject obj, obj2, obj3;

            try {
                obj = result.getJSONObject(1);
                array.put(obj.getString("noAchiv"));
                array2.put(obj.getString("achiv"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = sp3.edit();

            int i;
            for(i=0; i < array.length(); i++){
                try {

                    obj2 = array.getJSONObject(i);
                    editor.putString("achiv_about", obj2.getString("achiv_about"));
                    editor.putInt("achiv_point", obj2.getInt("achiv_point"));
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for(int j=i ; j < array2.length(); j++){
                try {

                    obj3 = array2.getJSONObject(i);
                    editor.putString("achiv_about", obj3.getString("achiv_about"));
                    editor.putInt("achiv_point", obj3.getInt("achiv_point"));
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            editor.putInt("array_size",array.length());
            editor.commit();
            return null;
        }
    }
}