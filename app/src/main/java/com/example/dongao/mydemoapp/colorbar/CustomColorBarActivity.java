package com.example.dongao.mydemoapp.colorbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dongao.mydemoapp.R;

public class CustomColorBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_color_bar);
        BarUtils.setBarColor(this, Color.GREEN,false);
    }
}
