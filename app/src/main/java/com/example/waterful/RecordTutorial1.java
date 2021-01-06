package com.example.waterful;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecordTutorial1 extends Fragment {
    TextView text1;
    public RecordTutorial1() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.record_tutorial1, container, false);
        text1 = rootView.findViewById(R.id.text1);

        String content = text1.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "기록 페이지";
        int start = content.indexOf(word);
        int end = start + word.length();
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#5F0080")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);    //글씨색
        spannableString.setSpan(new RelativeSizeSpan(1.3f), 0, 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);         //다른 글씨의 1.3배 크기
        text1.setText(spannableString);
        return rootView;
    }
}