package com.example.dongao.mydemoapp.widget.ptrviewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.dongao.mydemoapp.R;

public class LoadMoreView extends FrameLayout implements PtrRefreshAndLoadMoreViewListener{

    private TextView loadTv;
    private View loadImg;
    public LoadMoreView(@NonNull Context context) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.load_more_view,null,false));
        loadTv = (TextView) findViewById(R.id.loadTv);
        loadImg = findViewById(R.id.loadImg);
    }

    @Override
    public void pull(float percent) {
        loadImg.setRotation(percent*360);
        loadTv.setText("向\n左\n拉\n动\n加\n载\n更\n多");
    }

    @Override
    public void needRelease() {
        loadImg.setRotation(90);
        loadTv.setText("松\n开\n加\n载\n更\n多");
    }

    @Override
    public void loading() {
        loadImg.setRotation(180);
        loadTv.setText("加\n载\n中");
    }

    @Override
    public void finish() {
        loadImg.setRotation(270);
        loadTv.setText("加\n载\n完\n成");
    }

    @Override
    public void end() {
        loadImg.setRotation(3600);
        loadTv.setText("没\n有\n更\n多");
    }

    @Override
    public void error() {

    }
}
