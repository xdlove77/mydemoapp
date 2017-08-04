package com.example.dongao.mydemoapp.colorbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dongao.mydemoapp.R;

public class HideBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_bar);
        BarUtils.setBarColorForDrawerLayout(this, Color.GREEN,100,true);
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        BarUtils.setFullScreen(this);
//    }
}
