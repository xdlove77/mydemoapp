package com.example.dongao.mydemoapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import com.example.dongao.mydemoapp.fragment.RotateFragment1;
import com.example.dongao.mydemoapp.fragment.RotateFragment2;
import com.example.dongao.mydemoapp.rotate3d.SwitchCallback;
import com.example.dongao.mydemoapp.rotate3d.Rotate3DAnimationListener;
import com.example.dongao.mydemoapp.rotate3d.Rotate3DUtils;

public class Rotate3DActivity extends AppCompatActivity implements SwitchCallback, Rotate3DAnimationListener {


    private View arl;
    private RotateFragment1 f1;
    private RotateFragment2 f2;
    private Rotate3DUtils rotate3D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate3d);
        arl = findViewById(R.id.animation_root_layout);
        f1 = new RotateFragment1();
        f2 = new RotateFragment2();
        f1.setSwitchCallback(this);
        f2.setSwitchCallback(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.animation_root_layout,f1);
        ft.add(R.id.animation_root_layout,f2);
        ft.hide(f2);
        ft.show(f1);
        ft.commit();
        rotate3D = new Rotate3DUtils(arl,null,this);
    }

    @Override
    public void switchOther(boolean fromFirst) {
        float depthZ=200.0f;
        int duration=600;
        if (fromFirst){
            rotate3D.startAnimation(false,false,depthZ,duration);
        }else{
            rotate3D.startAnimation(true,true,depthZ,duration);
        }
    }

    @Override
    public void start(Animation animation) {
        f1.startRotate();
        f2.startRotate();
    }

    @Override
    public void center(Animation animation,boolean fromSecond) {
        f1.centerRotate();
        f2.centerRotate();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fromSecond){
            ft.hide(f2);
            ft.show(f1);
        }else{
            ft.hide(f1);
            ft.show(f2);
        }
        ft.commit();
    }

    @Override
    public void finish(Animation animation) {
        f1.endRotate();
        f2.endRotate();
    }
}
