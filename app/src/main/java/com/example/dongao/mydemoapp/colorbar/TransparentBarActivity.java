package com.example.dongao.mydemoapp.colorbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dongao.mydemoapp.R;

public class TransparentBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_bar);
        BarUtils.setBarColor(this, Color.GRAY,0,true);
    }
}
