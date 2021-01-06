package com.example.waterful;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

import static android.R.color.black;
import static com.example.waterful.PrivacyActivity.REQUEST_CODE;

public class SetPrivacyActivity extends AppCompatActivity {
    TextView tvDisconnection;
    Button btnSetWoman, btnSetMan, btnCheck;
    EditText btnSetNickname, btnSetAge, btnSetWeight;
    ImageView ivSetUserImage;
    private final int GET_GALLERY_IMAGE = 200;
    Toolbar toolbarAddSetPrivacy;
    ActionBar actionBar;
    Uri ur;


    String url = "http://192.168.0.102:8080/Waterful/user/info";

    SharedPreferences sp;
    public static final String MyPREFERENCES = "WaterfulApp";

    ContentValues values = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ucode = sp.getString("ucode","");
        values.put("ucode", ucode);

        SharedPreferences pre = getSharedPreferences("image", MODE_PRIVATE);
        String img = pre.getString("imagestrings", "");
        Bitmap bit = StringToBitmap(img);
        ivSetUserImage.setImageBitmap(bit);

        setContentView(R.layout.activity_setprivacy);
        btnCheck = findViewById(R.id.btnCheck);
        btnSetNickname = findViewById(R.id.btnSetNickname);
        btnSetAge = findViewById(R.id.btnSetAge);
        btnSetWeight = findViewById(R.id.btnSetWeight);
        btnSetWoman = findViewById(R.id.btnSetWoman);
        btnSetMan = findViewById(R.id.btnSetMan);
        ivSetUserImage = findViewById(R.id.ivSetUserImage);
        tvDisconnection = findViewById(R.id.tvDisconnection);

        //Toolbar 구현 코드
        toolbarAddSetPrivacy = findViewById(R.id.toolbarSetPrivacy);
        setSupportActionBar(toolbarAddSetPrivacy);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //뒤로가기 아이콘
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24);

        String nickname = sp.getString("nickname","");
        String age = String.valueOf(sp.getInt("age",0));
        String weight = String.valueOf(sp.getInt("weight",0));
        String sex = sp.getString("sex","");

        btnSetNickname.setText(nickname);
        btnSetAge.setText(age);
        btnSetWeight.setText(weight);
        if(sex == "여"){
            btnSetWoman.setSelected(true);
            btnSetMan.setSelected(false);
            btnSetWoman.setHintTextColor(Color.WHITE);
            btnSetMan.setHintTextColor(Color.GRAY);
        }
        else {
            btnSetMan.setSelected(true);
            btnSetWoman.setSelected(false);
            btnSetMan.setHintTextColor(Color.WHITE);
            btnSetWoman.setHintTextColor(Color.GRAY);
        }


        //사용자 사집첩에서 이미지 선택
        ivSetUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                //setDataAndType(mediaStore, image*) : 파일 연결
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });


        //남,여 선택버튼
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnSetWoman:
                        btnSetWoman.setSelected(true);
                        btnSetMan.setSelected(false);
                        btnSetWoman.setHintTextColor(Color.WHITE);
                        btnSetMan.setHintTextColor(Color.GRAY);
                        break;
                    case R.id.btnSetMan:
                        btnSetMan.setSelected(true);
                        btnSetWoman.setSelected(false);
                        btnSetMan.setHintTextColor(Color.WHITE);
                        btnSetWoman.setHintTextColor(Color.GRAY);
                        break;
                }
            }
        };
        btnSetWoman.setOnClickListener(onClickListener);
        btnSetMan.setOnClickListener(onClickListener);

        tvDisconnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessge();
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nickname = btnSetNickname.getText().toString();
                String Age = btnSetAge.getText().toString();
                String Weight = btnSetWeight.getText().toString();

                if (Nickname.getBytes().length == 0 || Age.getBytes().length == 0 || Weight.getBytes().length == 0) {
                    Toast.makeText(SetPrivacyActivity.this, "빈 칸을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    values.put("nickname", btnSetNickname.getText().toString());
                    values.put("age", btnSetAge.getText().toString());
                    values.put("weight", btnSetWeight.getText().toString());
                    if (btnSetMan.isSelected()) {
                        values.put("sex", "남");
                        try {
                            values.put("sex", URLEncoder.encode("남", "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            values.put("sex", URLEncoder.encode("여", "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    // AsyncTask를 통해 HttpURLConnection 수행.
                    NetworkTask networkTask = new NetworkTask(url, values);
                    networkTask.execute();

                    Intent intent = new Intent(getApplicationContext(), SettingFragment.class);
                    startActivity(intent);
                }
            }
        });
    }

    //안드로이드 폰으로 구현시 사용!!!!!!
    //사용자 이미지 핸드폰 사집첩에서 선택, 메인페이지와 개인설정페이지에서도 같은 이미지가 나오게 해야함
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE) {
            try{
                ur = data.getData();
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(in);     //bitmap으로 변환
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                if(width>500){
                    bitmap = Bitmap.createScaledBitmap(bitmap, (width=100), (height=height*100/width),true);
                }
                ivSetUserImage.setImageBitmap(bitmap);

                // 저장할 때
                String image = BitmapToString(bitmap);
                SharedPreferences pref = getSharedPreferences("image", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("imagestrings", image);
                editor.commit();

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }
    // 저장할 때
    public String BitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return  temp;
    }
    // 불러올 때
    public  Bitmap StringToBitmap(String encodedString){
        try{
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return  bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    //'연동 해제'를 눌렀을 때 경고창메세지, 해당 사용자 DB 데이터 삭제, 탈퇴처리, 자동으로 로그아웃 처리
    //여태까지 저장된 데이터 리셋(아이디, 비밀번호, 회원정보)
    public void showMessge(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고");
        builder.setMessage("연동 해제하시겠습니까?").setCancelable(false).
                setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BluetoothSPP bt2 = ((Device1Activity)Device1Activity.context).bt;
                        bt2.stopService(); //블루투스 중지

                        url = "http://192.168.0.102:8080/Waterful/user/remove";
                        NetworkTask networkTask = new NetworkTask(url, values);
                        networkTask.execute();

                        //로딩페이지로 넘어감
                        Intent intent = new Intent(SetPrivacyActivity.this, Intro0Activity.class);
                        startActivity(intent);
                        finish();

                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "아니오를 눌렀습니다", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    //툴바 뒤로가기 버튼 동작 메서드
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                finish();
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

            for (int i = 0; i < result.length(); i++) {
                JSONObject obj;
                SharedPreferences.Editor editor = sp.edit();
                try {
                    obj = result.getJSONObject(i);

                    editor.putString("ucode", obj.getString("ucode"));
                    editor.putString("nickname", obj.getString("nickname"));
                    editor.putString("sex", obj.getString("sex"));
                    editor.putInt("age", obj.getInt("age"));
                    editor.putInt("weight", obj.getInt("weight"));
                    editor.putInt("recommend", obj.getInt("recommend"));
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}