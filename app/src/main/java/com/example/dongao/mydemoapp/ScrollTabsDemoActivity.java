package com.example.dongao.mydemoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dongao.mydemoapp.widget.ScrollTabsLayout;

public class ScrollTabsDemoActivity extends AppCompatActivity implements ScrollTabsLayout.ScrollTabListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_tabs_demo);
        ScrollTabsLayout scrollTabsLayout= (ScrollTabsLayout) findViewById(R.id.scrollTabsLayout);
        scrollTabsLayout.setScrollTabListener(this);
    }

    public void onClick(View view) {
        int position=0;
        switch (view.getId()){
            case R.id.iv1:
                position=1;
                break;
            case R.id.iv2:
                position=2;
                break;
            case R.id.iv3:
                position=3;
                break;
            case R.id.iv4:
                position=4;
                break;
            case R.id.iv5:
                position=5;
                break;
            case R.id.iv6:
                position=6;
                break;
            case R.id.iv7:
                position=7;
                break;
            case R.id.iv8:
                position=8;
                break;
            case R.id.iv9:
                position=9;
                break;
        }
        Toast.makeText(this, "iv"+position+" : clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void scrollEnd(int page) {
//        Log.d("hhh", "page = "+page);
    }

    private int distance=0;
    @Override
    public void scrolling(int dist) {
        distance+=dist;
//        Log.d("hhh","distance = "+distance);
    }
}
