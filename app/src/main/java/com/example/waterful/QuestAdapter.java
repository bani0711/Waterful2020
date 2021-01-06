package com.example.waterful;

/* 업적 페이지 - 리사이클러뷰를 구현하기 위한 내 업적 어댑터 */

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;

public class QuestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //RecyclerView의 행을 표시하는 클래스 정의
    Context context;

    public static class QuestViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuest;
        TextView tvQuestPoint;
        ImageView ivComplete;

        SharedPreferences sp3;
        public static final String MyPREFERENCES3 = "Achiv";

        QuestViewHolder(View view) {
            super(view);
            tvQuest = view.findViewById(R.id.tvQuest);
            tvQuestPoint = view.findViewById(R.id.tvQuestPoint);
            //
            ivComplete = view.findViewById(R.id.ivComplete);
        }
    } //QuestViewHolder end

    private ArrayList<QuestInfo> questInfoArrayList;
    QuestAdapter(ArrayList<QuestInfo> questInfoArrayList) {
        this.questInfoArrayList = questInfoArrayList;
    }

    //RecyclerView의 행을 표시하는데 사용되는 레이아웃 xml 가져옴
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_quest, parent, false);

        return new QuestAdapter.QuestViewHolder(v);
    }

    //RecyclerView의 행에 보여질 tv 설정
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuestAdapter.QuestViewHolder questViewHolder = (QuestAdapter.QuestViewHolder)holder;

        questViewHolder.tvQuest.setText(questInfoArrayList.get(position).quest);
        questViewHolder.tvQuestPoint.setText(questInfoArrayList.get(position).questPoint);

        /*//index(position) 값에 따라 완료 표시하기 (삭제예정)
        if(position == 12) {
            ((QuestViewHolder) holder).ivComplete.setVisibility(View.VISIBLE);
        } else if(position == 13) {
            ((QuestViewHolder) holder).ivComplete.setVisibility(View.VISIBLE);
        }*/
    }

    //RecyclerView의 행 갯수 리턴
    @Override
    public int getItemCount() {
        return questInfoArrayList.size();
    }
    public QuestAdapter(Context context) {
        this.context = context;
    }
}