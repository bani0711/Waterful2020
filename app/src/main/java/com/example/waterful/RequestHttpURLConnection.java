package com.example.waterful;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class RequestHttpURLConnection {

    public JSONArray request(String _url, ContentValues _params) {

        HttpURLConnection conn;
        String jsonData = "";
        BufferedReader br = null;
        StringBuffer sb = null;
        JSONArray jsonArray = null;

        // URL 뒤에 붙여서 보낼 파라미터
        StringBuffer sbParams = new StringBuffer();

        /**
         * 1. StringBuffer에 파라미터 연결
         * */
        // 보낼 데이터가 없으면 파라미터를 비운다
        if (_params == null)
            sbParams.append("");

            // 보낼 데이터가 있으면 파라미터를 채운다
        else {
            //보낼 데이터가 있으면 ?를 붙인다
            sbParams.append("?");
            // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성
            boolean isAnd = false;
            // 파라미터 키와 값
            String key;
            String value;

            for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다
                if (isAnd)
                    sbParams.append("&");

                sbParams.append(key).append("=").append(value);

                // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다
                if (!isAnd)
                    if (_params.size() >= 2)
                        isAnd = true;
            }
        }

        /**
         * 2. HttpURLConnection을 통해 데이터를 가져온다
         * */

        try {
            String strParams = sbParams.toString();
            URL url = new URL(_url + strParams);
            Log.d("http_test", "url = " + url.toString());
            conn = (HttpURLConnection) url.openConnection();

            //커넥션에 각종 정보 설정
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            //응답 http코드를 가져옴
            int responseCode = conn.getResponseCode();

            //응답이 성공적으로 완료되었을 때
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                sb = new StringBuffer();

                while ((jsonData = br.readLine()) != null) {
                    sb.append(jsonData);
                }

                jsonArray = new JSONArray(sb.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
        return jsonArray;
    }
}