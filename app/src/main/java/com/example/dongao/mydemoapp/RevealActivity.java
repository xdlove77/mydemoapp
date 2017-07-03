package com.example.dongao.mydemoapp;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class RevealActivity extends AppCompatActivity {
    private Interpolator interpolator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal);
        setupWindowsAnmation();
    }

    private void setupWindowsAnmation() {
        interpolator= AnimationUtils.loadInterpolator(this,android.R.interpolator.linear_out_slow_in);
        setupEnterAniamtion();
        setupEndAnimaiton();
    }

    private void setupEnterAniamtion() {
//        Transition transition=
    }

    private void setupEndAnimaiton() {

    }


}
