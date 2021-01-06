package com.example.waterful;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class Device1Activity extends AppCompatActivity {
    Button btnConnect;
    public BluetoothSPP bt;
    public static Context context;
    TextView tvRecordDate, tvRecordIntake;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device1);


        bt = new BluetoothSPP(this); //initializing
        context = this;

        btnConnect = findViewById(R.id.btnConnect);

        tvRecordDate = findViewById(R.id.tvRecordDate);
        tvRecordIntake = findViewById(R.id.tvRecordIntake);

        //블루투스 사용 불가
        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }

        //데이터 수신 (url 여기에 작성!!!)
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {

            }
        });

        //블루투스 연결
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            //연결됐을 때(연결성공)
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext(), name + "와(과) 연결되었습니다." +  "\n" + address, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), Intro4Activity.class);
                startActivity(intent);
            }

            @Override
            //연결해제
            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext(), "연결이 해제되었습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            //연결실패
            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext(), "연결에 실패했습니다. 다시 확인해주세요.", Toast.LENGTH_LONG).show();
            }
        });

        //아두이노 연동 페이지로 이동
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    //기기 선택
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    intent.putExtra("no_devices_found", "디바이스가 없습니다.");
                    intent.putExtra("scan_for_devices", "디바이스 찾기");
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

    }

    //앱 중단시
    public void onDestroy() {
        super.onDestroy();
    }

    //앱 시작할 때 동작
    public void onStart() {
        super.onStart();
        if(!bt.isBluetoothEnabled()) {
            //블루투스 비활성화 된 경우 조치
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //앱의 상태를 보고 블루투스 사용이 가능하다면
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT); //새로운 액티비티 띄우기 및 현재 가능한 블루투스 정보 intent로 넘김
        } else {
            //블루투스가 이미 활성화 된 경우 조치
            if (!bt.isServiceAvailable()) { //블루투스 사용 불가 시
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
            }
        }
    }

    //장치 활동을 선택하고 해당 활동을 완료하려는 것. 결과 데이터 확인해야 함
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) { //연결시도
            if (resultCode == Activity.RESULT_OK) //연결됨
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) { //연결가능
            if (resultCode == Activity.RESULT_OK) { //연결됨
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            } else { //사용불가
                //사용자가 장치를 선택하지 않은 경우
                Toast.makeText(getApplicationContext(), "Bluetooth was not enabled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }//onCreate
}
