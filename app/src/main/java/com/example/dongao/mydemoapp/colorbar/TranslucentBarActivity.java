package com.example.dongao.mydemoapp.colorbar;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dongao.mydemoapp.R;

public class TranslucentBarActivity extends AppCompatActivity {

    private DegsinFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translucent_bar);
        BarUtils.setBarColorForDrawerLayout(this,Color.GREEN,100,true);
        fragment = new DegsinFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, fragment);
        ft.show(fragment);
        ft.commit();

    }
}
