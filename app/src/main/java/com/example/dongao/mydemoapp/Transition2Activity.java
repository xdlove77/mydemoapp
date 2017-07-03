package com.example.dongao.mydemoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import java.util.HashMap;

public class Transition2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition2);
        initToolBar();
        ViewGroup viewGroup1 = (ViewGroup) findViewById(R.id.viewGroup1);
        viewGroup1.setTransitionGroup(true);
        ViewGroup viewGroup2 = (ViewGroup) findViewById(R.id.viewGroup2);
        viewGroup2.setTransitionGroup(true);
        getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.demo2_window_enter));
    }

    private void initToolBar() {
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
