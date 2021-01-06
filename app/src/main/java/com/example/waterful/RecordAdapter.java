package com.example.waterful;

/* 메인 페이지 - 리사이클러뷰 구현을 위한 기록 어댑터 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //RecyclerView의 행을 표시하는 클래스 정의
    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecordDate, tvRecordIntake;

        RecordViewHolder(View view) {
            super(view);
            tvRecordDate = view.findViewById(R.id.tvRecordDate);
            tvRecordIntake = view.findViewById(R.id.tvRecordIntake);
        }
    } //MyViewHolder end

        private ArrayList<RecordInfo> recordInfoArrayList;
        RecordAdapter(ArrayList<RecordInfo> recordInfoArrayList) {
            this.recordInfoArrayList = recordInfoArrayList;
        }

        //RecyclerView의 행을 표시하는데 사용되는 레이아웃 xml 가져옴
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_record, parent, false);

            return new RecordViewHolder(v);
        }

        //RecyclerView의 행에 보여질 tv 설정
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            RecordViewHolder recordViewHolder = (RecordViewHolder)holder;

            recordViewHolder.tvRecordDate.setText(recordInfoArrayList.get(position).recordDate);
            recordViewHolder.tvRecordIntake.setText(recordInfoArrayList.get(position).intake);

        }

        //RecyclerView의 행 갯수 리턴
        @Override
        public int getItemCount() {
            return recordInfoArrayList.size();
        }

}
