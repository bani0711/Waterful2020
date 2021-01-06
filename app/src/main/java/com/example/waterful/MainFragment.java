package com.example.waterful;

/* 메인 페이지 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import me.itangqi.waveloadingview.WaveLoadingView;


public class MainFragment extends Fragment {
private View view;
WaveLoadingView waveLoadingView;
EditText edtIntake;
TextView tvUserNickName;

    SharedPreferences sp, sp2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        MainActivity activity = (MainActivity)getActivity();
        sp = activity.getSharedPreferences("WaterfulApp",Context.MODE_PRIVATE);
        sp2 = activity.getSharedPreferences("WaterData",Context.MODE_PRIVATE);

        //닉네임 preferences에서 불러오기
        tvUserNickName = view.findViewById(R.id.tvUserNickName);
        String nickname = sp.getString("nickname", "");
        tvUserNickName.setText(nickname);

        //음수량 프로그레스바 설정
        waveLoadingView = view.findViewById(R.id.waveLoadingView);
        waveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        waveLoadingView.setAnimDuration(3000);
        edtIntake = view.findViewById(R.id.edtIntake);

        int water = sp2.getInt("per_day_water", -1);
        if(water!=-1){
            edtIntake.setText(String.valueOf(water));
            //섭취량 edtIntake에서 값 얻어와서 백분율 계산하기 (일부값/전체값*100) -> 결과값에 따라 프로그레스바 높이가 달라짐
            String part = edtIntake.getText().toString();
            String total = waveLoadingView.getTopTitle().replaceAll("[^0-9]", ""); //숫자만 추출 (2000ml 이면 2000만 추출)
            int intake = (int)(Double.parseDouble(part) / Double.parseDouble(total) * 100.0);
            waveLoadingView.setProgressValue(intake);

            //100% 이상이면 걍 100으로 출력되게
            if(waveLoadingView.getProgressValue() >= 100) {
                waveLoadingView.setCenterTitle("100%");
            } else {
                waveLoadingView.setCenterTitle(intake + "%");
            }

            //음수량 프로그레스바 양 얻어오기
            int pValue = waveLoadingView.getProgressValue();

            //음수량 프로그레스바가 일정 높이 이상이면 안의 텍스트 색상을 변경함
            if(pValue >= 60) {
                waveLoadingView.setCenterTitleColor(Color.WHITE);
            }
            if(pValue >= 100) {
                waveLoadingView.setTopTitleColor(Color.WHITE);
            }
        }

        //권장량
        int recommend = sp.getInt("recommend",0);
        waveLoadingView.setTopTitle(recommend + " ml");

        //입력 값 변화 이벤트
        edtIntake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //섭취량 edtIntake에서 값 얻어와서 백분율 계산하기 (일부값/전체값*100) -> 결과값에 따라 프로그레스바 높이가 달라짐
                String part = edtIntake.getText().toString();
                String total = waveLoadingView.getTopTitle().replaceAll("[^0-9]", ""); //숫자만 추출 (2000ml 이면 2000만 추출)
                int intake = (int)(Double.parseDouble(part) / Double.parseDouble(total) * 100.0);
                waveLoadingView.setProgressValue(intake);

                //100% 이상이면 걍 100으로 출력되게
                if(waveLoadingView.getProgressValue() >= 100) {
                    waveLoadingView.setCenterTitle("100%");
                } else {
                    waveLoadingView.setCenterTitle(intake + "%");
                }

                //음수량 프로그레스바 양 얻어오기
                int pValue = waveLoadingView.getProgressValue();

                //음수량 프로그레스바가 일정 높이 이상이면 안의 텍스트 색상을 변경함
                if(pValue >= 60) {
                    waveLoadingView.setCenterTitleColor(Color.WHITE);
                }
                if(pValue >= 100) {
                    waveLoadingView.setTopTitleColor(Color.WHITE);
                }
            }
        });

        //음수량 프로그레스바 클릭 이벤트
        waveLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), RecordActivity.class);
                startActivity(intent);
            }
        });



       return view;
    }//onCreate



    public String getFragmentText() {
        String s = edtIntake.getText().toString();
        return s;
    }

    public void setFragmentText() {
        SharedPreferences sp2 = this.getActivity().getSharedPreferences("WaterData",Context.MODE_PRIVATE);
        int water = sp2.getInt("per_day_water", -1);
        edtIntake.setText(String.valueOf(water));

    }

}