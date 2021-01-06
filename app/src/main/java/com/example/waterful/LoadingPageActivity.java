package com.example.waterful;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class LoadingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        startLoading();
    }

    //첫 실행 여부 판단
    private  void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //일단 튜토리얼 페이지로 이동 나중에 삭제하고 밑에 주석처리 없애고 밑에 코드로 실행
                Intent intent = new Intent(LoadingPageActivity.this, Intro0Activity.class);
                startActivity(intent);
                finish();

                //최소 실행 여부 판단
                SharedPreferences preferences = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE);
                boolean checkFirst = preferences.getBoolean("checkFirst", false);
                if(checkFirst==false){
                    //앱 최초 실행이라면 코스터 연동 화면으로 이동
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("checkFirst",true);
                    editor.commit();

                    Intent intent2 = new Intent(LoadingPageActivity.this, Intro0Activity.class);
                    startActivity(intent2);
                    finish();
                }else{
                    //그 외 실행이라면 메인페이지로 이동
                    Intent intent3 = new Intent(LoadingPageActivity.this, MainActivity.class);
                    startActivity(intent3);
                    finish();
                }

            }
        }, 2000);       //2초 후 이동
    }
}