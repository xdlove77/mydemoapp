package com.example.dongao.mydemoapp;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;

public class ViewAnimationUtilsActivity extends AppCompatActivity {

    private View layout;
    private FloatingActionButton floatingActionButton;
    private Animator circularReveal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation_utils);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        layout = findViewById(R.id.layout);
//        layout.setBackgroundColor(ContextCompat.getColor(ViewAnimationUtilsActivity.this,R.color.colorAccent));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularReveal.start();
                layout.setBackgroundColor(ContextCompat.getColor(ViewAnimationUtilsActivity.this,R.color.colorAccent));

            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int right = layout.getRight();
        int left = layout.getLeft();
        int top = layout.getTop();
        int bottom = layout.getBottom();
        circularReveal = ViewAnimationUtils.createCircularReveal(layout, (right-left) / 2, (bottom-top) / 2, 100, (float) (Math.hypot((right-left),(bottom-top))/2));

    }
}
