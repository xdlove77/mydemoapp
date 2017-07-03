package com.example.dongao.mydemoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TransitionActivity extends AppCompatActivity {
    private Scene scene1,scene2;
    private boolean isScene2;
    private ImageView demo2_iv1,demo2_iv2,demo2_iv3,demo2_iv4;
    private ViewGroup rootView;
    private boolean isImageBagger;
    private View shared_element_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        initToolBar();
        initView();

    }

    private void initToolBar() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initView() {
        rootView = (ViewGroup) findViewById(R.id.activity_transition);
        // demo1 -------------------------------
//
//        scene1=Scene.getSceneForLayout(rootView,R.layout.transition_scene1,this);
//        scene2=Scene.getSceneForLayout(rootView,R.layout.trasition_scene2,this);
//
//        TransitionManager.go(scene1);

        // demo2 -------------------------------
        demo2_iv1= (ImageView) findViewById(R.id.demo2_iv1);
//        demo2_iv2= (ImageView) findViewById(R.id.demo2_iv2);
//        demo2_iv3= (ImageView) findViewById(R.id.demo2_iv3);
//        demo2_iv4= (ImageView) findViewById(R.id.demo2_iv4);

        // demo3 -------------------------------
        getWindow().setExitTransition(new Slide(Gravity.BOTTOM).setDuration(500));

        // demo4 -------------------------------
        shared_element_tv = findViewById(R.id.shared_element_tv);
    }

    public void onClick(View v){
        // demo1 -------------------------------
//        TransitionManager.go(isScene2?scene1:scene2,new Explode());
//        isScene2=!isScene2;

        // demo2 -------------------------------
//        TransitionManager.beginDelayedTransition(rootView, TransitionInflater.from(this).inflateTransition(R.transition.demo1));
//        changeScene(v);

        // demo3 -------------------------------
//        Intent intent=new Intent(this,Transition2Activity.class);
//        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());

        // demo4 -------------------------------
        Intent intent=new Intent(this,Transition2Activity.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation
                (this,new Pair<View, String>(demo2_iv1,"title_iv")
                ,new Pair<View, String>(shared_element_tv,"title_name")).toBundle());
    }

    private void changeScene(View v) {
        changeSize(v);
        changeVisible(demo2_iv1,demo2_iv2,demo2_iv3,demo2_iv4);
        v.setVisibility(View.VISIBLE);
    }

    private void changeVisible(View... v) {
        for (View view : v) {
            view.setVisibility(view.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
        }
    }

    private void changeSize(View v) {
        isImageBagger =!isImageBagger;
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (isImageBagger){
            lp.height= (int) (1.5f*dp2px(this,68));
            lp.width= (int) (1.5f*dp2px(this,45));
        }else{
            lp.height= dp2px(this,68);
            lp.width= dp2px(this,45);
        }
        v.setLayoutParams(lp);
    }

    private int dp2px(Context context,int dp){
        return (int) (context.getResources().getDisplayMetrics().density*dp);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
