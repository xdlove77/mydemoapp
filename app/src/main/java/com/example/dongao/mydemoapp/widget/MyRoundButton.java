package com.example.dongao.mydemoapp.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by xd on 2017/5/3.
 */

@SuppressLint("AppCompatCustomView")
public class MyRoundButton extends TextView implements View.OnClickListener {

    private Paint bgPaint;
    private Paint vPaint;
    protected int width;
    protected int height;
    private boolean isGoing;
    private RectF btr;
    protected float two_circle_distance;
    protected float rx;
    private Path path;
    private PathMeasure pathMeasure;
    protected boolean isVStart;

    public MyRoundButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.GRAY);
        bgPaint.setStyle(Paint.Style.FILL);

        vPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        vPaint.setStyle(Paint.Style.STROKE);
        vPaint.setColor(Color.WHITE);
        vPaint.setStrokeWidth(7);

        btr = new RectF();
        path=new Path();
        pathMeasure=new PathMeasure();

        setOnClickListener(this);
    }

    private void initOk() {
        path.moveTo((width-height)/2 + height / 8 * 3, height / 2);
        path.lineTo((width-height)/2 + height / 2, height / 5 * 3);
        path.lineTo((width-height)/2 + height / 3 * 2, height / 5 * 2);
        pathMeasure.setPath(path,true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
        initOk();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        btr.left=two_circle_distance;
        btr.top=0;
        btr.right=width-two_circle_distance;
        btr.bottom=height;
        canvas.drawRoundRect(btr,rx,rx,bgPaint);
        if (isVStart){
            canvas.drawPath(path,vPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        if (!isGoing){
            isGoing=true;
            AnimatorSet set=new AnimatorSet();
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isGoing=false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            ValueAnimator rAnimator=ValueAnimator.ofFloat(0,height/2);
            rAnimator.setDuration(500);
            rAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    rx=animatedValue;
                    invalidate();
                }
            });

            ValueAnimator dAnimator=ValueAnimator.ofFloat(0,(width-height)/2);
            dAnimator.setDuration(500);
            dAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    two_circle_distance=animatedValue;
                    invalidate();
                }
            });

            ValueAnimator aAnimator=ValueAnimator.ofArgb(0x11ffffff,0x00ffffff);
            aAnimator.setDuration(500);
            aAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    setTextColor(animatedValue);
                    invalidate();
                }
            });

            ValueAnimator vAnimator=ValueAnimator.ofFloat(1,0);
            vAnimator.setDuration(500);
            vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    isVStart=true;
                    float animatedValue = (float) animation.getAnimatedValue();
                    DashPathEffect pathEffect=new DashPathEffect(new float[]{pathMeasure.getLength()
                            ,pathMeasure.getLength()},animatedValue* pathMeasure.getLength());
                    vPaint.setPathEffect(pathEffect);
                    invalidate();
                }
            });

            set.play(dAnimator)
                    .with(rAnimator)
                    .before(aAnimator)
                    .before(vAnimator);
            set.start();
        }
    }
}
