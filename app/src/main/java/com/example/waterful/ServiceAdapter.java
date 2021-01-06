package com.example.waterful;

/* 설정 페이지 - 리사이클러뷰 구현을 위한 고객센터 어댑터 */

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Item> data;

    public ServiceAdapter(List<Item> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        switch (type) {
            //헤더(질문) 부분 상세 설정
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.recycler_view_service, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            //서브(답변) 부분 상세 설정
            case CHILD:
                TextView itemTextView = new TextView(context);
                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                itemTextView.setTextColor(Color.BLACK);
                itemTextView.setLayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                return new RecyclerView.ViewHolder(itemTextView) {
                };
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.tvQuestion.setText(item.text);
                if (item.invisibleChildren == null) {
                    itemController.ibtnExpand.setImageResource(R.drawable.ic_collapse_white);
                    itemController.lineService.setBackgroundColor(Color.parseColor("#5F0080"));
                    itemController.tvQuestion.setTextColor(Color.WHITE);
                } else {
                    itemController.ibtnExpand.setImageResource(R.drawable.ic_expand);
                    itemController.lineService.setBackgroundColor(Color.WHITE);
                    itemController.tvQuestion.setTextColor(Color.BLACK);
                }
                itemController.ibtnExpand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.lineService.setBackgroundColor(Color.WHITE);
                            itemController.tvQuestion.setTextColor(Color.BLACK);
                            itemController.ibtnExpand.setImageResource(R.drawable.ic_expand);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.ibtnExpand.setImageResource(R.drawable.ic_collapse_white);
                            itemController.lineService.setBackgroundColor(Color.parseColor("#5F0080"));
                            itemController.tvQuestion.setTextColor(Color.WHITE);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                TextView itemTextView = (TextView) holder.itemView;
                itemTextView.setText(data.get(position).text);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView tvQuestion;
        public ImageView ibtnExpand;
        public LinearLayout lineService;
        public Item refferalItem;


        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);
            ibtnExpand = (ImageView) itemView.findViewById(R.id.ibtnExpand);
            lineService = (LinearLayout) itemView.findViewById(R.id.lineService);
        }
    }

    public static class Item {
        public int type;
        public String text;
        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }
    }
}
