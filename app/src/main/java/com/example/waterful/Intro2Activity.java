package com.example.waterful;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class Intro2Activity extends Fragment {
    TextView text0;
    public Intro2Activity() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_intro2, container, false);
        text0 = rootView.findViewById(R.id.text0);

        String content = text0.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "코스터를 준비";
        int start = content.indexOf(word);
        int end = start + word.length();
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#5F0080")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);    //글씨색
        spannableString.setSpan(new RelativeSizeSpan(1.3f), 9, 17, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);         //다른 글씨의 1.3배 크기
        text0.setText(spannableString);

        return rootView;
    }
}