package com.example.waterful;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class PrivacyActivity extends AppCompatActivity {
    Button btnWoman, btnMan, btnNext;
    EditText btnNickname, btnAge, btnWeight;
    ImageView ivUserImage;

    static final int REQUEST_CODE = 1;
    Uri ur;

    String url = "http://192.168.0.102:8080/Waterful/user";

    SharedPreferences sp;
    public static final String MyPREFERENCES = "WaterfulApp";

    ContentValues values = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btnNext = findViewById(R.id.btnNext);
        btnNickname = findViewById(R.id.btnNickname);
        btnAge = findViewById(R.id.btnAge);
        btnWeight = findViewById(R.id.btnWeight);
        btnWoman = findViewById(R.id.btnWoman);
        btnMan = findViewById(R.id.btnMan);
        ivUserImage = findViewById(R.id.ivUserImage);

        //사용자 사진첩에서 이미지 선택
        ivUserImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });



        // 남, 여 버튼 클릭시
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnWoman:
                        btnWoman.setSelected(true);
                        btnMan.setSelected(false);
                        btnWoman.setHintTextColor(Color.WHITE);
                        btnMan.setHintTextColor(Color.GRAY);
                        break;
                    case R.id.btnMan:
                        btnMan.setSelected(true);
                        btnWoman.setSelected(false);
                        btnMan.setHintTextColor(Color.WHITE);
                        btnWoman.setHintTextColor(Color.GRAY);
                        break;
                }
            }
        };
        btnWoman.setOnClickListener(onClickListener);
        btnMan.setOnClickListener(onClickListener);


        //다음 버튼 눌렀을 때 코스터 연동 페이지로 이동
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nickname = btnNickname.getText().toString();
                String Age = btnAge.getText().toString();
                String Weight = btnWeight.getText().toString();

                if (Nickname.getBytes().length == 0 || Age.getBytes().length == 0 || Weight.getBytes().length == 0) {
                    Toast.makeText(PrivacyActivity.this, "빈 칸을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    values.put("nickname", btnNickname.getText().toString());
                    values.put("age", btnAge.getText().toString());
                    values.put("weight", btnWeight.getText().toString());
                    if (btnMan.isSelected()) {
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

                    Intent intent = new Intent(getApplicationContext(), Device1Activity.class);
                    startActivity(intent);
                }
            }
        });
    }

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
                ivUserImage.setImageBitmap(bitmap);

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
                    editor.putInt("user_achiv_point", obj.getInt("user_achiv_point"));
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