package com.example.waterful;

/* 설정 페이지 - 고객센터 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {
private RecyclerView recyclerViewS;
Toolbar toolbarService;
ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        //Toolbar 구현 코드
        toolbarService = findViewById(R.id.toolbarService);
        setSupportActionBar(toolbarService);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //뒤로가기 아이콘
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24);

        recyclerViewS = findViewById(R.id.recyclerViewS);
        recyclerViewS.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ServiceAdapter.Item> data = new ArrayList<>();

        //고객센터 QnA 질문 및 답변 추가
        data.add(new ServiceAdapter.Item(ServiceAdapter.HEADER, "Q. 질문 1"));
        data.add(new ServiceAdapter.Item(ServiceAdapter.CHILD, "A. 질문 답변 1"));

        ServiceAdapter.Item qna2 = new ServiceAdapter.Item(ServiceAdapter.HEADER, "Q. 질문 2");
        qna2.invisibleChildren = new ArrayList<>();
        qna2.invisibleChildren.add(new ServiceAdapter.Item(ServiceAdapter.CHILD, "A. 질문 답변 2"));

        ServiceAdapter.Item qna3 = new ServiceAdapter.Item(ServiceAdapter.HEADER, "Q. 질문 3");
        qna3.invisibleChildren = new ArrayList<>();
        qna3.invisibleChildren.add(new ServiceAdapter.Item(ServiceAdapter.CHILD, "A. 질문 답변 3"));

        ServiceAdapter.Item qna4 = new ServiceAdapter.Item(ServiceAdapter.HEADER, "Q. 질문 4");
        qna4.invisibleChildren = new ArrayList<>();
        qna4.invisibleChildren.add(new ServiceAdapter.Item(ServiceAdapter.CHILD, "A. 질문 답변 4"));

        //추가된 사항 적용
        data.add(qna2);
        data.add(qna3);
        data.add(qna4);

        //어뎁터 적용
        recyclerViewS.setAdapter(new ServiceAdapter(data));
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
}