package com.example.waterful;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SettingFragment extends Fragment {
private View view;
TextView tvPrivacy, tvReConnect, tvService, tvSettingUserName;

    SharedPreferences sp;
    public static final String MyPREFERENCES = "WaterfulApp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        sp = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //이름 DB에서 받아와서 바꿔줌
        tvSettingUserName = view.findViewById(R.id.tvSettingUserName);
        String nickname = sp.getString("nickname","");
        tvSettingUserName.setText(nickname);


        //개인정보 설정 클릭 이벤트
        tvPrivacy = view.findViewById(R.id.tvPrivacy);
        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetPrivacyActivity.class);
                startActivity(intent);
            }
        });

        //코스터 연동하기 클릭 이벤트
        tvReConnect = view.findViewById(R.id.tvReConnect);
        tvReConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConnectActivity.class);
                startActivity(intent);
            }
        });

        //고객센터 클릭 이벤트
        tvService = view.findViewById(R.id.tvService);
        tvService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServiceActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}