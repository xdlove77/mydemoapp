package com.example.dongao.mydemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dongao.mydemoapp.colorbar.CustomColorBarActivity;
import com.example.dongao.mydemoapp.colorbar.HideBarActivity;
import com.example.dongao.mydemoapp.colorbar.TranslucentBarActivity;
import com.example.dongao.mydemoapp.colorbar.TransparentBarActivity;

public class StatusBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar);
    }

    public void onCustomColorBar(View view) {
        Intent intent=new Intent(this, CustomColorBarActivity.class);
        startActivity(intent);
    }

    public void onCustomTranslucentBar(View view) {
        Intent intent=new Intent(this, TranslucentBarActivity.class);
        startActivity(intent);
    }

    public void onCustomTransparentBar(View view) {
        Intent intent=new Intent(this, TransparentBarActivity.class);
        startActivity(intent);
    }

    public void onCustomInvisiableBar(View view) {
        Intent intent=new Intent(this, HideBarActivity.class);
        startActivity(intent);
    }
}
