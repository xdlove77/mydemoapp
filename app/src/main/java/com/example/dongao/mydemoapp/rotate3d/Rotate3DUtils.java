package com.example.dongao.mydemoapp.rotate3d;

import android.support.annotation.Size;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Created by fishzhang on 2018/4/4.
 */

public class Rotate3DUtils {

    private View[] targetChilds;
    private View target;
    private Rotate3DAnimationListener listener;

    public Rotate3DUtils(View target,@Size(2) View[] targetChilds,Rotate3DAnimationListener listener) {
        this.target=target;
        this.targetChilds = targetChilds;
        this.listener=listener;
    }

    public void startAnimation(boolean fromSecond,boolean isClockwise,float depthZ,int duration){
        float formDegress = 0f;
        float toDegress = 180f;
        if (isClockwise){
            toDegress=-toDegress;
        }
        float centerX = target.getWidth()/2.0f;
        float centerY = target.getHeight()/2.0f;
        Rotate3DAnimation animation=new Rotate3DAnimation(depthZ,formDegress
                ,(formDegress+toDegress)/2.0f, centerX, centerY,true,target.getContext().getResources().getDisplayMetrics());
        animation.setDuration(duration/2);
        animation.setFillAfter(false);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new MyListenerClass(targetChilds,target,listener,centerX
                ,centerY,(formDegress+toDegress)/2.0f,formDegress+toDegress,depthZ,fromSecond,duration/2));
        target.startAnimation(animation);
    }




    private static class Rotate3DSecondAnimationExe implements Runnable,Animation.AnimationListener{
        private View target;
        private Rotate3DAnimationListener listener;
        private float centerX;
        private float centerY;
        private float formDegress;
        private float toDegress;
        private float depthZ;
        private boolean fromOther;
        private int duration;
        private View[] targetChilds;

        public Rotate3DSecondAnimationExe(View target, Rotate3DAnimationListener listener, View[] targetChilds,float centerX
                , float centerY, float formDegress, float toDegress, float depthZ, boolean fromOther, int duration) {
            this.targetChilds=targetChilds;
            this.target = target;
            this.listener = listener;
            this.centerX = centerX;
            this.centerY = centerY;
            this.formDegress = formDegress;
            this.toDegress = toDegress;
            this.depthZ = depthZ;
            this.fromOther = fromOther;
            this.duration = duration;
        }

        @Override
        public void run() {
            if (targetChilds != null && targetChilds.length>0){
                if (fromOther){
                    targetChilds[0].setVisibility(View.VISIBLE);
                    targetChilds[0].requestFocus();
                    targetChilds[1].setVisibility(View.GONE);
                }else{
                    targetChilds[1].setVisibility(View.VISIBLE);
                    targetChilds[1].requestFocus();
                    targetChilds[0].setVisibility(View.GONE);
                }
            }
            Rotate3DAnimation animation=new Rotate3DAnimation(depthZ,formDegress,toDegress,centerX,centerY,false
                    ,target.getContext().getResources().getDisplayMetrics());
            animation.setDuration(duration);
            animation.setInterpolator(new LinearInterpolator());
            animation.setFillAfter(false);
            animation.setAnimationListener(this);
            target.startAnimation(animation);
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (listener!=null)
                listener.center(animation,fromOther);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (listener!=null)
                listener.finish(animation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private static final class MyListenerClass implements Animation.AnimationListener{

        private View[] targetChilds;
        private View target;
        private Rotate3DAnimationListener listener;
        private float centerX;
        private float centerY;
        private float formDegress;
        private float toDegress;
        private float depthZ;
        private boolean fromOther;
        private int duration;

        public MyListenerClass(View[] targetChilds, View target, Rotate3DAnimationListener listener, float centerX, float centerY
                , float formDegress, float toDegress, float depthZ, boolean fromOther, int duration) {
            this.targetChilds = targetChilds;
            this.target = target;
            this.listener = listener;
            this.centerX = centerX;
            this.centerY = centerY;
            this.formDegress = formDegress;
            this.toDegress = toDegress;
            this.depthZ = depthZ;
            this.fromOther = fromOther;
            this.duration = duration;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (listener!=null)
                listener.start(animation);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            target.post(new Rotate3DSecondAnimationExe(target,listener,targetChilds,centerX,centerY
                    ,formDegress+180.0f,toDegress+180.0f,depthZ,fromOther,duration));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
